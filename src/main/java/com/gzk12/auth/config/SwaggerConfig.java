package com.gzk12.auth.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<>();

        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("My-Auth-Token").description("登录后返回的token，访问需要权限的接口需要").
                modelRef(new ModelRef("string")).
                parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(Predicates.not(PathSelectors.ant("/user/**")))
//                .apis(RequestHandlerSelectors.basePackage("com.gzk12.auth.web"))
//                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档测试")
                .description("哈哈哈哈")
                .version("0.1")
                .contact(new Contact("ysn", "", "12412@qq.com"))
                .build();
    }
}
