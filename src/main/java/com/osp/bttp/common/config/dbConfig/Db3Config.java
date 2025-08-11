package com.osp.bttp.common.config.dbConfig;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author sangnk
 * @Created 08/10/2024 - 4:55 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = { "com.osp.bttp.dao.model.entity.db3", "com.osp.bttp.dao.repository.db3","com.osp.bttp.dao.repository.bttp" })
@EnableTransactionManagement
public class Db3Config {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(final EntityManagerFactoryBuilder builder,
                                                                   @Qualifier("dataSource") DataSource dataSource) {

        final HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.osp.bttp.dao.model.entity.db3")
                .persistenceUnit("db3")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    PlatformTransactionManager dbTransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
