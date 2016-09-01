package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.Page;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.WEEK;

@Repository
public class NorthMessagesByIntervalRepository extends AbstractRepository<NorthMessageByInterval> {

    private NorthMessagesByIntervalAccessor accessor;

    @Autowired
    public NorthMessagesByIntervalRepository(MappingManager mappingManager, @BucketType(WEEK) Bucket bucket) {
        super(mappingManager, NorthMessageByInterval.class, bucket);
        accessor = mappingManager.createAccessor(NorthMessagesByIntervalAccessor.class);
    }

    public Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState) {
        List<Long> partitions = getPartitions(from, to, 8);
        return getPagedResult(accessor.getMessagesByInterval(partitions, from, to), pagingState);
    }
}
