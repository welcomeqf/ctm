package eqlee.ctm.apply.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.dao.PriceMapper;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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


    /**
     * 根据出行日期查询价格
     * @param OutDate
     * @return
     */
    @Override
    public Price queryPrice(LocalDateTime OutDate) {
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<Price>()
                .eq(Price::getOutDate,OutDate);
        return baseMapper.selectOne(queryWrapper);

    }

    /**
     * 价格设定
     * @param priceVo
     */
    @Override
    public void insertPrice(PriceVo priceVo) {
        //根据线路名查询该线路的Id插入数据库
        Line line = lineService.queryLineByName(priceVo.getLineName());
        IdGenerator idGenerator = new IdGenerator();
        //获取总价格
        Double AllPrice = priceVo.getAdultPrice() + priceVo.getBabyPrice() + priceVo.getChildPrice() + priceVo.getOldPrice();
        //如果输入的开始时间和结束时间是同一天的话  就只设定该天一天的价格
        if (priceVo.getStartTime().equals(priceVo.getEndTime())) {
            LocalDate outDate = DateUtil.parseDate(priceVo.getStartTime());
            Price price = new Price();
            price.setId(idGenerator.getNumberId());
            price.setAllPrice(AllPrice);
            price.setAdultPrice(priceVo.getAdultPrice());
            price.setBabyPrice(priceVo.getBabyPrice());
            price.setChildPrice(priceVo.getChildPrice());
            price.setMaxNumber(priceVo.getMaxNumber());
            price.setMinNumber(priceVo.getMinNumber());
            price.setOutDate(outDate);
            price.setLineId(line.getId());
            // 渠道ID
        }
        //输入的开始时间和结束时间不是同一天，就默认批量设置相同的价格


     }

    /**
     * 价格修改
     * @param price
     */
    @Override
    public void updatePrice(Price price) {

    }
}
