package eqlee.ctm.apply.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.entity.websocket.NettyType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.carInfo.service.ICarInfoService;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.bo.ApplyGuideBo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.message.entity.vo.MsgVo;
import eqlee.ctm.apply.message.service.IMessageService;
import eqlee.ctm.apply.orders.dao.OrdersMapper;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.orders.entity.OrderSubstitut;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.*;
import eqlee.ctm.apply.orders.entity.query.*;
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
   private ILineService lineService;

   @Autowired
   private ICarInfoService carInfoService;

   @Autowired
   private HttpUtils httpUtils;

   @Autowired
   private IMessageService messageService;

   @Autowired
   private IOrderSubstitutService orderSubstitutService;

   IdGenerator idGenerator = new IdGenerator();

    /**
     * 导游选人
     * @param indexVoList
     */
    @Override
    public Long saveApply(List<LongVo> indexVoList) {

        List<Long> ids = new ArrayList<>();
        //重装list
        for (LongVo vo : indexVoList) {
            ids.add(vo.getId());
        }

        Long result;

        //查询报名表
        List<Apply> applies = applyService.listApply(ids);

        List<OrderDetailedBo> orderDetailedList = new ArrayList<>();

        UserLoginQuery user = localUser.getUser("用户信息");

        Orders orders = new Orders();
        long numberId = idGenerator.getNumberId();
        orders.setId(numberId);
        orders.setOrderNo(idGenerator.getShortNo());
        orders.setGuideName(user.getCname());
        orders.setGuideTel(user.getTel());
        orders.setCreateUserId(user.getId());


        //先暂定1天   后续需要调整
       //要根据线路的出游天数来设置
       //因为考虑到拼团的情况   1天的线路和2天的线路拼团暂时不确定
       //后期调整
        orders.setTravelSituation(1);

        Double allPrice = 0.0;

        Orders order = null;
       String newLineName = "";

       //装配线路参数集合
       List<Long> lineIds = new ArrayList<>();

       //装配修改报名表参数集合
       List<ApplyGuideBo> list = new ArrayList<>();

       for (Apply apply : applies) {
            //装配订单
            OrderDetailedBo orderDetailed = new OrderDetailedBo();

            orders.setOutDate(apply.getOutDate());
            orders.setRegion(apply.getRegion());
            orders.setCity(apply.getCity());
            orders.setCompanyId(apply.getCompanyId());

            allPrice = allPrice + apply.getAllPrice();

            //月结金额
           if (apply.getPayType() == 1 || apply.getPayType() == 2) {
              orderDetailed.setMonthPrice(apply.getAllPrice());
           } else {
              orderDetailed.setMonthPrice(0.0);
           }

           //线路名，备注
          Line line = lineService.queryOneLine(apply.getLineId());

           orderDetailed.setApplyId(apply.getId());
           orderDetailed.setLineName(line.getLineName());
           orderDetailed.setApplyRemark(apply.getApplyRemark());
           orderDetailed.setTypes(apply.getType());

           orderDetailed.setAccountName(apply.getCompanyUser());
           orderDetailed.setCompanyName(apply.getCompanyName());
           orderDetailed.setAdultNumber(apply.getAdultNumber());
           orderDetailed.setBabyNumber(apply.getBabyNumber());
           orderDetailed.setChildNumber(apply.getChildNumber());
           orderDetailed.setOldNumber(apply.getOldNumber());
           orderDetailed.setAllNumber(apply.getAllNumber());
            if (apply.getAllPrice() != null) {
               orderDetailed.setPrice(apply.getAllPrice());
            }
            orderDetailed.setCreateUserId(user.getId());
            orderDetailed.setPayType(apply.getPayType());

            if (apply.getMsPrice() == null) {
               apply.setMsPrice(0.0);
            }

            orderDetailed.setMsPrice(apply.getMsPrice());

            //同行代表人姓名和电话
           orderDetailed.setCompanyUserName(apply.getCName());
           orderDetailed.setCompanyUserTel(apply.getCompanyTel());

            //先判断该天是否已经选了人
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                    .eq(Orders::getOutDate, orders.getOutDate())
                    .eq(Orders::getCreateUserId, user.getId());
            order = baseMapper.selectOne(wrapper);

            //订单详情表
            if (order != null) {

               //如果订单已经完成，则当天不能再选人
               //如果导游已经提交了，只要不是被拒绝，全部看作是订单已完成
               if (order.getIsFinash()) {
                  throw new ApplicationException(CodeType.SERVICE_ERROR, "该天订单已完成，不能再选人");
               }

                orderDetailed.setOrderId(order.getId());
                newLineName = order.getLineName();
            }

            //订单为空  说明还没有开始选人
            //第一次选人
            if (order == null) {
                orderDetailed.setOrderId(numberId);
            }

            orderDetailed.setContactName(apply.getContactName());
            orderDetailed.setContactTel(apply.getContactTel());
            orderDetailed.setId(idGenerator.getNumberId());
            orderDetailed.setPlace(apply.getPlace());
            orderDetailedList.add(orderDetailed);


          lineIds.add(apply.getLineId());


          //装配修改报名表参数的集合
          ApplyGuideBo bo = new ApplyGuideBo();
          bo.setId(apply.getId());
          bo.setGuide(user.getCname());
          bo.setTel(user.getTel());
          list.add(bo);
        }

       //查询线路集合
       List<Line> lines = lineService.queryByIdList(lineIds);

       for (Line line : lines) {
          //第一次进来只有一条线路就生成
          //多条线路就拼团
          //如果只有一条线路就取线路名
          //如果是多条线路就取第一条线路名
          if (order == null) {
             //第一次进来
             newLineName = line.getLineName();
             break;
          } else {
             //第二次进来直接拼团
            break;
          }

       }

       orders.setAllPrice(allPrice);
        //没有该订单就生成订单
        if (order == null) {
           result = numberId;
           orders.setLineName(newLineName);
            int insert = baseMapper.insert(orders);

            if (insert <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"生成订单失败");
            }
        } else {
           result = order.getId();

           //修改
           Double price = order.getAllPrice();
           Double newAllPrice = allPrice + price;

           Orders orders1 = new Orders();
           orders1.setId(order.getId());
           orders1.setAllPrice(newAllPrice);
           orders1.setIsFinash(true);

           int update = baseMapper.updateById(orders1);

           if (update <= 0) {
              throw new ApplicationException(CodeType.SERVICE_ERROR, "选人失败");
           }

        }

        //有此订单了就不再生成订单
        orderDetailedService.batchInsertorderDetailed(orderDetailedList);

        //修改报名表导游选人状态

       //优化---
       applyService.updateAllGuestStatus (list);

       return result;
    }


    /**
     *由线路名，出发时间，导游姓名得到车牌号
     * @return
     */
    @Override
    public OrderDetailedFainllyQuery getCarNumber(Long id) {
       Orders orders1 = baseMapper.selectById(id);

       OrderDetailedFainllyQuery query = new OrderDetailedFainllyQuery();
       query.setCarNo(orders1.getCarNumber());
       query.setLineName(orders1.getLineName());
       query.setOutDate(DateUtil.formatDate(orders1.getOutDate()));
       query.setGuideName(orders1.getGuideName());
       return query;
    }



    /**
     * 导游换人
     * @param orderIndexVos
     * @param Id
     */
    @Override
    public void updateApply(List<OrderIndexVo> orderIndexVos,Long Id,String lineName, String outDate) {
        //查询被换人的导游是否是在同一天同一条线路
        LocalDate localDate = DateUtil.parseDate(outDate);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOutDate,localDate)
                .eq(Orders::getCreateUserId,Id);
        Orders orders = baseMapper.selectOne(wrapper);

        if (orders == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该导游这天在该线路不出行");
        }

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

       //增加消息提醒的记录
//        MsgVo vo = new MsgVo();
//       vo.setCreateId(user.getId());
//       vo.setMsg(NettyType.PERSON_EXA.getMsg());
//       vo.setMsgType(3);
//
//       vo.setToId(Id);
//       messageService.insertMsg(vo);
    }

   /**
    * 排车
    * @param carId
    * @param orderId
    * @param carNo
    */
    @Override
    public void save(Long carId, Long orderId, String carNo) {

        //先查询订单的情况
       Orders order = baseMapper.selectById(orderId);

       if (order == null) {
          throw new ApplicationException(CodeType.SERVICE_ERROR);
       }

       if (StringUtils.isNotBlank(order.getCarNumber())) {
          //已经排了车了
          //先删除  再重新添加
          carInfoService.deleteCarInfo(orderId);
       }


       if (carId != null) {
          //公司内部车辆
          //查询车牌号
          CarQueryBo bo = baseMapper.queryCar(carId);

          if (bo.getStatus() == 2) {
             throw new ApplicationException(CodeType.SERVICE_ERROR, "该车辆正在维修");
          }

          //添加
          CarInfoInsertVo vo = new CarInfoInsertVo();
          vo.setCarId(carId);
          vo.setOrderId(orderId);
          carInfoService.insertCarInfo(vo);

          int update = baseMapper.updateOrdersCarNo(orderId,bo.getCarNo());

          if(update == 0){
             throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
          }
       }

        if(carId == null) {
            //公司外部车辆
            int update = baseMapper.updateOrdersOutsideCarNo(orderId,carNo);
            if(update == 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"更新失败");
            }
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
     * @param id
     * @param outDate
     * @param lineName
     */
    @Override
    public void sureChoised(Long id, String outDate, String lineName) {
        UserLoginQuery user = localUser.getUser("用户信息");

        //查询到该条的订单信息
        LocalDate localDate = DateUtil.parseDate(outDate);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOutDate,localDate)
                .eq(Orders::getCreateUserId,user.getId());

        Orders orders = baseMapper.selectOne(wrapper);

        if (orders == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "线路或日期不一致不能换人");
        }

       OrderDetailed detailed = orderDetailedService.queryById(id);

       //增加一条同意换人的记录
//        MsgVo vo = new MsgVo();
//        vo.setCreateId(user.getId());
//        vo.setMsg(NettyType.AGGRE_NO_PERSON_EXA.getMsg());
//        vo.setMsgType(1);
//
//        vo.setToId(detailed.getCreateUserId());
//        messageService.insertMsg(vo);

        //修改换人表数据
        orderSubstitutService.apotSubstitution(DateUtil.parseDate(outDate),lineName,user.getId(),1);


        int update = baseMapper.sureChoised(id, orders.getId());

        if(update <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }

    }


    /**
     * 拒绝换人
     * @param id
     * @param outDate
     * @param lineName
     */
    @Override
    public void denyChoised(Long id, String outDate, String lineName) {

        UserLoginQuery user = localUser.getUser("用户信息");


       OrderDetailed detailed = orderDetailedService.queryById(id);

        //增加一条拒绝换人的记录
//        MsgVo vo = new MsgVo();
//        vo.setCreateId(user.getId());
//        vo.setMsg(NettyType.AGGRE_PERSON_EXA.getMsg());
//        vo.setMsgType(2);
//
//        vo.setToId(detailed.getCreateUserId());
//        messageService.insertMsg(vo);

        //修改换人表数据
        orderSubstitutService.apotSubstitution(DateUtil.parseDate(outDate),lineName,user.getId(),2);

        int insert = baseMapper.denyChoised(id);

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
     * @param orderId
     * @return
     */
    @Override
    public IncomeVo incomeCount(Long orderId) {

       IncomeVo incomeVo = baseMapper.selectIncomeCount(orderId);

       //查询车辆类型
       Orders orders = baseMapper.selectById(orderId);

       if (orders != null) {
          incomeVo.setCarType(orders.getCarType());
       }

       IncomeVo vo = baseMapper.queryStatus(orderId);

       if (vo != null) {
          incomeVo.setStatus(vo.getStatus());
       } else {
          if (incomeVo != null) {
             incomeVo.setStatus(3);
          }
       }

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

   /**
    * 查询未结算的条数
    * @return
    */
   @Override
   public Integer queryAllNoCount () {
      UserLoginQuery user = localUser.getUser("用户信息");
      LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
            .eq(Orders::getCreateUserId,user.getId())
            .eq(Orders::getIsFinash,0);

      //查询条数
      return baseMapper.selectCount(wrapper);
   }

   /**
    * 查询具体未结算的信息
    * @return
    */
   @Override
   public List<OrdersNoCountInfoQuery> queryAllNoCountInfo() {
      UserLoginQuery user = localUser.getUser("用户信息");
      LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
            .eq(Orders::getCreateUserId,user.getId())
            .eq(Orders::getIsFinash,0);

      List<Orders> orders = baseMapper.selectList(wrapper);

      List<OrdersNoCountInfoQuery> result = new ArrayList<>();
      for (Orders order : orders) {
         OrdersNoCountInfoQuery query = new OrdersNoCountInfoQuery();
         query.setLineName(order.getLineName());
         query.setOutDate(DateUtil.formatDate(order.getOutDate()));
         query.setCarNo(order.getCarNumber());
         query.setId(order.getId());
         result.add(query);
      }

      return result;
   }

   /**
    * 查询id
    * @param outDate
    * @param lineName
    * @return
    */
   @Override
   public IdBo queryOrderId(String outDate, String lineName) {
      UserLoginQuery user = localUser.getUser("用户信息");

      LocalDate date = DateUtil.parseDate(outDate);

      LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
            .eq(Orders::getOutDate,date)
            .eq(Orders::getCreateUserId,user.getId());

      Orders orders = baseMapper.selectOne(wrapper);

      IdBo bo = new IdBo();

      bo.setId(orders.getId());

      return bo;
   }


   /**
    * 查询一条记录
    * @param id
    * @return
    */
   @Override
   public Orders queryOne(Long id) {
      return baseMapper.selectById(id);
   }


   /**
    * 修改车辆出行状态
    */
   @Override
   public void upCarStatus() {

      LocalDate now = LocalDate.now();

      LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
            .le(Orders::getOutDate,now);

      List<Orders> orders = baseMapper.selectList(wrapper);


      Integer integer = baseMapper.upCarStatus(orders);

      if (integer <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改出行状态失败");
      }

   }

   @Override
   public IdBo queryId(String outDate) {

      UserLoginQuery user = localUser.getUser("用户信息");

      LocalDate localDate = null;
      if (StringUtils.isNotBlank(outDate)) {
         localDate = DateUtil.parseDate(outDate);
      }

      LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
            .eq(Orders::getOutDate,localDate)
            .eq(Orders::getCreateUserId,user.getId());

      Orders orders = baseMapper.selectOne(wrapper);

      if (orders == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }

      IdBo bo = new IdBo();
      bo.setId(orders.getId());

      return bo;
   }

}
