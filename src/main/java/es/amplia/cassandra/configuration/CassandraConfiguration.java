package es.amplia.cassandra.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans({
    @ComponentScan("es.amplia.cassandra.repository"),
        @ComponentScan("es.amplia.cassandra.service")
})
public class CassandraConfiguration {

    @Bean
    public Session session(Cluster cluster) {
        return cluster.connect();
    }

    @Bean
    public MappingManager getManager(Session session) {
        return new MappingManager(session);
    }

    @Bean
    public NorthMessagesByIntervalAccessor northMessagesByIntervalAccessor(MappingManager mappingManager) {
        return mappingManager.createAccessor(NorthMessagesByIntervalAccessor.class);
    }

    @Bean
    public NorthMessagesByUserIntervalAccessor northMessagesByUserIntervalAccessor(MappingManager mappingManager) {
        return mappingManager.createAccessor(NorthMessagesByUserIntervalAccessor.class);
    }

    @Bean
    public NorthMessagesByUserSubjectIntervalAccessor northMessagesByUserSubjectIntervalAccessor(MappingManager mappingManager) {
        return mappingManager.createAccessor(NorthMessagesByUserSubjectIntervalAccessor.class);
    }
}
