package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.cassandra.entity.Page;
import es.amplia.model.AuditMessage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;

public interface NorthMessagesService {

    void save(AuditMessage message);
    Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState);
    Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to, String pagingState);
    Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject, Date from, Date to, String pagingState);
}
