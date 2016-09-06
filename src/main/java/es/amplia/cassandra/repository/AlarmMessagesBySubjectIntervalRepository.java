package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.AlarmMessagesBySubjectIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.AlarmMessageBySubjectInterval;
import es.amplia.cassandra.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.WEEK;

@Repository
public class AlarmMessagesBySubjectIntervalRepository extends BucketRepository<AlarmMessageBySubjectInterval> {

    private AlarmMessagesBySubjectIntervalAccessor accessor;

    @Autowired
    public AlarmMessagesBySubjectIntervalRepository(MappingManager mappingManager, @BucketType(WEEK) Bucket bucket) {
        super(mappingManager, AlarmMessageBySubjectInterval.class, bucket);
        accessor = mappingManager.createAccessor(AlarmMessagesBySubjectIntervalAccessor.class);
    }

    public Page<AlarmMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to,
                                                                            String pagingState, Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 4);
        return getPagedResult(accessor.getMessagesBySubjectInterval(subject, partitions, from, to), pagingState, fetchSize);
    }
}
