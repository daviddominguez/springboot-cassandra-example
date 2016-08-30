package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.Message;

public interface Repository<T extends Message> {
    Statement saveQuery(T entity);

    T get(Object... id);
}
