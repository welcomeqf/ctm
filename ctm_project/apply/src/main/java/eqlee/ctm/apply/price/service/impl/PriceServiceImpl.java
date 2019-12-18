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
import com.yq.utils.WeekUtils;
import eqlee.ctm.apply.channle.entity.Channel;
import eqlee.ctm.apply.channle.service.IChannelService;
import eqlee.ctm.apply.entry.entity.bo.PriceBo;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.dao.PriceMapper;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.query.PriceNumberQuery;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.*;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    public Price queryPrice(LocalDate OutDate,String LineName) {

        Line line = lineService.queryLineByName(LineName);

        if (line == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"网络忙，请稍后再试");
        }

        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getOutDate, OutDate)
                .eq(Price::getLineId,line.getId());
        return baseMapper.selectOne(queryWrapper);

    }

    /**
     * 价格设定与修改(没有就增加  有就修改)
     *
     * @param priceVo
     */
    @Override
    public void insertPrice(PriceVo priceVo) {
        //根据线路名查询该线路的Id插入数据库
        Line line = lineService.queryLineByName(priceVo.getLineName());

        LocalDate startTime = DateUtil.parseDate(priceVo.getStartTime());
        LocalDate endTime = DateUtil.parseDate(priceVo.getEndTime());
        LocalDate now = LocalDate.now();

        long until = now.until(startTime, ChronoUnit.DAYS);
        long until1 = now.until(endTime, ChronoUnit.DAYS);

        if (until < 0 || until1 < 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请设定今天或今天之后的价格");
        }

        Channel channel = channelService.selectChannelByType("代理");
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");
        IdGenerator idGenerator = new IdGenerator();

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
        List<PriceAllUpdateVo> list = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            PriceAllUpdateVo price = new PriceAllUpdateVo();
            price.setAdultPrice(priceVo.getAdultPrice());
            price.setBabyPrice(priceVo.getBabyPrice());
            price.setChildPrice(priceVo.getChildPrice());
            price.setOldPrice(priceVo.getOldPrice());
            price.setMarketAdultPrice(priceVo.getMarketAdultPrice());
            price.setMarketOldPrice(priceVo.getMarketOldPrice());
            price.setMarketChildPrice(priceVo.getMarketChildPrice());
            price.setMarketBabyPrice(priceVo.getMarketBabyPrice());
            price.setLineId(line.getId());

            //设置每一天的日期
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            c.add(Calendar.DAY_OF_MONTH, i);

            String format = sf.format(c.getTime());
            LocalDate date = DateUtil.parseDate(format);

            LambdaQueryWrapper<Price> wrapper = new LambdaQueryWrapper<Price>()
                  .eq(Price::getLineId,line.getId())
                  .eq(Price::getOutDate,date);
            Price one = baseMapper.selectOne(wrapper);

            if (one == null) {
                //增加
                Price price1 = new Price();
                price1.setId(idGenerator.getNumberId());
                price1.setAdultPrice(priceVo.getAdultPrice());
                price1.setMarketAdultPrice(priceVo.getMarketAdultPrice());
                price1.setMarketBabyPrice(priceVo.getMarketBabyPrice());
                price1.setMarketChildPrice(priceVo.getMarketChildPrice());
                price1.setMarketOldPrice(priceVo.getMarketOldPrice());
                price1.setBabyPrice(priceVo.getBabyPrice());
                price1.setChildPrice(priceVo.getChildPrice());
                price1.setRemark(priceVo.getRemark());
                price1.setOldPrice(priceVo.getOldPrice());
                price1.setOutDate(date);
                price1.setLineId(line.getId());
                price1.setCreateUserId(user.getId());
                price1.setUpdateUserId(user.getId());
                // 渠道ID
                price1.setChannelId(channel.getId());
                baseMapper.insert(price1);
            }
            price.setOutDate(date);
            list.add(price);
        }

        Integer result = baseMapper.batchUpdatePrice(list);

        if (result <= 0) {
            log.error("all insert price fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改价格失败");
        }

    }

    /**
     * 价格修改
     *
     * @param price1
     */
    @Override
    public void updatePrice(PriceUpdateVo price1) {

        if (price1.getId() == null) {
            //增加

            Channel channel = channelService.selectChannelByType("代理");
            //获取用户信息
            UserLoginQuery user = localUser.getUser("用户信息");

            IdGenerator idGenerator = new IdGenerator();
            Price price = new Price();
            price.setId(idGenerator.getNumberId());
            price.setChildPrice(price1.getChildPrice());
            price.setBabyPrice(price1.getBabyPrice());
            price.setAdultPrice(price1.getAdultPrice());
            price.setOldPrice(price1.getOldPrice());
            price.setMarketAdultPrice(price1.getMarketAdultPrice());
            price.setMarketBabyPrice(price1.getMarketBabyPrice());
            price.setMarketChildPrice(price1.getMarketChildPrice());
            price.setMarketOldPrice(price1.getMarketOldPrice());
            price.setOutDate(DateUtil.parseDate(price1.getOutDate()));
            price.setLineId(price1.getLineId());
            price.setCreateUserId(user.getId());
            price.setUpdateUserId(user.getId());
            price.setRemark(price1.getRemark());
            // 渠道ID
            price.setChannelId(channel.getId());

            int insert = baseMapper.insert(price);

            if (insert <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
            }

            return;
        }


        Price price = new Price();
        price.setId(price1.getId());
        price.setChildPrice(price1.getChildPrice());
        price.setBabyPrice(price1.getBabyPrice());
        price.setAdultPrice(price1.getAdultPrice());
        price.setOldPrice(price1.getOldPrice());
        price.setMarketAdultPrice(price1.getMarketAdultPrice());
        price.setMarketBabyPrice(price1.getMarketBabyPrice());
        price.setMarketChildPrice(price1.getMarketChildPrice());
        price.setMarketOldPrice(price1.getMarketOldPrice());
        price.setRemark(price1.getRemark());

        int result = baseMapper.updateById(price);

        if (result <= 0) {
            log.error("update price fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改价格失败");
        }
    }


    /**
     * 批量修改   没有就增加
     * 按一个时间段的星期来操作
     * @param priceVo
     */
    @Override
    public void batchUpdatePrice(PriceBo priceVo) {
        //根据线路名查询该线路的Id插入数据库
        Line line = lineService.queryLineByName(priceVo.getLineName());

        LocalDate startTime = DateUtil.parseDate(priceVo.getStartTime());
        LocalDate endTime = DateUtil.parseDate(priceVo.getEndTime());
        LocalDate now = LocalDate.now();

        long until = now.until(startTime, ChronoUnit.DAYS);
        long until1 = now.until(endTime, ChronoUnit.DAYS);

        if (until < 0 || until1 < 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请设定今天或今天之后的价格");
        }

        Channel channel = channelService.selectChannelByType("代理");
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        IdGenerator idGenerator = new IdGenerator();

        //得到需要操作的日期
        List<String> list = WeekUtils.getDates(priceVo.getStartTime(), priceVo.getEndTime(), priceVo.getWeekList());

        List<PriceAllUpdateVo> result = new ArrayList<>();
        for (String date : list) {

            LocalDate localDate = DateUtil.parseDate(date);

            LambdaQueryWrapper<Price> wrapper = new LambdaQueryWrapper<Price>()
                  .eq(Price::getLineId,line.getId())
                  .eq(Price::getOutDate,localDate);
            Price one = baseMapper.selectOne(wrapper);

            if (one == null) {
                //增加
                Price price1 = new Price();
                price1.setId(idGenerator.getNumberId());
                price1.setAdultPrice(priceVo.getAdultPrice());
                price1.setMarketAdultPrice(priceVo.getMarketAdultPrice());
                price1.setMarketBabyPrice(priceVo.getMarketBabyPrice());
                price1.setMarketChildPrice(priceVo.getMarketChildPrice());
                price1.setMarketOldPrice(priceVo.getMarketOldPrice());
                price1.setBabyPrice(priceVo.getBabyPrice());
                price1.setChildPrice(priceVo.getChildPrice());
                price1.setRemark(priceVo.getRemark());
                price1.setOldPrice(priceVo.getOldPrice());
                price1.setOutDate(localDate);
                price1.setLineId(line.getId());
                price1.setCreateUserId(user.getId());
                price1.setUpdateUserId(user.getId());
                // 渠道ID
                price1.setChannelId(channel.getId());
                baseMapper.insert(price1);
            }

            //有就修改
            PriceAllUpdateVo price = new PriceAllUpdateVo();
            price.setAdultPrice(priceVo.getAdultPrice());
            price.setBabyPrice(priceVo.getBabyPrice());
            price.setChildPrice(priceVo.getChildPrice());
            price.setOldPrice(priceVo.getOldPrice());
            price.setMarketAdultPrice(priceVo.getMarketAdultPrice());
            price.setMarketOldPrice(priceVo.getMarketOldPrice());
            price.setMarketChildPrice(priceVo.getMarketChildPrice());
            price.setMarketBabyPrice(priceVo.getMarketBabyPrice());
            price.setLineId(line.getId());
            price.setOutDate(localDate);
            price.setRemark(priceVo.getRemark());
            result.add(price);
        }

        Integer integer = baseMapper.batchUpdatePrice(result);

        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"批量修改价格失败");
        }

    }


    /**
     * 根据出行时间或者线路名查看价格序列
     * @param numberType
     * @param outDate
     * @param lineId
     * @return
     */
    @Override
    public Map<String,Object> queryPricePageByFilter(Integer numberType, String outDate, Long lineId) {
        //起始日期
        LocalDate start = DateUtil.parseDate(outDate);
        //获取下个月的日期
        LocalDate localDate1 = start.plusMonths(1);
        //获取当前月的最后一天
        LocalDate end = localDate1.minusDays(1);

        List<PriceSelectVo> pricePage = baseMapper.selectPriceByFilter(start, end, lineId);

        Map<String,Object> map = new HashMap<>();
        map.put("startDate",outDate);
        String endDate = DateUtil.formatDate(end);
        map.put("endDate",endDate);

        List<PriceNumberQuery> queryList = new ArrayList<>();
        if (numberType == 1) {
            //老人
            for (PriceSelectVo vo : pricePage) {
                PriceNumberQuery query = new PriceNumberQuery();
                query.setId(vo.getId());
                query.setOutDate(vo.getOutDate());
                query.setPrice(vo.getOldPrice());
                query.setMarketPrice(vo.getMarketOldPrice());
                queryList.add(query);
            }

            map.put("price",queryList);
            return map;
        }

        if (numberType == 2) {
            //小孩
            for (PriceSelectVo vo : pricePage) {
                PriceNumberQuery query = new PriceNumberQuery();
                query.setId(vo.getId());
                query.setOutDate(vo.getOutDate());
                query.setPrice(vo.getChildPrice());
                query.setMarketPrice(vo.getMarketChildPrice());
                queryList.add(query);
            }

            map.put("price",queryList);
            return map;

        }

        if (numberType == 3) {
            //幼儿
            for (PriceSelectVo vo : pricePage) {
                PriceNumberQuery query = new PriceNumberQuery();
                query.setId(vo.getId());
                query.setOutDate(vo.getOutDate());
                query.setPrice(vo.getBabyPrice());
                query.setMarketPrice(vo.getMarketBabyPrice());
                queryList.add(query);
            }

            map.put("price",queryList);
            return map;
        }

        //成人
        for (PriceSelectVo vo : pricePage) {
            PriceNumberQuery query = new PriceNumberQuery();
            query.setId(vo.getId());
            query.setOutDate(vo.getOutDate());
            query.setPrice(vo.getAdultPrice());
            query.setMarketPrice(vo.getMarketAdultPrice());
            queryList.add(query);
        }

        map.put("price",queryList);
        return map;

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

        if (line == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路错误");
        }

        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getLineId,line.getId()).eq(Price::getOutDate,Outdate);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    @Override
    public Price queryPriceById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deletePriceById(Long id) {
        int i = baseMapper.deleteById(id);

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
        }
    }

    /**
     * 跟剧线路Id查询价格
     * @param lineId
     * @return
     */
    @Override
    public List<Price> queryPriceByLineNameId(Long lineId) {
        LambdaQueryWrapper<Price> wrapper = new LambdaQueryWrapper<Price>()
              .eq(Price::getLineId,lineId);
        return baseMapper.selectList(wrapper);
    }
}
