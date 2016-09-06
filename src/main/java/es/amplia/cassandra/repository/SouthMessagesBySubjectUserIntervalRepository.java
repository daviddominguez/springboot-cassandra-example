package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.SouthMessagesBySubjectUserIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.Page;
import es.amplia.cassandra.entity.SouthMessageBySubjectUserInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.MONTH;

@Repository
public class SouthMessagesBySubjectUserIntervalRepository extends BucketRepository<SouthMessageBySubjectUserInterval> {

    private SouthMessagesBySubjectUserIntervalAccessor accessor;

    @Autowired
    public SouthMessagesBySubjectUserIntervalRepository(MappingManager mappingManager,
                                                        @BucketType(MONTH) Bucket bucket) {
        super(mappingManager, SouthMessageBySubjectUserInterval.class, bucket);
        accessor = mappingManager.createAccessor(SouthMessagesBySubjectUserIntervalAccessor.class);
    }

    public Page<SouthMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState,
                                                                                    Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 2);
        return getPagedResult(
                accessor.getMessagesBySubjectUserInterval(subject, user, partitions, from, to), pagingState, fetchSize);
    }
}
