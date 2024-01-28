package com.chao.share.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 自定义 Swagger 接口文档配置
 * 注解 “@Profile” 是限制本地环境才开启knife4j
 *
 * @author 超
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class Knife4jConfig {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里一定要标注你控制器的位置
                .apis(RequestHandlerSelectors.basePackage("com.chao.share.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息
     *
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口中心")
                .description("接口文档")
                .termsOfServiceUrl("https://github.com/xiaochaoye")
                .contact(new Contact("超","https://github.com/xiaochaoye","2861184979@qq.com"))
                .version("1.0")
                .build();
    }
}