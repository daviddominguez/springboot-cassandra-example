package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.AlarmMessagesBySubjectUserIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.AlarmMessageBySubjectUserInterval;
import es.amplia.cassandra.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.MONTH;

@Repository
public class AlarmMessagesBySubjectUserIntervalRepository extends BucketRepository<AlarmMessageBySubjectUserInterval> {

    private AlarmMessagesBySubjectUserIntervalAccessor accessor;

    @Autowired
    public AlarmMessagesBySubjectUserIntervalRepository(MappingManager mappingManager,
                                                        @BucketType(MONTH) Bucket bucket) {
        super(mappingManager, AlarmMessageBySubjectUserInterval.class, bucket);
        accessor = mappingManager.createAccessor(AlarmMessagesBySubjectUserIntervalAccessor.class);
    }

    public Page<AlarmMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState,
                                                                                    Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 2);
        return getPagedResult(
                accessor.getMessagesBySubjectUserInterval(subject, user, partitions, from, to), pagingState, fetchSize);
    }
}
