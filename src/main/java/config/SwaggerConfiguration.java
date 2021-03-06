package config;

import java.util.List;

import ga.rugal.pt.springmvc.controller.PackageInfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for swagger 2.
 *
 * @author Rugal Bernstein
 */
@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfiguration {

  private ApiKey apiKey() {
    // The "bearerAuth" has to match what that is in contract.yml
    return new ApiKey("bearerAuth", "Authorization", "header");
  }

  /**
   * Docket bean.
   *
   * @return bean object
   */
  @Bean
  public Docket apiDocket() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(PackageInfo.class.getPackage().getName()))
            .paths(PathSelectors.any())
            .build()
            // Add this to show "Authorize" button in Springfox UI
            .securitySchemes(List.of(this.apiKey()));
  }
}
