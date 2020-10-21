package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager theEntityManager;

    @Autowired // can delete this annotation because here is only 1 single constructor
    public MyDataRestConfig(EntityManager theEntityManager) {
        this.theEntityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] unsupportedMethods = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT};

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));


        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)))
                .withItemExposure((metdata, httpmethods) -> httpmethods.disable(unsupportedMethods));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get all entities
        Set<EntityType<?>> entityTypeSet = theEntityManager.getMetamodel().getEntities();

        List<Class> entities = new ArrayList<>();

        for (EntityType entityType : entityTypeSet) {
            entities.add(entityType.getJavaType());
        }

        // expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entities.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}









