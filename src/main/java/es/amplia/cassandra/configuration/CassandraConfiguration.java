package es.amplia.cassandra.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.*;
import es.amplia.cassandra.repository.NorthMessagesByIntervalRepository;
import es.amplia.cassandra.service.NorthMessagesService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import static es.amplia.cassandra.bucket.Bucket.Type.*;

@Configuration
@ComponentScans({
    @ComponentScan(basePackageClasses = {NorthMessagesByIntervalRepository.class}),
        @ComponentScan(basePackageClasses = {NorthMessagesService.class})
})
public class CassandraConfiguration implements DisposableBean {

    private Cluster cluster;

    @Bean
    public Session session(Cluster cluster) {
        this.cluster = cluster;
        return cluster.connect();
    }

    @Bean
    public MappingManager getManager(Session session) {
        return new MappingManager(session);
    }

    @Bean
    @BucketType(DAY)
    public Bucket dayBucket() {
        return new DayBucket();
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

    @Override
    public void destroy() throws Exception {
        cluster.close();
    }
}
