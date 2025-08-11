package com.osp.bttp.common.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Getter
@Configuration
@EnableAspectJAutoProxy
@EnableScheduling
public class ApplicationConfigCommon {

    /*
     * dev
     * */
    private static final Resource[] DEV_PROPERTIES = new ClassPathResource[]{new ClassPathResource("application-dev.properties"),};

    private static final Resource[] PROD_PROPERTIES = new ClassPathResource[]{new ClassPathResource("application-prod.properties"),};
//    private static final Resource[] PROD_PROPERTIES
//            = new FileSystemResource[]{new FileSystemResource("./config/application-prod.properties"),};

    @Component
    @Profile("dev")
    public static class DevConfig {
        private DevConfig() {
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(DEV_PROPERTIES);
            return pspc;
        }
    }

    @Component
    @Profile("prod")
    public static class ProdConfig {

        private ProdConfig() {
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(PROD_PROPERTIES);
            return pspc;
        }
    }

}
