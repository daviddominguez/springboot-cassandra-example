package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;

import java.util.List;

@Accessor
public interface NorthMessagesByUserSubjectIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_user_subject_and_interval WHERE user=:user AND subject=:subject AND interval=:interval")
    Result<NorthMessageByUserSubjectInterval> getMessagesByUserAndSubjectAndInterval(
            @Param("user") String user,
            @Param("subject") String subject,
            @Param("interval") long interval);

    @Query("SELECT * FROM audit.north_messages_by_user_subject_and_interval WHERE user=:user AND subject=:subject AND interval IN :intervals")
    Result<NorthMessageByUserSubjectInterval> getMessagesByUserAndSubjectAndIntervalList(
            @Param("user") String user,
            @Param("subject") String subject,
            @Param("intervals")List<Long> intervals);

}
