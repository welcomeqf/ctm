package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.apply.entry.entity.query.ApplyNoReadCountQuery;
import eqlee.ctm.apply.orders.dao.OrderSubstitutMapper;
import eqlee.ctm.apply.orders.entity.OrderSubstitut;
import eqlee.ctm.apply.orders.service.IOrderSubstitutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderSubstitutServiceImpl extends ServiceImpl<OrderSubstitutMapper, OrderSubstitut> implements IOrderSubstitutService {


    /**
     * 添加换人记录
     * @param orderSubstitut
     */
    @Override
    public void addOrderSubstitut(OrderSubstitut orderSubstitut) {

        int insert = baseMapper.insert(orderSubstitut);

        if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加换人记录失败");
        }
    }

    /**
     * 同意换人
     * @param outDate
     * @param lineName
     * @param id
     */
    @Override
    public void apotSubstitution(LocalDate outDate, String lineName, Long id, Integer type) {

        LambdaQueryWrapper<OrderSubstitut> queryWrapper = new LambdaQueryWrapper<OrderSubstitut>()
                .eq(OrderSubstitut::getToGuideId,id)
                .eq(OrderSubstitut::getOutDate,outDate);

        LocalDateTime now = LocalDateTime.now();
        OrderSubstitut substitut = new OrderSubstitut();
        substitut.setStatus(type);
        substitut.setUpdateDate(now);
        substitut.setUpdateUserId(id);
        int update = baseMapper.update(substitut, queryWrapper);

        if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
        }

    }

    /**
     * 查询导游未审核的换人记录条数
     * @return
     */
    @Override
    public ApplyNoReadCountQuery queryGuideNoExaCount() {

        LambdaQueryWrapper<OrderSubstitut> wrapper = new LambdaQueryWrapper<OrderSubstitut>()
              .eq(OrderSubstitut::getStatus,0);

        Integer integer = baseMapper.selectCount(wrapper);

        ApplyNoReadCountQuery query = new ApplyNoReadCountQuery();
        query.setCount(integer);

        return query;
    }
}
