package com.example.api.spring_boot_kotlin_service.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EntityScan("com.example.api.spring_boot_kotlin_service")
@EnableJpaRepositories(
    entityManagerFactoryRef = "sampleEntityManagerFactory",
    transactionManagerRef = "transactionManager",
    basePackages = ["com.example.api.spring_boot_kotlin_service"]
)
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class PersistenceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.sample-db")
    fun sampleDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    fun sampleDataSource(@Qualifier("sampleDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().build()
    }

    @Bean
    @Primary
    fun sampleEntityManagerFactory(
        @Qualifier("sampleDataSource") hubDataSource: DataSource?,
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(hubDataSource)
            .packages("com.example.api.spring_boot_kotlin_service")
            .persistenceUnit("sample")
            .build()
    }

    @Bean
    @Primary
    fun transactionManager(@Qualifier("sampleEntityManagerFactory") factory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(factory!!)
    }

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAwareConfiguration()
    }
}