package eqlee.ctm.msg;

import com.yq.handle.ApplicationAdviceHandle;
import com.yq.handle.GlobalResponseHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qf
 */
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@EnableScheduling
@MapperScan("eqlee.ctm.msg.*.dao")
public class MsgApplication extends SpringBootServletInitializer {

   public static void main(String[] args) {
      SpringApplication.run(MsgApplication.class, args);
   }


   @Bean
   public GlobalResponseHandler getGlobalResponseHandler() {
      return new GlobalResponseHandler();
   }

   @Bean
   public ApplicationAdviceHandle getApplicationAdviceHandle() {
      return new ApplicationAdviceHandle();
   }

   /**
    * 打包
    * @param builder
    * @return
    */
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(MsgApplication.class);
   }

}
