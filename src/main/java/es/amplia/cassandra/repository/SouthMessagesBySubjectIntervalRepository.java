package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.SouthMessagesBySubjectIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.Page;
import es.amplia.cassandra.entity.SouthMessageBySubjectInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.WEEK;

@Repository
public class SouthMessagesBySubjectIntervalRepository extends BucketRepository<SouthMessageBySubjectInterval> {

    private SouthMessagesBySubjectIntervalAccessor accessor;

    @Autowired
    public SouthMessagesBySubjectIntervalRepository(MappingManager mappingManager, @BucketType(WEEK) Bucket bucket) {
        super(mappingManager, SouthMessageBySubjectInterval.class, bucket);
        accessor = mappingManager.createAccessor(SouthMessagesBySubjectIntervalAccessor.class);
    }

    public Page<SouthMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to,
                                                                            String pagingState, Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 4);
        return getPagedResult(accessor.getMessagesBySubjectInterval(subject, partitions, from, to), pagingState, fetchSize);
    }
}
