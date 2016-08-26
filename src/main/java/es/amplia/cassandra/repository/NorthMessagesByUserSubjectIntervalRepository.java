package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.SemesterBucket;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class NorthMessagesByUserSubjectIntervalRepository extends AbstractRepository<NorthMessageByUserSubjectInterval> {

    @Autowired
    public NorthMessagesByUserSubjectIntervalRepository(MappingManager mappingManager) {
        super(mappingManager, NorthMessageByUserSubjectInterval.class, new SemesterBucket());
    }
}
