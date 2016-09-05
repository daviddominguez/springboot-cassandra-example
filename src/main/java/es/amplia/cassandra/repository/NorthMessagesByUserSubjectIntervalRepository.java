package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.cassandra.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.SEMESTER;

@Repository
public class NorthMessagesByUserSubjectIntervalRepository extends BucketRepository<NorthMessageByUserSubjectInterval> {

    private NorthMessagesByUserSubjectIntervalAccessor accessor;

    @Autowired
    public NorthMessagesByUserSubjectIntervalRepository(MappingManager mappingManager,
                                                        @BucketType(SEMESTER) Bucket bucket) {
        super(mappingManager, NorthMessageByUserSubjectInterval.class, bucket);
        accessor = mappingManager.createAccessor(NorthMessagesByUserSubjectIntervalAccessor.class);
    }

    public Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject,
                                                                                    Date from, Date to,
                                                                                    String pagingState,
                                                                                    Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 2);
        return getPagedResult(
                accessor.getMessagesByUserSubjectInterval(user, subject, partitions, from, to), pagingState, fetchSize);
    }
}
