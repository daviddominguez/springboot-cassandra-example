package es.amplia.cassandra.repository;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import es.amplia.cassandra.bucket.Bucket;
import es.amplia.cassandra.entity.Message;
import es.amplia.cassandra.entity.Page;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

class AbstractRepository<T extends Message> implements Repository<T> {

    private final Mapper<T> mapper;
    private final Session session;
    private final Bucket bucket;

    AbstractRepository(MappingManager mappingManager, Class<T> clazz, Bucket bucket) {
        this.mapper = mappingManager.mapper(clazz);
        this.session = mappingManager.getSession();
        this.bucket = bucket;
    }

    @Override
    public Statement saveQuery(T entity) {
        entity.setAuditId(UUID.randomUUID());
        entity.setInterval(bucket.getInterval(entity.getOccurTime()));
        return mapper.saveQuery(entity);
    }

    @Override
    public T get(Object... id) {
        return mapper.get(id);
    }

    List<Long> getPartitions(Date from, Date to, int maxSize) {
        List<Long> partitions = bucket.getIntervals(from, to);
        checkArgument(partitions.size() < maxSize, "specified time range is too big, be more specific");
        return partitions;
    }

    Page<T> getPagedResult(Statement statement, String previousPagingState) {
        if (previousPagingState != null ) {
            statement.setPagingState(PagingState.fromString(previousPagingState));
        }
        Result<T> result = mapper.map(session.execute(statement));
        PagingState nextPage = result.getExecutionInfo().getPagingState();
        int remaining = result.getAvailableWithoutFetching();
        List<T> fetchedRows = new ArrayList<>();
        for (T row : result) {
            fetchedRows.add(row);
            if (--remaining == 0) {
                break;
            }
        }
        return new Page<>(nextPage != null ? nextPage.toString() : null, fetchedRows);
    }
}
