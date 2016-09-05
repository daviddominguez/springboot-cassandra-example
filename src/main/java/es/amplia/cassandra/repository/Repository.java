package es.amplia.cassandra.repository;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import es.amplia.cassandra.entity.Entity;
import es.amplia.cassandra.entity.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Repository<T extends Entity> {

    private final Mapper<T> mapper;
    private final Session session;

    Repository(MappingManager mappingManager, Class<T> clazz) {
        this.mapper = mappingManager.mapper(clazz);
        this.session = mappingManager.getSession();
    }

    public Statement saveQuery(T entity) {
        entity.setId(UUID.randomUUID());
        return mapper.saveQuery(entity);
    }

    public T get(Object... id) {
        return mapper.get(id);
    }

    Page<T> getPagedResult(Statement statement, String previousPagingState, Integer fetchSize) {
        if (fetchSize != null) {
            statement.setFetchSize(fetchSize);
        }
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
