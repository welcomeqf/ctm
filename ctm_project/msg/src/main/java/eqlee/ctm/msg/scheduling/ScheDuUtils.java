package eqlee.ctm.msg.scheduling;

import eqlee.ctm.msg.msgConfig.monitor.MsgMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * @author qf
 * @date 2019/11/14
 * @vesion 1.0
 **/
@Slf4j
@Component
public class ScheDuUtils {

   @Autowired
   private MsgMonitor msgMonitor;

   @Scheduled(cron = "0/5 * * * * ?")
   public void toDeleteApply () {

      msgMonitor.sendMsg();

   }
}
