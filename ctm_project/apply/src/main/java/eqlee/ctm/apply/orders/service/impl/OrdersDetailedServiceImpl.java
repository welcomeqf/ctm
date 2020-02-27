package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.guider.entity.vo.GuiderIdParamVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.dao.OrderDetailedMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.bo.CancelBo;
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderFinanceQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountInfoQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrdersDetailedServiceImpl extends ServiceImpl<OrderDetailedMapper, OrderDetailed> implements IOrdersDetailedService {


    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String AGENT_PAY = "面收";

    @Autowired
    private LocalUser user;

    @Autowired
    private IOrdersService ordersService;

    @Override
    public void batchInsertorderDetailed(List<OrderDetailedBo> orderDetailedList) {
        baseMapper.batchInsertorderDetailed(orderDetailedList);
    }

    /**
     * 查询导游人员表
     * @param id
     * @param payType
     * @return
     */
    @Override
    public List<OrderDetailedQuery> pageOrderDetailed2Type(Long id, Integer payType) {
            //查询所有
            return baseMapper.pageOrderDetailed(payType,id);

    }

    /**
     * 管理分配首页
     * @param page
     * @param startDate
     * @param endDate
     * @param lineName
     * @param region
     * @return
     */
    @Override
    public Page<OrderBo> pageOrder(Page<OrderBo> page,String startDate, String endDate, String lineName, String region) {
        UserLoginQuery users = user.getUser("用户信息");

        LocalDate start = null;
        LocalDate end = null;

        if (StringUtils.isNotBlank(startDate)) {
            start = DateUtil.parseDate(startDate);
            end = DateUtil.parseDate(endDate);
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        if (StringUtils.isBlank(region)) {
            region = null;
        }

        Long id = null;
        if ("运营人员".equals(users.getRoleName()) || "超级管理员".equals(users.getRoleName()) || "管理员".equals(users.getRoleName())) {
            id = null;
        } else {
            id = users.getId();
        }

        return baseMapper.pageOrder(page,start,end,lineName,region,id);
    }

    /**
     * 交账结果
     * @param page
     * @param startDate
     * @param endDate
     * @param lineName
     * @param guideName
     * @return
     */
    @Override
    public Page<OrderBo> pageOrder2(Page<OrderBo> page, String startDate, String endDate, String lineName, String guideName, Integer inStatus) {
        UserLoginQuery users = user.getUser("用户信息");

        LocalDate start = null;
        LocalDate end = null;

        if (StringUtils.isNotBlank(startDate)) {
            start = DateUtil.parseDate(startDate);
            end = DateUtil.parseDate(endDate);
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        if (StringUtils.isBlank(guideName)) {
            guideName = null;
        }

        Long id = null;
        if ("运营人员".equals(users.getRoleName()) || "超级管理员".equals(users.getRoleName()) || "管理员".equals(users.getRoleName())) {
            id = null;
        } else {
            id = users.getId();
        }

        return baseMapper.pageOrder2(page,start,end,lineName,guideName,id,inStatus);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public OrderDetailed queryById(Long id) {
        return baseMapper.selectById(id);
    }


    /**
     * 查询支出信息  c
     * 财务信息
     * @param orderId
     * @return
     */
    @Override
    public  List<OrderFinanceQuery>  queryInOutInfo(Long orderId) {
        return baseMapper.queryInOutInfo (orderId);
    }


    /**
     * 提交取消操作
     * @param applyId
     * @param cancelStatus
     */
    @Override
    public void updateCancelStatus(Long applyId, Integer cancelStatus) {

        LambdaQueryWrapper<OrderDetailed> wrapper = new LambdaQueryWrapper<OrderDetailed>()
              .eq(OrderDetailed::getApplyId,applyId);

        //修改订单主表的人数  金额信息
        OrderDetailed orderDetailed = baseMapper.selectOne(wrapper);

        if (orderDetailed == null) {
            return;
        }

        if (cancelStatus == 1) {
            //如果已经出行的单不能取消
            Orders orders = ordersService.queryOne(orderDetailed.getOrderId());
            LocalDate now = LocalDate.now();
            long until = now.until(orders.getOutDate(), ChronoUnit.DAYS);

            if (until < 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"亲~该单已经出行不能取消！");
            }
        }

        //判断是提交取消审核  还是同意审核  还是拒绝审核
        OrderDetailed detailed = new OrderDetailed();
        if (cancelStatus == 1 || cancelStatus == 0) {
            //提交取消审核   取消中
            //0  表示拒绝审核
            detailed.setCancelStatus(cancelStatus);
            int update = baseMapper.update(detailed, wrapper);

            if (update <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "提交操作出错");
            }
        }

        if (cancelStatus == 2) {

            //同意审核
            detailed.setCancelStatus(cancelStatus);
            detailed.setAdultNumber(0);
            detailed.setAllNumber(0);
            detailed.setBabyNumber(0);
            detailed.setChildNumber(0);
            detailed.setMonthPrice(0.0);
            detailed.setMsPrice(0.0);
            detailed.setOldNumber(0);
            detailed.setPrice(0.0);

            int update = baseMapper.update(detailed, wrapper);

            if (update <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "提交操作出错");
            }

            Orders orders = ordersService.queryOne(orderDetailed.getOrderId());

            CancelBo bo = new CancelBo();

            Double price = orders.getAllPrice() - orderDetailed.getPrice();
            bo.setOrderId(orderDetailed.getOrderId());
            bo.setCancelPrice(price);

            ordersService.updateOrderCancel(bo);
        }
    }


}
