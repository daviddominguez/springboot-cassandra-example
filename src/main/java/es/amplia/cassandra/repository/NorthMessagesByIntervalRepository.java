package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.bucket.WeekBucket;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class NorthMessagesByIntervalRepository extends AbstractRepository<NorthMessageByInterval> {

    private NorthMessagesByIntervalAccessor accessor;

    @Autowired
    public NorthMessagesByIntervalRepository(MappingManager mappingManager) {
        super(mappingManager, NorthMessageByInterval.class, new WeekBucket());
        accessor = mappingManager.createAccessor(NorthMessagesByIntervalAccessor.class);
    }

    public Result<NorthMessageByInterval> getMessagesByInterval(Date from, Date to) {
        return accessor.getMessagesByInterval(getInterval(from, to, 8), from, to);
    }
}
