package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;

@Accessor
public interface NorthMessagesByUserIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_user_and_interval WHERE user=:user AND interval=:interval")
    Result<NorthMessageByUserInterval> getMessagesByUserInterval(@Param("user") String user, @Param("interval") long interval);
}
