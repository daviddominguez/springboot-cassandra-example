package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;

import java.util.Date;
import java.util.List;

@Accessor
public interface NorthMessagesByUserSubjectIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_user_subject_and_interval WHERE user=:user AND subject=:subject AND interval IN :intervals and occur_time >= :fromDate and occur_time <= :toDate")
    Result<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectAndInterval(
            @Param("user") String user,
            @Param("subject") String subject,
            @Param("intervals")List<Long> intervals,
            @Param("fromDate") Date from,
            @Param("toDate") Date to);

}
