package com.yq.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * @Author qf
 * @Date 2019/8/22
 * @Version 1.0
 */
@Configuration
public class SwaggerUtil {

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("易奇公司_CTM_接口文档")
                        .description("文档~")
//                        .contact("XX")
                        .version("2.0.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yq.*.*.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}
