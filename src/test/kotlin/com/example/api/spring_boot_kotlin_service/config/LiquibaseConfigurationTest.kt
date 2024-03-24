package com.example.api.spring_boot_kotlin_service.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class LiquibaseConfigurationTest {
    var liquibaseConfiguration: LiquibaseConfiguration =
        LiquibaseConfiguration()

    @BeforeEach
    fun beforeEach() {
        liquibaseConfiguration = LiquibaseConfiguration()
    }

    @Nested
    internal inner class DataSourceProperties {
        @Test
        fun dataSourcePropertiesTest() {
            Assertions.assertNotNull(liquibaseConfiguration.liquibaseDataSourceProperties())
        }
    }

    @Nested
    internal inner class DataSource {
        @Test
        fun dataSourceTest() {
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.username = "root"
            dataSourceProperties.password = "my-secret-pw"
            dataSourceProperties.url = "jdbc:mysql://localhost:3306/sample-db"
            Assertions.assertNotNull(liquibaseConfiguration.liquibaseDataSource(dataSourceProperties))
        }
    }

    @Nested
    internal inner class UserLiquibase {
        @Test
        fun liquibase() {
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.username = "root"
            dataSourceProperties.password = "my-dhr-pwd"
            dataSourceProperties.url = "jdbc:mysql://localhost:3306/sample-db"
            val dataSource: javax.sql.DataSource = liquibaseConfiguration.liquibaseDataSource(dataSourceProperties)
            val liquibase = liquibaseConfiguration.liquibase(dataSource)
            Assertions.assertNotNull(liquibase)
            Assertions.assertEquals("", liquibase.changeLog)
            Assertions.assertEquals(dataSource, liquibase.dataSource)
        }
    }

}