package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.orders.dao.OrderDetailedMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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


    @Override
    public void batchInsertorderDetailed(List<OrderDetailedBo> orderDetailedList) {
        baseMapper.batchInsertorderDetailed(orderDetailedList);
    }

    /**
     * 查询导游人员表
     * @param page
     * @param payType
     * @return
     */
    @Override
    public Page<OrderDetailedQuery> pageOrderDetailed2Type(Page<OrderDetailedQuery> page, String payType, String lineName, String outDate) {

        UserLoginQuery users = user.getUser("用户信息");
        Long id = users.getId();
        LocalDate localDate = DateUtil.parseDate(outDate);

        if (StringUtils.isBlank(payType)) {
            //查询所有
            return baseMapper.pageOrderDetailed(page,lineName,localDate,id);
        }

        if (!MONTH_PAY.equals(payType) && !NOW_PAY.equals(payType) && !AGENT_PAY.equals(payType)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"您输入的结算方式有误");
        }

        int type = 0;
        if (MONTH_PAY.equals(payType)) {
            type = 1;
        }

        if (NOW_PAY.equals(payType)) {
            type = 0;
        }

        if (AGENT_PAY.equals(payType)) {
            type = 2;
        }

        return baseMapper.pageOrderDetailedByType(page,lineName,localDate,type,id);
    }

}
