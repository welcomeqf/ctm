package eqlee.ctm.apply.orders.service.impl;

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
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.orders.dao.OrdersMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.OrderSubstitut;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.CarQueryBo;
import eqlee.ctm.apply.orders.entity.bo.OrderDetailedBo;
import eqlee.ctm.apply.orders.entity.query.ChangedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderContectQuery;
import eqlee.ctm.apply.orders.entity.query.OrderQuery;
import eqlee.ctm.apply.orders.entity.query.PriceQuery;
import eqlee.ctm.apply.orders.service.IOrderSubstitutService;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;

import java.time.LocalDate;
import java.util.*;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

   @Autowired
   private IApplyService applyService;
   @Autowired
   private IOrdersDetailedService orderDetailedService;

   @Autowired
   private LocalUser localUser;
   @Autowired
   private IPriceService priceService;

   @Autowired
   private IOrderSubstitutService orderSubstitutService;

   IdGenerator idGenerator = new IdGenerator();


    @Override
    public void saveApply(List<LongVo> indexVoList) {

        List<OrderDetailedBo> orderDetailedList = new ArrayList<>();
        List<Apply> applyList = applyService.selectAllApply();

        UserLoginQuery user = localUser.getUser("用户信息");

        Orders orders = new Orders();
        long numberId = idGenerator.getNumberId();
        orders.setId(numberId);
        orders.setOrderNo(idGenerator.getOrderCode());
        orders.setGuideName(user.getCname());
        orders.setCreateUserId(user.getId());
        Double allPrice = 0.0;

        for (LongVo longVo : indexVoList) {
            OrdersVo ordersVo = baseMapper.selectApplyVoList(longVo.getId());

            //装配订单
            OrderDetailedBo orderDetailed = new OrderDetailedBo();
            orders.setLineName(ordersVo.getLineName());
            String date = ordersVo.getOutDate().substring(0,10);
            orders.setOutDate(DateUtil.parseDate(date));
            orders.setRegion(ordersVo.getRegion());
            for (Apply apply:applyList) {
                if(apply.getLineId().equals(ordersVo.getLineId())
                        && DateUtil.formatDate(apply.getOutDate()).equals(ordersVo.getOutDate())){
                    orders.setAllPrice(allPrice + apply.getAllPrice());

                    orderDetailed.setAccountName(apply.getCompanyUser());
                    orderDetailed.setCompanyName(apply.getCompanyName());
                    orderDetailed.setAdultNumber(apply.getAdultNumber());
                    orderDetailed.setBabyNumber(apply.getBabyNumber());
                    orderDetailed.setChildNumber(apply.getChildNumber());
                    orderDetailed.setOldNumber(apply.getOldNumber());
                    orderDetailed.setAllNumber(apply.getAllNumber());
                    orderDetailed.setPrice(apply.getAllPrice());
                    orderDetailed.setCreateUserId(user.getId());
                    orderDetailed.setPayType(apply.getPayType());

                }

            }
            //订单详情表
            orderDetailed.setOrderId(numberId);
            orderDetailed.setContactName(ordersVo.getContactName());
            orderDetailed.setContactTel(ordersVo.getContactTel());
            orderDetailed.setId(idGenerator.getNumberId());
            orderDetailed.setPlace(ordersVo.getPlace());
            orderDetailedList.add(orderDetailed);

        }
        int insert = baseMapper.insert(orders);

        if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"生成订单失败");
        }

        orderDetailedService.batchInsertorderDetailed(orderDetailedList);

        //修改报名表导游选人状态
        for (LongVo longVo : indexVoList) {
            applyService.updateGuestStatus(longVo.getId());
        }
    }



    /**
     * 查看导游已选人情况
     * @param page
     * @param LineName
     * @param OutDate
     * @return
     */
    @Override
    public Page<OrderIndexVo> ChoisedIndex(Page<OrderIndexVo> page, String LineName, String OutDate) {
        UserLoginQuery user = localUser.getUser("用户信息");
        return baseMapper.selectOrdersByCreateUserId(page,user.getId(),LineName,DateUtil.parseDate(OutDate));
    }



    /**
     *由线路名，出发时间，导游姓名得到车牌号
     * @return
     */
    @Override
    public String getCarNumber(String LineName, String OutDate) {
        LocalDate localDate = DateUtil.parseDate(OutDate);
        UserLoginQuery user = localUser.getUser("用户信息");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getLineName,LineName).eq(Orders::getCreateUserId,user.getId())
                .eq(Orders::getOutDate,localDate);
        List<Orders> ordersList = baseMapper.selectList(queryWrapper);
        String carNumber = "";
        for (Orders orders:ordersList) {
          carNumber = orders.getCarNumber();
          break;
        }
        return carNumber;
    }



    /**
     * 导游换人
     * @param orderIndexVos
     * @param Id
     */
    @Override
    public void updateApply(List<OrderIndexVo> orderIndexVos,Long Id,String lineName, String outDate) {
        //生成换人表记录
        UserLoginQuery user = localUser.getUser("用户信息");
        OrderSubstitut substitut = new OrderSubstitut();
        Long id = idGenerator.getNumberId();
        substitut.setId(id);
        substitut.setCreateUserId(user.getId());
        substitut.setGuideName(user.getCname());
        substitut.setLineName(lineName);
        substitut.setOutDate(DateUtil.parseDate(outDate));
        substitut.setToGuideId(Id);
        substitut.setUpdateUserId(user.getId());
        orderSubstitutService.addOrderSubstitut(substitut);

        //修改订单详情表
       int i = baseMapper.updateOrderDetailed(orderIndexVos,id);
       if (i<= 0){
           throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
       }
    }

    @Override
    public void save(String LineName, String OutDate, String CarNumber) {

        UserLoginQuery userLoginQuery = localUser.getUser("用户信息");
        //判断该线路日期是否配了车
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getLineName,LineName)
                .eq(Orders::getOutDate,OutDate)
                .eq(Orders::getCreateUserId,userLoginQuery.getId());
        Orders orders = baseMapper.selectOne(wrapper);

        if (StringUtils.isNotBlank(orders.getCarNumber())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您已配过车了");
        }

        //判断该车辆是否已经出行
        CarQueryBo bo = baseMapper.queryCar(CarNumber);

        if(bo == null) {
            int update = baseMapper.updateOrdersOutsideCarNo(LineName,DateUtil.parseDate(OutDate),CarNumber,userLoginQuery.getId());
            if(update == 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
            }
        }

        if (bo.getStatus() == 1) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该车辆已经被选了");
        }

        if (bo.getStatus() == 2) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该车辆正在维修");
        }

        //本公司车辆
        int update = baseMapper.updateOrdersCarNo(LineName,DateUtil.parseDate(OutDate),CarNumber,userLoginQuery.getId());

        if(update == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
        }

        //修改出行状态
        updateCarStatus (CarNumber);


    }

    /**
     * 修改公司状态
     * @param carNo
     */
    @Override
    public void updateCarStatus(String carNo) {
        int status1 = baseMapper.updateCarStatus(carNo);

        if (status1 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }


    /**
     * 导游换人消息展示
     * @return
     */
    @Override
    public Page<ChangedVo> waiteChangeIndex(Page<ChangedVo> page) {

        UserLoginQuery user = localUser.getUser("用户信息");
        return baseMapper.waiteChangeIndex(page,user.getId());
}



    /**
     * 接受换人
     * @param choisedList
     */
    @Override
    public void sureChoised(List<ChoisedVo> choisedList, String outDate, String lineName) {
        UserLoginQuery user = localUser.getUser("用户信息");

        //修改换人表数据
        orderSubstitutService.apotSubstitution(DateUtil.parseDate(outDate),lineName,user.getId(),1);

        int update = baseMapper.sureChoised(choisedList);
        if(update == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }

        int i = baseMapper.sureOrders(user.getId(), lineName, DateUtil.parseDate(outDate));

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }
    }


    /**
     * 拒绝换人
     * @param choisedList
     */
    @Override
    public void denyChoised(List<ChoisedVo> choisedList, String outDate, String lineName) {
        UserLoginQuery user = localUser.getUser("用户信息");
        //修改换人表数据
        orderSubstitutService.apotSubstitution(DateUtil.parseDate(outDate),lineName,user.getId(),2);

        int insert = baseMapper.denyChoised(choisedList);
        if(insert == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"插入失败");
        }
    }


    /**
     * 列表查询
     * @return
     */
    @Override
    public Page<ChangedQuery> denyChoisedindex(Page<ChangedQuery> page, Integer type) {
        UserLoginQuery user = localUser.getUser("用户信息");

        if (type == null || type == 1) {
            //我推给别人的
            return baseMapper.denyChoisedindex(page,user.getId());
        }

        //别人推给我的
        return baseMapper.withChoiseIndex(page,user.getId());
    }



    /**
     * 导游收入统计
     * @param LineName
     * @param OutDate
     * @return
     */
    @Override
    public IncomeVo IncomeCount(String LineName, String OutDate) {
        UserLoginQuery user = localUser.getUser("用户信息");
        List<InComeRemerberVo> incomeRemerberVoList = baseMapper.selectIncomeCount(LineName,DateUtil.parseDate(OutDate),user.getId());

        if (incomeRemerberVoList.size() == 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"日期或线路错误");
        }

        Price price = priceService.queryPrice(DateUtil.parseDate(OutDate),LineName);

        if (price == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路该天的价格已被删除");
        }

        IncomeVo incomeVo = new IncomeVo();
        incomeVo.setComeedCount(0);
        incomeVo.setDoNumber(0);
        incomeVo.setAdultNumber(0);
        incomeVo.setChildNumber(0);
        incomeVo.setBabyNumber(0);
        incomeVo.setOldNumber(0);
        incomeVo.setUnpaid(0);
        incomeVo.setSumInCome(0.0);
        incomeVo.setCarType(true);
        Double sum = 0.0;
        Map<String,Object> map = new HashMap<>();
        for (InComeRemerberVo item:incomeRemerberVoList) {
           incomeVo.setComeedCount(incomeVo.getComeedCount() + item.getAllNumber());
           incomeVo.setDoNumber(incomeVo.getDoNumber() + item.getAllNumber());
           incomeVo.setAdultNumber(incomeVo.getAdultNumber()+item.getAdultNumber());
           incomeVo.setBabyNumber(incomeVo.getBabyNumber()+item.getBabyNumber());
           incomeVo.setOldNumber(incomeVo.getOldNumber() + item.getOldNumber());
           incomeVo.setChildNumber(incomeVo.getChildNumber() + item.getChildNumber());
           incomeVo.setCarNumber(item.getCarNumber());
           incomeVo.setCarType(item.getCarType());

        }

        List<OrderContectQuery> queries = baseMapper.listContect(LineName, DateUtil.parseDate(OutDate), user.getId());

        //查询面收的人员名单
        List<Map<String,Object>> list = new ArrayList<>();
        for (OrderContectQuery query : queries) {
            //算出总价
            Double AllPrice = query.getAdultNumber() * price.getAdultPrice() + query.getBabyNumber() * price.getBabyPrice()
                    + query.getChildNumber() + price.getChildPrice() + query.getOldNumber() * price.getOldPrice();
            map.put("allPrice",AllPrice);
            map.put("Id",query.getId());
            map.put("AdultNumber",query.getAdultNumber());
            map.put("BabyNumber",query.getBabyNumber());
            map.put("OldNumber",query.getOldNumber());
            map.put("ChildNumber",query.getChildNumber());
            map.put("ContactName",query.getContactName());
            map.put("AllNumber",query.getAllNumber());
            map.put("ContactTel",query.getContactTel());
            list.add(map);

            sum = AllPrice;
        }

        incomeVo.setContactNames(list);
        incomeVo.setSumInCome(sum);

        return incomeVo;
    }


    /**
     * 未付款人信息
     * @return
     */
    @Override
    public Page<UnpaidInformationVo> unpaidInformation(Page<UnpaidInformationVo> page) {
        return baseMapper.unpaidInformation(page);
    }

    /**
     * 查询日期和线路
     * @return
     */
    @Override
    public List<OrderQuery> queryLineAndTime() {
        UserLoginQuery user = localUser.getUser("用户信息");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getCreateUserId,user.getId())
                .eq(Orders::getIsFinash,false)
                .orderByDesc(Orders::getOutDate);

        List<Orders> orders = baseMapper.selectList(queryWrapper);
        List<OrderQuery> result = new ArrayList<>();

        for (Orders order : orders) {
            OrderQuery query = new OrderQuery();
            query.setLineName(order.getLineName());
            if (order.getOutDate() != null) {
                query.setOutDate(DateUtil.formatDate(order.getOutDate()));
            }
            result.add(query);
        }

        return result;
    }


}
