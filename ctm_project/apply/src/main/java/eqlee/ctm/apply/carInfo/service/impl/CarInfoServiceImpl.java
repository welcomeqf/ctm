package eqlee.ctm.apply.carInfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.carInfo.dao.CarInfoMapper;
import eqlee.ctm.apply.carInfo.entity.CarInfo;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.carInfo.entity.vo.DateVo;
import eqlee.ctm.apply.carInfo.service.ICarInfoService;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarInfoServiceImpl extends ServiceImpl<CarInfoMapper, CarInfo> implements ICarInfoService {

   @Autowired
   private IOrdersService ordersService;

   @Autowired
   private LocalUser localUser;


   @Override
   public void insertCarInfo(CarInfoInsertVo vo) {

      UserLoginQuery user = localUser.getUser("用户信息");

      //先查询订单信息
      Orders orders = ordersService.queryOne(vo.getOrderId());

      IdGenerator idGenerator = new IdGenerator();

      //几日游
      //批量增加车辆信息
      //因为批量增加数据不多，就直接在程序里for循环添加了
      for (int i = 0; i < orders.getTravelSituation(); i++) {
         CarInfo info = new CarInfo();
         info.setId(idGenerator.getNumberId());
         info.setCarId(vo.getCarId());
         info.setOutDate(orders.getOutDate().plusDays(i));
         info.setCreateUserId(user.getId());
         info.setOrderId(vo.getOrderId());
         baseMapper.insert(info);
      }



   }



   @Override
   public List<CarInfoQuery> queryCarInfo(Long orderId) {

      //先查询订单信息
      Orders orders = ordersService.queryOne(orderId);

      //出游天数的集合
      List<DateVo> list = new ArrayList<>();

      //几日游
      for (int i = 0; i < orders.getTravelSituation(); i++) {
         DateVo vo = new DateVo();
         vo.setDate(orders.getOutDate().plusDays(i));
         list.add(vo);
      }

      return baseMapper.queryCarInfo (list);
   }

   @Override
   public void deleteCarInfo(Long orderId) {
      LambdaQueryWrapper<CarInfo> wrapper = new LambdaQueryWrapper<CarInfo>()
            .eq(CarInfo::getOrderId,orderId);

      int delete = baseMapper.delete(wrapper);

      if (delete <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }
}
