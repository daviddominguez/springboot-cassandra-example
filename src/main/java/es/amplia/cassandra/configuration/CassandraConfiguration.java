package es.amplia.cassandra.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.*;
import es.amplia.cassandra.repository.Repository;
import es.amplia.cassandra.service.NorthMessagesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import static es.amplia.cassandra.bucket.Bucket.Type.MONTH;
import static es.amplia.cassandra.bucket.Bucket.Type.SEMESTER;
import static es.amplia.cassandra.bucket.Bucket.Type.WEEK;

@Configuration
@ComponentScans({
    @ComponentScan(basePackageClasses = {Repository.class}),
        @ComponentScan(basePackageClasses = {NorthMessagesService.class})
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
    @BucketType(WEEK)
    public Bucket weekBucket() {
        return new WeekBucket();
    }

    @Bean
    @BucketType(MONTH)
    public Bucket monthBucket() {
        return new MonthBucket();
    }

    @Bean
    @BucketType(SEMESTER)
    public Bucket semesterBucket() {
        return new SemesterBucket();
    }
}
