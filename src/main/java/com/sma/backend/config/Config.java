/*
 * this is header file
 */
package com.sma.backend.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sma.backend.ErrorResponse;
import com.sma.backend.SwaggerPublicApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Config class.
 *
 * @author sophea mak
 */
@Configuration
@EnableSwagger2
@Slf4j
public class Config {

    @Value("#{${aws.secret.manager}}")
    private Map<String, String> awsConfigProperties;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Bean
    public FilterRegistrationBean<Filter> basicAuthenticationFilter(Environment env) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        final Filter filter = new AuthenticationFilter();

        registration.addInitParameter("username", env.getProperty("basic.username"));
        registration.addInitParameter("secret", env.getProperty("basic.password"));
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");
        registration.addUrlPatterns("/swagger-ui.html");
        registration.addUrlPatterns("/swagger-ui/*");
        registration.addUrlPatterns("/actuator/*");
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public Map<String, String> loadAwsSecretBean() {
        log.info("=========LOADING AWS SECRET MANAGER CONFIG=============");
        log.info(this.gson.toJson(this.awsConfigProperties));
        return this.awsConfigProperties;
    }

    /**
     * SWAGGER REST-APIs configuration.
     *
     * @return ApiInfo object
     */
    @Bean
    public ApiInfo apiEndPointsInfo() {
        return (new ApiInfoBuilder())
                .title("Simple API")
                .description("Simple API")
                .termsOfServiceUrl("Terms of Service applied")
                .version("1.0.0")
                .contact(
                        new Contact(
                                "Sophea Mak", "https://github.com/sophea", "sopheamak@gmail.com"))
                .build();
    }

    @Bean
    public Docket documentApi(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndPointsInfo())
                .select()
                // .apis(RequestHandlerSelectors.basePackage("com.sma"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(SwaggerPublicApi.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .ignoredParameterTypes(Resource.class)
                .useDefaultResponseMessages(false)
                .globalResponses(
                        HttpMethod.GET,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() + "")
                                        .description("Bad Request")
                                        .build(),
                                new ResponseBuilder()
                                        .code(HttpStatus.NOT_FOUND.value() + "")
                                        .description("Not Found")
                                        .build()))
                .globalResponses(
                        HttpMethod.POST,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() + "")
                                        .description("Bad Request")
                                        .build(),
                                new ResponseBuilder()
                                        .code(HttpStatus.CONFLICT.value() + "")
                                        .description("Conflict")
                                        .build()))
                .globalResponses(
                        HttpMethod.PUT,
                        Arrays.asList(
                                new ResponseBuilder()
                                        .code(HttpStatus.BAD_REQUEST.value() + "")
                                        .description("Bad Request")
                                        .build(),
                                new ResponseBuilder()
                                        .code(HttpStatus.NOT_FOUND.value() + "")
                                        .description("Not Found")
                                        .build()))
                .additionalModels(typeResolver.resolve(ErrorResponse.class));
    }

    /**
     * 2 beans below to fix with springfox version 3.x with springboot 2.6.x with
     * application.properties spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER. Ref :
     * https://github.com/springfox/springfox/issues/3462#issuecomment-1010721223
     *
     * @param controllerEndpointsSupplier endpoint
     * @param corsProperties cors
     * @param endpointMediaTypes media type
     * @param environment environment
     * @param servletEndpointsSupplier servlet endpoints
     * @param webEndpointProperties webEndpoints properties
     * @param webEndpointsSupplier webEndpoint
     * @return WebMvcEndpointHandlerMapping object
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier,
            ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier,
            EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties,
            WebEndpointProperties webEndpointProperties,
            Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping =
                this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(
                endpointMapping,
                webEndpoints,
                endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping);
    }

    /**
     * shouldRegisterLinksMapping.
     *
     * @param webEndpointProperties webEndpointProperties
     * @param environment environment
     * @param basePath basePath
     * @return boolean true/false
     */
    private boolean shouldRegisterLinksMapping(
            WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled()
                && (StringUtils.hasText(basePath)
                        || ManagementPortType.get(environment)
                                .equals(ManagementPortType.DIFFERENT));
    }
}
