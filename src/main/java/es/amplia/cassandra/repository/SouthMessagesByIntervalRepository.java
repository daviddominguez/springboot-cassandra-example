package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.SouthMessagesByIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.Page;
import es.amplia.cassandra.entity.SouthMessageByInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.DAY;

@Repository
public class SouthMessagesByIntervalRepository extends BucketRepository<SouthMessageByInterval> {

    private SouthMessagesByIntervalAccessor accessor;

    @Autowired
    public SouthMessagesByIntervalRepository(MappingManager mappingManager, @BucketType(DAY) Bucket bucket) {
        super(mappingManager, SouthMessageByInterval.class, bucket);
        accessor = mappingManager.createAccessor(SouthMessagesByIntervalAccessor.class);
    }

    public Page<SouthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 8);
        return getPagedResult(accessor.getMessagesByInterval(partitions, from, to), pagingState, fetchSize);
    }
}
