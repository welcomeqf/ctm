package eqlee.ctm.apply.scheduling;

import eqlee.ctm.apply.entry.service.IApplyService;
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

   @Scheduled(cron = "0 */10 * * * ?")
   public void toDeleteApply () {

      //回收订单
      applyService.dopAllApply();

   }
}
