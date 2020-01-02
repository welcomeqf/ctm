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
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountInfoQuery;
import eqlee.ctm.apply.orders.entity.query.OrdersNoCountQuery;
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
        if ("运营人员".equals(users.getRoleName())) {
            id = null;
        } else {
            id = users.getId();
        }

        return baseMapper.pageOrder(page,start,end,lineName,region,id);
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


}
