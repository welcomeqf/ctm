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
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.orders.dao.OrdersMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

   IdGenerator idGenerator = new IdGenerator();


    @Override
    public void saveApply(List<IdVo> IndexVoList) {
        List<OrderDetailed> orderDetailedList = new ArrayList<OrderDetailed>();
        List<Orders> ordersList = new ArrayList<Orders>();
        List<Apply> applyList = applyService.selectAllApply();
        List<OrdersVo> applyVoList = baseMapper.selectApplyVoList(IndexVoList);
        for (OrdersVo ordersVo:applyVoList) {
            Orders orders = new Orders();
            OrderDetailed orderDetailed = new OrderDetailed();
            long numberId = idGenerator.getNumberId();
            orders.setId(numberId);
            orders.setLineName(ordersVo.getLineName());
            orders.setOutDate(LocalDate.parse(ordersVo.getOutDate()));
            UserLoginQuery user = localUser.getUser("用户信息");
            orders.setRegion(ordersVo.getRegion());
            for (Apply apply:applyList) {
                if(apply.getContactTel().equals(ordersVo.getContactTel()) && String.valueOf(apply.getLineId()).equals(String.valueOf(ordersVo.getLineId()))
                        && DateUtil.formatDate(apply.getOutDate()).equals(ordersVo.getOutDate())){
                    orders.setAccountName(apply.getCompanyUser());
                    orders.setOrderNo(apply.getApplyNo());
                    orders.setCompanyName(apply.getCompanyName());
                    orders.setAllPrice(apply.getAllPrice());
                    orders.setGuideName(user.getCName());
                    orders.setCreateUserId(user.getId());
                    orderDetailed.setAdultNumber(apply.getAdultNumber());
                    orderDetailed.setBabyNumber(apply.getBabyNumber());
                    orderDetailed.setChildNumber(apply.getChildNumber());
                    orderDetailed.setOldNumber(apply.getOldNumber());
                    orderDetailed.setAllNumber(apply.getAllNumber());
                    orderDetailed.setPrice(apply.getAllPrice());
                    orderDetailed.setCreateUserId(user.getId());
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
        UserLoginQuery user = localUser.getUser("用户信息");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getLineName,LineName).eq(Orders::getCreateUserId,user.getId())
                .eq(Orders::getOutDate,OutDate);
        List<Orders> ordersList = baseMapper.selectList(queryWrapper);
        String carNumber = "";
        for (Orders orders:ordersList) {
          carNumber = orders.getCarNumber();
          break;
        }
        return carNumber;
    }



    /**
     * 更换导游
     * @param orderIndexVos
     * @param Id
     */
    @Override
    public void updateApply(List<OrderIndexVo> orderIndexVos,Long Id) {
       int i = baseMapper.updateOrderDetailed(orderIndexVos,String.valueOf(Id));
       if (i<= 0){
           throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
       }
    }

    @Override
    public synchronized void save(String LineName, String OutDate, String CarNumber) {
        UserLoginQuery userLoginQuery = localUser.getUser("用户信息");
        CarVo car = baseMapper.isCompanyCar(CarNumber);
        int update =0;
        if(car == null) {
            Long id = idGenerator.getNumberId();
            baseMapper.insertCar(id,CarNumber,userLoginQuery.getId());
            update = baseMapper.updateOrdersOutsideCarNo(LineName,DateUtil.parseDate(OutDate),CarNumber,userLoginQuery.getId());
        }else
            update = baseMapper.updateOrdersCarNo(LineName,DateUtil.parseDate(OutDate),CarNumber,userLoginQuery.getId());
        if(update == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
        }

    }



    /**
     * 展示导游已选游客
     * @param page
     * @param OutDate
     * @param ContactName
     * @param LineName
     * @return
     */
    @Override
    public Page<VisitorInformation> choisedVisitor(Page<VisitorInformation> page, String OutDate, String ContactName, String LineName) {


           return baseMapper.selectVisitor(page,DateUtil.parseDate(OutDate), ContactName, LineName);

    }



    /**
     * 导游换人消息展示
     * @return
     */
    @Override
    public List<ChangedVo> waiteChangeIndex() {

        UserLoginQuery user = localUser.getUser("用户信息");
        return baseMapper.waiteChangeIndex(String.valueOf(user.getId()));
    }



    /**
     * 接受换人
     * @param choisedList
     */
    @Override
    public void sureChoised(List<ChoisedVo> choisedList) {
        UserLoginQuery user = localUser.getUser("用户信息");
        log.info(String.valueOf(user.getId()));
        for (ChoisedVo choisedVo:choisedList) {
            choisedVo.setUpdatedId(String.valueOf(user.getId()));
        }
        int update = baseMapper.sureChoised(choisedList);
        if(update == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }
    }


    /**
     * 拒绝换人
     * @param choisedList
     */
    @Override
    public void denyChoised(List<ChoisedVo> choisedList) {
        UserLoginQuery user = localUser.getUser("用户信息");
        for (ChoisedVo choisedVo:choisedList) {
            choisedVo.setUpdatedId(String.valueOf(user.getId()));
        }
        int insert = baseMapper.denyChoised(choisedList);
        if(insert == 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"插入失败");
        }
    }


    /**
     * 换人拒绝列表查询
     * @param LineName
     * @param OutDate
     * @return
     */
    @Override
    public List<ChangedVo> denyChoisedindex(String LineName, String OutDate) {
        UserLoginQuery user = localUser.getUser("用户信息");
        return baseMapper.denyChoisedindex(LineName,DateUtil.parseDate(OutDate),user.getId());
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
        Price price = priceService.queryPriceByTimeAndLineName(DateUtil.parseDate(OutDate),LineName);
        IncomeVo incomeVo = new IncomeVo();
        incomeVo.setComeedCount(0);
        incomeVo.setAdultNumber(0);
        incomeVo.setChildNumber(0);
        incomeVo.setBabyNumber(0);
        incomeVo.setOldNumber(0);
        incomeVo.setUnpaid(0);
        incomeVo.setSumInCome(0.0);
        Double sum = 0.0;
        Double realsum = 0.0;
        Map<String,String> list = new HashMap<String,String>();
        for (InComeRemerberVo item:incomeRemerberVoList) {
           incomeVo.setComeedCount(incomeVo.getComeedCount() + item.getAllNumber());
           incomeVo.setAdultNumber(incomeVo.getAdultNumber()+item.getAdultNumber());
           incomeVo.setBabyNumber(incomeVo.getBabyNumber()+item.getBabyNumber());
           incomeVo.setOldNumber(incomeVo.getOldNumber() + item.getOldNumber());
           incomeVo.setChildNumber(incomeVo.getChildNumber() + item.getChildNumber());
           if(!item.getIsPayment()){
               incomeVo.setUnpaid(incomeVo.getUnpaid()+1);
           }else{
               realsum = realsum + item.getOldNumber()*price.getOldPrice()+
                       item.getBabyNumber()*price.getBabyPrice()+
                       item.getAdultNumber()*price.getAdultPrice()+
                       item.getChildNumber()*price.getChildPrice();
           }
           sum = sum + item.getOldNumber()*price.getOldPrice()+
                   item.getBabyNumber()*price.getBabyPrice()+
                   item.getAdultNumber()*price.getAdultPrice()+
                   item.getChildNumber()*price.getChildPrice();
           list.put(item.getContactTel(),item.getContactName());
        }
        incomeVo.setContactNames(list);
        incomeVo.setSumInCome(sum);
        incomeVo.setRealInCome(realsum);
        return incomeVo;
    }


    /**
     * 未付款人信息
     * @param ContactTel
     * @return
     */
    @Override
    public UnpaidInformationVo unpaidInformation(String ContactTel, String LineName, String OutDate) {
        UnpaidInformationVo unpaidInformationVo = baseMapper.unpaidInformation(ContactTel,LineName,DateUtil.parseDate(OutDate));
        Price price = priceService.queryPriceByTimeAndLineName(DateUtil.parseDate(OutDate),LineName);
        Double sum = unpaidInformationVo.getOldNumber()*price.getOldPrice()+
                unpaidInformationVo.getBabyNumber()*price.getBabyPrice()+
                unpaidInformationVo.getAdultNumber()*price.getAdultPrice()+
                unpaidInformationVo.getChildNumber()*price.getChildPrice();
        unpaidInformationVo.setSumPaid(sum);
        return unpaidInformationVo;

    }


}
