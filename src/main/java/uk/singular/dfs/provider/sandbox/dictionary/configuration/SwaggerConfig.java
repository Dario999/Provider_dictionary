package uk.singular.dfs.provider.sandbox.dictionary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("uk.singular"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails(){
        return new ApiInfo(
                "Dictionary application",
                "Sample dictionary api.",
                "1.0",
                "Free to use",
                new Contact("Dario Ivanovski","http//github.com/Dario999","dario.ivanovski@singular.uk"),
                "Licence - Singular UK",
                "http//singular-group.uk",
                Collections.emptyList()
        );
    }

}
