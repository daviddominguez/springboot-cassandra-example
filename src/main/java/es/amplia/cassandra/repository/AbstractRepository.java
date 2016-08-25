package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.Message;

import java.io.Serializable;

class AbstractRepository<T extends Message, ID extends Serializable> {

    private final Mapper<T> mapper;
    private final Bucket bucket;

    AbstractRepository(MappingManager mappingManager, Class<T> clazz, Bucket bucket) {
        this.mapper = mappingManager.mapper(clazz);
        this.bucket = bucket;
    }

    public Statement saveQuery(T entity) {
        entity.setAuditId(UUIDs.timeBased());
        entity.setInterval(bucket.getInterval(entity.getTimestamp()));
        return mapper.saveQuery(entity);
    }

    public T get(ID id) {
        return mapper.get(id);
    }

    public Statement deleteQuery(T entity) {
        return mapper.deleteQuery(entity);
    }

    public Bucket getBucket() {
        return bucket;
    }
}
