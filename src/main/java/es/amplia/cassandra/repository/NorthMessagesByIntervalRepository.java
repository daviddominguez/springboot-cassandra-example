package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.WeekBucket;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NorthMessagesByIntervalRepository extends AbstractRepository<NorthMessageByInterval> {

    @Autowired
    public NorthMessagesByIntervalRepository(MappingManager mappingManager) {
        super(mappingManager, NorthMessageByInterval.class, new WeekBucket());
    }
}
