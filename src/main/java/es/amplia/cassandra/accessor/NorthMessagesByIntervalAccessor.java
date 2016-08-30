package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByInterval;

import java.util.Date;
import java.util.List;

@Accessor
public interface NorthMessagesByIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_interval WHERE interval IN :intervals and occur_time >= :fromDate and occur_time <= :toDate")
    Result<NorthMessageByInterval> getMessagesByInterval(
            @Param("intervals")List<Long> intervals,
            @Param("fromDate") Date from,
            @Param("toDate") Date to);
}
