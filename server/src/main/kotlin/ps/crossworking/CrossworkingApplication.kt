package ps.crossworking

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@SpringBootApplication
@ConfigurationPropertiesScan
class CrossworkingApplication(private val configProperties: ConfigProperties) {

    init {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
            .build()

        FirebaseApp.initializeApp(options)
    }

    @Bean
    fun dataSource() = PGSimpleDataSource().apply {
        setURL(configProperties.dbConnString)
    }

    @Bean
    fun jdbi(ds: DataSource): Jdbi {
        val proxy = TransactionAwareDataSourceProxy(ds)
        return Jdbi.create(proxy).apply {
            installPlugin(KotlinPlugin())
            installPlugin(PostgresPlugin())
            installPlugin(SqlObjectPlugin())
        }
    }
}

fun main(args: Array<String>) {
    runApplication<CrossworkingApplication>(*args)
}
