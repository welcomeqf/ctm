package eqlee.ctm.apply.month.service;

import eqlee.ctm.apply.month.entity.MonthPay;
import eqlee.ctm.apply.month.entity.vo.MonthParamVo;
import eqlee.ctm.apply.month.entity.vo.MonthVo;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
public interface IMonthService {

   /**
    * 增加月结账单(月结)
    * @param vo
    * @return
    */
   MonthVo insertMonth (MonthParamVo vo);

   /**
    * 续费授信金额
    * @param monthPrice
    * @return
    */
   MonthVo xuMonth (Double monthPrice);

   /**
    * 修改
    * @param monthPay
    */
   void updateMonth (MonthPay monthPay);


   /**
    * 查询信息
    * @param id
    * @return
    */
   MonthVo queryInfo (Long id);

   /**
    * 修改月季支付状态
    * @param monthNo
    */
   void updateMonthStatus (String monthNo);



}
