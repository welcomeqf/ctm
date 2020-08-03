package eqlee.ctm.apply;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.yq.handle.ApplicationAdviceHandle;
import com.yq.handle.GlobalResponseHandler;
import com.yq.httpclient.HttpClientUtils;
import com.yq.utils.SendUtils;
import com.yq.jwt.contain.LocalUser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@EnableScheduling
@MapperScan("eqlee.ctm.apply.*.dao")
public class ApplyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApplyApplication.class, args);
    }


    @Bean
    public LocalUser getLocalUser() {
        return new LocalUser();
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
     * 多表分页的bean
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public HttpClientUtils getHttpClientUtils () {
        return new HttpClientUtils ();
    }

    @Bean
    public SendUtils getSendUtils () {
        return new SendUtils ();
    }


    /**
     * 打包
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplyApplication.class);
    }
}
