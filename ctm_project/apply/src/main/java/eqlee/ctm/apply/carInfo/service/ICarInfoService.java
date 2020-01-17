package eqlee.ctm.apply.carInfo.service;

import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;

import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
public interface ICarInfoService {


   /**
    * 增加该天的车辆信息
    * @param vo
    */
   void insertCarInfo (CarInfoInsertVo vo);

   /**
    * 查询该天没有出行的车辆
    * @param orderId
    * @return
    */
   List<CarInfoQuery> queryCarInfo (Long orderId);

   /**
    * 删除
    * @param orderId
    */
   void deleteCarInfo (Long orderId);
}
