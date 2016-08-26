package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.MonthBucket;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class NorthMessagesByUserIntervalRepository extends AbstractRepository<NorthMessageByUserInterval> {

    @Autowired
    public NorthMessagesByUserIntervalRepository(MappingManager mappingManager) {
        super(mappingManager, NorthMessageByUserInterval.class, new MonthBucket());
    }
}
