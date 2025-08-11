package com.osp.bttp.common.config.dbConfig;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "db1EntityManagerFactory",
        transactionManagerRef = "db1TransactionManager",
        basePackages = { "com.osp.bttp.dao.model.entity.db1" })
@EnableTransactionManagement
public class Db1Config {

    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db1EntityManagerFactory")
    LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(final EntityManagerFactoryBuilder builder,
                                                                   @Qualifier("dataSource1") DataSource dataSource) {

        final HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.osp.bttp.dao.model.entity.db1")
                .persistenceUnit("db1")
                .build();
    }

    @Bean(name = "db1TransactionManager")
    PlatformTransactionManager db1TransactionManager(@Qualifier("db1EntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
