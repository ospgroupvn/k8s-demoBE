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
        entityManagerFactoryRef = "db4EntityManagerFactory",
        transactionManagerRef = "db4TransactionManager",
        basePackages = { "com.osp.bttp.dao.model.entity.db4", "com.osp.bttp.dao.repository.db4" })
@EnableTransactionManagement
public class Db4Config {

    @Bean(name = "dataSource4")
    @ConfigurationProperties(prefix = "spring.datasource.db4")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db4EntityManagerFactory")
    LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(final EntityManagerFactoryBuilder builder,
                                                                   @Qualifier("dataSource4") DataSource dataSource) {

        final HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.osp.bttp.dao.model.entity.db4")
                .persistenceUnit("db4")
                .build();
    }

    @Bean(name = "db4TransactionManager")
    PlatformTransactionManager db4TransactionManager(@Qualifier("db4EntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
