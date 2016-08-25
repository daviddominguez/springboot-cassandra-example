package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByInterval;

@Accessor
public interface NorthMessagesByIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_interval WHERE interval=:interval")
    Result<NorthMessageByInterval> getMessagesByInterval(@Param("interval") long interval);
}
