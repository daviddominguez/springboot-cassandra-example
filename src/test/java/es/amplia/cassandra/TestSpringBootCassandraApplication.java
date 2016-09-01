package es.amplia.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import es.amplia.model.AuditMessage.*;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@SpringBootApplication(exclude = WebClientAutoConfiguration.class)
public class TestSpringBootCassandraApplication extends SpringBootCassandraApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSpringBootCassandraApplication.class);

    @Bean
    @Primary
    public Cluster cluster() {
        LOGGER.info("Starting embedded cassandra server");
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000);
            LOGGER.info("Connect to embedded db");
            Cluster cluster = Cluster.builder()
                    .addContactPoints("127.0.0.1")
                    .withPort(9142)
                    .withQueryOptions(new QueryOptions().setFetchSize(100))
                    .withCodecRegistry(
                            new CodecRegistry()
                                    .register(new EnumNameCodec<>(ProcessType.class))
                                    .register(new EnumNameCodec<>(ComponentType.class))
                                    .register(new EnumNameCodec<>(NameType.class))
                                    .register(new EnumNameCodec<>(MsgType.class))
                                    .register(new EnumNameCodec<>(MsgDirection.class))
                                    .register(new EnumNameCodec<>(SubjectType.class))
                                    .register(new EnumNameCodec<>(MsgStatus.class)))
                    .build();

            Session session = cluster.connect();
            LOGGER.info("Initialize keyspace");
            final CQLDataLoader cqlDataLoader = new CQLDataLoader(session);
            cqlDataLoader.load(new ClassPathCQLDataSet("audit_with_tables.cql", false, true, "audit"));
            return cluster;
        }
        catch (InterruptedException | TTransportException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
