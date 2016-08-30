package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.Message;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

class AbstractRepository<T extends Message> implements Repository<T> {

    private final Mapper<T> mapper;
    private final Bucket bucket;

    AbstractRepository(MappingManager mappingManager, Class<T> clazz, Bucket bucket) {
        this.mapper = mappingManager.mapper(clazz);
        this.bucket = bucket;
    }

    @Override
    public Statement saveQuery(T entity) {
        entity.setAuditId(UUIDs.timeBased());
        entity.setInterval(bucket.getInterval(entity.getOccurTime()));
        return mapper.saveQuery(entity);
    }

    @Override
    public T get(Object... id) {
        return mapper.get(id);
    }

    List<Long> getInterval(Date from, Date to, int maxSize) {
        List<Long> intervals = bucket.getIntervals(from, to);
        checkArgument(intervals.size() < maxSize, "specified time range is too big, be more specific");
        return intervals;
    }
}
