package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unsupportedMethods = { HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH };

        // disable unsupported HTTP methods for exposed entities
        disableHttpMethod(config, unsupportedMethods, Product.class);
        disableHttpMethod(config, unsupportedMethods, ProductCategory.class);
        disableHttpMethod(config, unsupportedMethods, State.class);
        disableHttpMethod(config, unsupportedMethods, Country.class);
        disableHttpMethod(config, unsupportedMethods, Order.class);

        // expose entity ids
        exposeIds(config);

        // configure cors mapping
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);
    }

    /**
     * Helper method to disable Http methods for an entity
     * @param config org.springframework.data.rest.core.config.RepositoryRestConfiguration instance
     * @param unsupportedMethods the http methods to disable
     * @param clazz the entity class
     */
    private void disableHttpMethod(RepositoryRestConfiguration config, HttpMethod[] unsupportedMethods, Class<?> clazz) {
        config.getExposureConfiguration()
                .forDomainType(clazz)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods));
    }

    /**
     * Helper method to expose entity ids
     * @param config la configuration
     */
    private void exposeIds(RepositoryRestConfiguration config) {

        Class<?>[] entityClasses = entityManager.getMetamodel().getEntities()
                .stream()
                .map(EntityType::getJavaType)
                .toList()
                .toArray(new Class[0]);

        config.exposeIdsFor(entityClasses);
    }
}