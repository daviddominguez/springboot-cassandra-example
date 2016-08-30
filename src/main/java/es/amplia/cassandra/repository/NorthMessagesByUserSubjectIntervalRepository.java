package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.bucket.SemesterBucket;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class NorthMessagesByUserSubjectIntervalRepository extends AbstractRepository<NorthMessageByUserSubjectInterval> {

    private NorthMessagesByUserSubjectIntervalAccessor accessor;

    @Autowired
    public NorthMessagesByUserSubjectIntervalRepository(MappingManager mappingManager) {
        super(mappingManager, NorthMessageByUserSubjectInterval.class, new SemesterBucket());
        accessor = mappingManager.createAccessor(NorthMessagesByUserSubjectIntervalAccessor.class);
    }

    public Result<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectAndInterval(String user, String subject, Date from, Date to) {
        List<Long> interval = getInterval(from, to, 2);
        return accessor.getMessagesByUserSubjectAndInterval(user, subject, interval, from, to);
    }
}
