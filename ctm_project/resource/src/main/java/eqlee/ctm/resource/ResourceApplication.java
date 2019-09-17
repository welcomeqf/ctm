package eqlee.ctm.resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("eqlee.ctm.resource.*.dao")
public class ResourceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }


    /**
     * 打包
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ResourceApplication.class);
    }
}
