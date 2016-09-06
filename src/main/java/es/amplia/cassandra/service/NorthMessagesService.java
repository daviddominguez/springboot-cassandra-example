package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.cassandra.entity.Page;
import es.amplia.model.AuditMessage;

import java.util.Date;

public interface NorthMessagesService {

    void save(AuditMessage message);
    Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState);
    Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int pageSize);
    Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to, String pagingState);
    Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to, String pagingState, int pageSize);
    Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject,
                                                                             Date from, Date to, String pagingState);
    Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject, Date from,
                                                                             Date to, String pagingState, int pageSize);
}
