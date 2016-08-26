package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.Message;

/**
 * Created by david on 26/08/16.
 */
public interface Repository<T extends Message> {
    Statement saveQuery(T entity);

    T get(Object... id);

    Bucket getBucket();
}
