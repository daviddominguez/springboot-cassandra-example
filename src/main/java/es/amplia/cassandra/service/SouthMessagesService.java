package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.Page;
import es.amplia.cassandra.entity.SouthMessageByInterval;
import es.amplia.cassandra.entity.SouthMessageBySubjectInterval;
import es.amplia.cassandra.entity.SouthMessageBySubjectUserInterval;
import es.amplia.model.AuditMessage;

import java.util.Date;

public interface SouthMessagesService {

    void save(AuditMessage message);
    Page<SouthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState);
    Page<SouthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int pageSize);
    Page<SouthMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to, String pagingState);
    Page<SouthMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to, String pagingState, int pageSize);
    Page<SouthMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                             Date from, Date to, String pagingState);
    Page<SouthMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user, Date from,
                                                                             Date to, String pagingState, int pageSize);
}
