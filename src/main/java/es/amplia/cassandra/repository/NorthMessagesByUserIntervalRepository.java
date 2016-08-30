package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import es.amplia.cassandra.accessor.NorthMessagesByUserIntervalAccessor;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.bucket.BucketType;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static es.amplia.cassandra.bucket.Bucket.Type.MONTH;

@Repository
public class NorthMessagesByUserIntervalRepository extends AbstractRepository<NorthMessageByUserInterval> {

    private NorthMessagesByUserIntervalAccessor accessor;

    @Autowired
    public NorthMessagesByUserIntervalRepository(MappingManager mappingManager, @BucketType(MONTH) Bucket bucket) {
        super(mappingManager, NorthMessageByUserInterval.class, bucket);
        accessor = mappingManager.createAccessor(NorthMessagesByUserIntervalAccessor.class);
    }

    public Result<NorthMessageByUserInterval> getMessagesByUserAndInterval(String user, Date from, Date to) {
        List<Long> interval = getInterval(from, to, 4);
        return accessor.getMessagesByUserAndInterval(user, interval, from, to);
    }
}
