package com.example.api.vod.config

import jakarta.persistence.EntityManagerFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.Map

class PersistenceConfigurationTest {
    private lateinit var persistenceConfiguration: com.example.api.vod.config.PersistenceConfiguration
    private val DRIVER_CLASS = "org.h2.Driver"
    private val URL = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;MODE=MYSQL"
    private val USERNAME = "sa"
    private val PASSWORD = "sa"


    @BeforeEach
    fun beforeEach() {
        persistenceConfiguration = PersistenceConfiguration()
    }

    @Nested
    internal inner class DataSourceProperties {
        @Test
        fun dataSourcePropertiesTest() {
            Assertions.assertNotNull(persistenceConfiguration.vodDataSourceProperties())
        }
    }

    @Nested
    internal inner class DataSource {
        @Test
        fun dataSourceTest() {
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.url = URL
            dataSourceProperties.username = USERNAME
            dataSourceProperties.password = PASSWORD
            dataSourceProperties.driverClassName = DRIVER_CLASS
            Assertions.assertNotNull(persistenceConfiguration.vodDataSource(dataSourceProperties))
        }


    }

    @Nested
    internal inner class UserManagerFactory {
        @Test
        fun vodEntityManagerFactoryTest() {
            val factory = EntityManagerFactoryBuilder(
                HibernateJpaVendorAdapter(), Map.of<String, Any>(), null
            )
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.url = URL
            dataSourceProperties.username = USERNAME
            dataSourceProperties.password = PASSWORD
            dataSourceProperties.driverClassName = DRIVER_CLASS
            val dataSource = persistenceConfiguration.vodDataSource(dataSourceProperties)
            Assertions.assertNotNull(persistenceConfiguration.vodEntityManagerFactory(dataSource, factory))
        }
    }

    @Nested
    internal inner class TestPlatformTransactionManager {
        @Test
        fun platformTransactionManagerTest() {
            val managerMock: EntityManagerFactory = Mockito.mock(EntityManagerFactory::class.java)
            Assertions.assertNotNull(persistenceConfiguration.transactionManager(managerMock))
        }
    }

    @Nested
    internal inner class AuditorAwareTest {
        @Test
        fun auditorProviderTest() {
            Assertions.assertNotNull(persistenceConfiguration.auditorProvider())
        }
    }
}