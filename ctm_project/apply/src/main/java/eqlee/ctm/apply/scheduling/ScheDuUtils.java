package eqlee.ctm.apply.scheduling;

import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每10分钟回收一次未支付订单
 * @author qf
 * @date 2019/11/14
 * @vesion 1.0
 **/
@Slf4j
@Component
public class ScheDuUtils {

   @Autowired
   private IApplyService applyService;

   @Autowired
   private IOrdersService ordersService;

   @Scheduled(cron = "0 */10 * * * ?")
   public void toDeleteApply () {

      //回收订单
      applyService.dopAllApply();

   }


   /**
    * 每天晚上1点更新车辆状态
    */
   @Scheduled(cron = "0 0 1 * * ?")
   public void upCarStatus () {

      //修改车辆出行状态
      ordersService.upCarStatus ();
   }

}
