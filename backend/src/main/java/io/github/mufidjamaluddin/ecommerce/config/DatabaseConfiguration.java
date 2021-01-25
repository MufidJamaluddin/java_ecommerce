package io.github.mufidjamaluddin.ecommerce.config;

import io.github.mufidjamaluddin.ecommerce.shared.Gender;
import io.github.mufidjamaluddin.ecommerce.utils.Parser;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.lang.NonNull;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PreDestroy;
import java.time.Duration;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

    @Value("${db.host:localhost}")
    private String dbHost;

    @Value("${db.database:test}")
    private String dbDatabase;

    @Value("${db.username:user}")
    private String dbUsername;

    @Value("${db.password:password}")
    private String dbPassword;

    @Value("${db.max_idle_time:1000}")
    private int dbMaxIdleTime;

    @Value("${db.max_connections:20}")
    private int dbMaxConnections;

    @Bean
    @NonNull
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(this.dbHost)
                        .database(this.dbDatabase)
                        .username(this.dbUsername)
                        .password(this.dbPassword)
                        .codecRegistrar(
                                EnumCodec.builder()
                                        .withEnum("gender", Gender.class)
                                        .build())
                        .build()
        );
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();

        ConnectionPoolConfiguration configuration =
                ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(this.dbMaxIdleTime))
                .maxSize(this.dbMaxConnections)
                .build();

        ConnectionPool pool = new ConnectionPool(configuration);

        initializer.setConnectionFactory(pool);
        initializer.setDatabaseCleaner(connection -> pool.disposeLater());

        return initializer;
    }

    @PreDestroy
    public void onDestroy(ConnectionFactoryInitializer connections) throws Exception {
        connections.destroy();
    }
}
