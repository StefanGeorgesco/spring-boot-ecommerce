package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Country;
import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import com.luv2code.ecommerce.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unsupportedMethods = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        // disable HTTP methods for Product: PUT, POST, DELETE and PATCH
        disableHttpMethod(config, unsupportedMethods, Product.class);

        // disable HTTP methods for ProductCategory: PUT, POST, DELETE and PATCH
        disableHttpMethod(config, unsupportedMethods, ProductCategory.class);

        // disable HTTP methods for State: PUT, POST, DELETE and PATCH
        disableHttpMethod(config, unsupportedMethods, State.class);

        // disable HTTP methods for Country: PUT, POST, DELETE and PATCH
        disableHttpMethod(config, unsupportedMethods, Country.class);

        // expose entity ids
        exposeIds(config);
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