package eqlee.ctm.api;


import com.yq.handle.ApplicationAdviceHandle;
import com.yq.handle.GlobalResponseHandler;
import com.yq.httpclient.HttpClientUtils;
import com.yq.jwt.contain.LocalUser;
import com.yq.utils.IdGenerator;
import com.yq.utils.SendUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("eqlee.ctm.api.*.dao")
public class ApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
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
        return builder.sources(ApiApplication.class);
    }

}
