package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;

import java.util.List;

@Accessor
public interface NorthMessagesByUserIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_user_and_interval WHERE user=:user AND interval=:interval")
    Result<NorthMessageByUserInterval> getMessagesByUserAndInterval(
            @Param("user") String user,
            @Param("interval") long interval);

    @Query("SELECT * FROM audit.north_messages_by_interval WHERE user=:user AND interval IN :intervals")
    Result<NorthMessageByInterval> getMessagesByUserAndIntervalList(
            @Param("user") String user,
            @Param("intervals")List<Long> intervals);

}
