package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.orders.dao.OrdersMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.OrdersVo;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

   @Autowired
   private IApplyService applyService;
   @Autowired
   private IOrdersDetailedService orderDetailedService;

   IdGenerator idGenerator = new IdGenerator();

    @Override
    public void saveApply(List<OrdersVo> applyVoList) {
        List<OrderDetailed> orderDetailedList = new ArrayList<OrderDetailed>();
        List<Orders> ordersList = new ArrayList<Orders>();
        List<Apply> applyList = applyService.selectAllApply();
        for (OrdersVo ordersVo:applyVoList) {
            Orders orders = new Orders();
            OrderDetailed orderDetailed = new OrderDetailed();
            long numberId = idGenerator.getNumberId();
            orders.setId(numberId);
            orders.setLineName(ordersVo.getLineName());
            orders.setOutDate(LocalDate.parse(ordersVo.getOutDate()));
            //TODO 导游姓名
            orders.setRegion(ordersVo.getRegion());
            for (Apply apply:applyList) {
                if(apply.getContactTel().equals(ordersVo.getContactTel()) && String.valueOf(apply.getLineId()).equals(String.valueOf(ordersVo.getLineId()))
                        && DateUtil.formatDate(apply.getOutDate()).equals(ordersVo.getOutDate())){
                    log.info("io");
                    orders.setAccountName(apply.getCompanyUser());
                    orders.setOrderNo(apply.getApplyNo());
                    orders.setCompanyName(apply.getCompanyName());
                    orders.setAllPrice(apply.getAllPrice());
                    orderDetailed.setAdultNumber(apply.getAdultNumber());
                    orderDetailed.setBabyNumber(apply.getBabyNumber());
                    orderDetailed.setChildNumber(apply.getChildNumber());
                    orderDetailed.setOldNumber(apply.getOldNumber());
                    orderDetailed.setAllNumber(apply.getAllNumber());
                    orderDetailed.setPrice(apply.getAllPrice());
                }
            }
            ordersList.add(orders);

            //订单详情表
                    orderDetailed.setOrderId(numberId);
                    orderDetailed.setContactName(ordersVo.getContactName());
                    orderDetailed.setContactTel(ordersVo.getContactTel());
                    orderDetailed.setId(idGenerator.getNumberId());
                    orderDetailed.setPlace(ordersVo.getPlace());
                    orderDetailedList.add(orderDetailed);
        }
        baseMapper.batchInsertOrders(ordersList);
        orderDetailedService.batchInsertorderDetailed(orderDetailedList);
    }
}
