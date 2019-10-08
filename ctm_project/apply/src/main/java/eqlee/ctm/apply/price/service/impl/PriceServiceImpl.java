package eqlee.ctm.apply.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.channle.entity.Channel;
import eqlee.ctm.apply.channle.service.IChannelService;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.dao.PriceMapper;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceSelectVo;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements IPriceService {


    @Autowired
    private ILineService lineService;

    @Autowired
    private IChannelService channelService;

    @Autowired
    private LocalUser localUser;


    /**
     * 根据出行日期查询价格
     *
     * @param OutDate
     * @return
     */
    @Override
    public Price queryPrice(LocalDate OutDate) {
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getOutDate, OutDate);
        return baseMapper.selectOne(queryWrapper);

    }

    /**
     * 价格设定
     *
     * @param priceVo
     */
    @Override
    public void insertPrice(PriceVo priceVo) {
        //根据线路名查询该线路的Id插入数据库
        Line line = lineService.queryLineByName(priceVo.getLineName());
        Channel channel = channelService.selectChannelByType("代理");
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");
        IdGenerator idGenerator = new IdGenerator();
        //如果输入的开始时间和结束时间是同一天的话 或者其中一个时间为空  就只设定该天一天的价格
        if (priceVo.getStartTime().equals(priceVo.getEndTime()) || StringUtils.isBlank(priceVo.getStartTime())
                || StringUtils.isBlank(priceVo.getEndTime())) {
            LocalDate outDate = DateUtil.parseDate(priceVo.getStartTime());
            Price price = new Price();
            price.setId(idGenerator.getNumberId());
            price.setAdultPrice(priceVo.getAdultPrice());
            price.setBabyPrice(priceVo.getBabyPrice());
            price.setChildPrice(priceVo.getChildPrice());
            price.setOutDate(outDate);
            price.setLineId(line.getId());
            price.setCreateUserId(user.getId());
            price.setUpdateUserId(user.getId());
            // 渠道ID
            price.setChannelId(channel.getId());
            baseMapper.insert(price);
            return;
        }
        //输入的开始时间和结束时间不是同一天，就默认批量设置相同的价格
        LocalDateTime start = DateUtil.parseDateTime(priceVo.getStartTime() + " 00:00:00");
        LocalDateTime end = DateUtil.parseDateTime(priceVo.getEndTime() + " 00:00:00");
        Duration duration = Duration.between(start, end);
        long days = duration.toDays();
        //截取日期年 月  日
        String year = priceVo.getStartTime().substring(0, 4);
        String month = priceVo.getStartTime().substring(5, 7);
        String day = priceVo.getStartTime().substring(8, 10);
        //创建一个集合
        List<Price> list = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            Price price = new Price();
            price.setId(idGenerator.getNumberId());
            price.setAdultPrice(priceVo.getAdultPrice());
            price.setBabyPrice(priceVo.getBabyPrice());
            price.setChildPrice(priceVo.getChildPrice());
            price.setLineId(line.getId());

            price.setCreateUserId(user.getId());
            price.setUpdateUserId(user.getId());
            // 渠道ID
            price.setChannelId(channel.getId());
            //设置每一天的日期
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            c.add(Calendar.DAY_OF_MONTH, i);
            price.setOutDate(DateUtil.parseDate(sf.format(c.getTime())));
            list.add(price);
        }

        Integer result = baseMapper.allInsertPrice(list);

        if (result <= 0) {
            log.error("all insert price fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "批量设置价格失败");
        }

    }

    /**
     * 价格修改
     *
     * @param price
     */
    @Override
    public void updatePrice(Price price) {
        int result = baseMapper.updateById(price);

        if (result <= 0) {
            log.error("update price fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改价格失败");
        }
    }


    /**
     * 修改价格
     *
     * @param priceVo
     */
    public void updateFourPrice(Price price, PriceVo priceVo) {
        price.setAdultPrice(priceVo.getAdultPrice());
        price.setBabyPrice(priceVo.getBabyPrice());
        price.setChildPrice(priceVo.getChildPrice());
        price.setOldPrice(priceVo.getOldPrice());
    }

    @Override
    public synchronized void batchUpdatePrice(PriceVo priceVo) {
        Line line = lineService.queryLineByName(priceVo.getLineName());
        //根据线路id查找出对应的线路列表
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getLineId, line.getId());
        List<Price> pricesList = baseMapper.selectList(queryWrapper);
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        //如果输入的开始时间和结束时间是同一天的话 或者其中一个时间为空  就只设定该天一天的价格
        if (priceVo.getStartTime().equals(priceVo.getEndTime()) || StringUtils.isBlank(priceVo.getStartTime())
                || StringUtils.isBlank(priceVo.getEndTime())) {
            String time = priceVo.getStartTime();
            if (time.isEmpty()) {
                time = priceVo.getEndTime();
            }

            LambdaQueryWrapper<Price> lambdaQueryWrapper = new LambdaQueryWrapper<Price>()
                    .eq(Price::getLineId, line.getId()).eq(Price::getOutDate, LocalDate.parse(time));
            Price needUpdatePrice = baseMapper.selectOne(lambdaQueryWrapper);
            needUpdatePrice.setAdultPrice(priceVo.getAdultPrice());
            needUpdatePrice.setBabyPrice(priceVo.getBabyPrice());
            needUpdatePrice.setChildPrice(priceVo.getChildPrice());
            needUpdatePrice.setOldPrice(priceVo.getOldPrice());
            needUpdatePrice.setUpdateUserId(user.getId());
            baseMapper.updateById(needUpdatePrice);
        }
        if (!priceVo.getStartTime().equals(priceVo.getEndTime()) && StringUtils.isNotBlank(priceVo.getStartTime())
                && StringUtils.isNotBlank(priceVo.getEndTime())) {
            //截取日期年 月  日
            int startyear = Integer.parseInt(priceVo.getStartTime().substring(0, 4));
            int startmonth = Integer.parseInt(priceVo.getStartTime().substring(5, 7));
            int startday = Integer.parseInt(priceVo.getStartTime().substring(8, 10));
            int endyear = Integer.parseInt(priceVo.getEndTime().substring(0, 4));
            int endmonth = Integer.parseInt(priceVo.getEndTime().substring(5, 7));
            int endday = Integer.parseInt(priceVo.getEndTime().substring(8, 10));
//如果输入的开始时间和结束时间不是同一天的话，更改PriceList中出行时间在这个时间端的价格
            for (Price price : pricesList) {
                if (startyear < endyear) {
                    if (price.getOutDate().getYear() > startyear && price.getOutDate().getYear() < endyear) {
                        updateFourPrice(price, priceVo);
                    }
                    if (price.getOutDate().getYear() == startyear) {
                        if (price.getOutDate().getMonthValue() > startmonth || (price.getOutDate().getMonthValue() == startmonth && price.getOutDate().getDayOfMonth() >= startday)) {
                            updateFourPrice(price, priceVo);
                        }
                    }
                    if (price.getOutDate().getYear() == endyear) {
                        if (price.getOutDate().getMonthValue() < endmonth || (price.getOutDate().getMonthValue() == endmonth &&
                                price.getOutDate().getDayOfMonth() <= endday)) {
                            updateFourPrice(price, priceVo);
                        }
                    }
                }
                if (startyear == endyear) {
                    if (price.getOutDate().getYear() == startyear) {
                        if (startmonth < endmonth) {
                            if (price.getOutDate().getMonthValue() > startmonth && price.getOutDate().getMonthValue() < endmonth) {
                                updateFourPrice(price, priceVo);
                            }
                            if (price.getOutDate().getDayOfMonth() == startmonth) {
                                if (price.getOutDate().getDayOfMonth() >= startday) {
                                    updateFourPrice(price, priceVo);
                                }
                            }
                            if (price.getOutDate().getDayOfMonth() == endmonth) {
                                if (price.getOutDate().getDayOfMonth() <= endday) {
                                    updateFourPrice(price, priceVo);
                                }
                            }
                        }
                        if (startmonth == endmonth) {
                            if (price.getOutDate().getDayOfMonth() <= endday && price.getOutDate().getDayOfMonth() >= startday) {
                                updateFourPrice(price, priceVo);
                            }
                        }
                    }
                }
            }


            int update = baseMapper.batchupdatePrice(pricesList);
            if (update <= 0) {
                log.error("batchUpdate price fail.");
                throw new ApplicationException(CodeType.SERVICE_ERROR, "批量修改价格失败");
            }
        }
    }


    /**
     * 根据出行时间或者线路名查看价格序列
     *
     * @param priceQuery
     * @return
     */
    @Override
    public Page<PriceSelectVo> queryPricePageByFilter(PriceQuery priceQuery) {
        Page<PriceVo> page =new Page<PriceVo>();
        page.setCurrent(priceQuery.getCurrent());
        page.setSize(priceQuery.getSize());
        return baseMapper.selectPriceByFilter( page,priceQuery);

    }



    /**
     * 根据日期和线路名查询价格
     * @param Outdate
     * @param LineName
     * @return
     */
    @Override
    public Price queryPriceByTimeAndLineName(LocalDate Outdate, String LineName) {
        Line line = lineService.queryLineByName(LineName);
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getLineId,line.getId()).eq(Price::getOutDate,Outdate);
        return baseMapper.selectOne(queryWrapper);
    }
}
