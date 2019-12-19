package config;

import ga.rugal.pt.springmvc.controller.PackageInfo;
import ga.rugal.pt.springmvc.interceptor.AuthenticationInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Java based Web context configuration class. Including argument resolution, message converter,
 * view resolution etc.,
 *
 * @author Rugal Bernstein
 * @since 0.2
 */
@ComponentScan(basePackageClasses = PackageInfo.class)
@Configuration
@EnableWebMvc
public class SpringMvcApplicationContext implements WebMvcConfigurer {

  @Autowired
  private AuthenticationInterceptor authenticationInterceptor;

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/",
                                  "classpath:/META-INF/resources/",
                                  "classpath:/resources/ ",
                                  "classpath:/public/");
  }

  @Override
  public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
    configurer.favorPathExtension(false).favorParameter(false);
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }

  /**
   * Handler mapping.
   *
   * @return handler mapping object
   */
  @Bean
  public AbstractHandlerMapping defaultAnnotationHandlerMapping() {
    final RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
    mapping.setUseSuffixPatternMatch(false);
    return mapping;
  }

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*");
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    //This is a very important interceptor for authentication usage
    registry.addInterceptor(this.authenticationInterceptor)
            .addPathPatterns("/post/**");
  }
}
