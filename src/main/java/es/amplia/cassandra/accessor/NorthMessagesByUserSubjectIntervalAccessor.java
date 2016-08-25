package es.amplia.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;

@Accessor
public interface NorthMessagesByUserSubjectIntervalAccessor {

    @Query("SELECT * FROM audit.north_messages_by_user_subject_and_interval WHERE user=:user AND subject=:subject AND interval=:interval")
    Result<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(@Param("user") String user,
                                                                               @Param("subject") String subject,
                                                                               @Param("interval") long interval);
}
