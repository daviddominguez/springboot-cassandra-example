package es.amplia.cassandra.repository;

import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.BucketEntity;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

class BucketRepository<T extends BucketEntity> extends Repository<T> {

    private final Bucket bucket;

    BucketRepository(MappingManager mappingManager, Class<T> clazz, Bucket bucket) {
        super(mappingManager, clazz);
        this.bucket = bucket;
    }

    @Override
    public Statement saveQuery(T entity) {
        entity.setInterval(bucket.getInterval(entity.getOccurTime()));
        return super.saveQuery(entity);
    }

    List<Long> getPartitions(Date from, Date to, int maxSize) {
        List<Long> partitions = bucket.getIntervals(from, to);
        checkArgument(partitions.size() < maxSize, "specified time range is too big, be more specific");
        return partitions;
    }
}
