package es.amplia.cassandra.accessor;

import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;

import java.util.Date;
import java.util.List;

@Accessor
public interface SouthMessagesBySubjectIntervalAccessor {

    @Query("SELECT * FROM audit.south_messages_by_subject_and_interval WHERE subject=:subject AND interval IN :intervals and occur_time >= :fromDate and occur_time <= :toDate")
    @QueryParameters(idempotent = true)
    Statement getMessagesBySubjectInterval(
            @Param("subject") String subject,
            @Param("intervals") List<Long> intervals,
            @Param("fromDate") Date from,
            @Param("toDate") Date to);
}
