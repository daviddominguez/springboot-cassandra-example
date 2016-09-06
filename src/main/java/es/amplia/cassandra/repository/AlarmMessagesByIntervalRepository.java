package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.AlarmMessagesByIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.AlarmMessageByInterval;
import es.amplia.cassandra.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.DAY;

@Repository
public class AlarmMessagesByIntervalRepository extends BucketRepository<AlarmMessageByInterval> {

    private AlarmMessagesByIntervalAccessor accessor;

    @Autowired
    public AlarmMessagesByIntervalRepository(MappingManager mappingManager, @BucketType(DAY) Bucket bucket) {
        super(mappingManager, AlarmMessageByInterval.class, bucket);
        accessor = mappingManager.createAccessor(AlarmMessagesByIntervalAccessor.class);
    }

    public Page<AlarmMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, Integer fetchSize) {
        List<Long> partitions = getPartitions(from, to, 8);
        return getPagedResult(accessor.getMessagesByInterval(partitions, from, to), pagingState, fetchSize);
    }
}
