package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.AlarmMessageByInterval;
import es.amplia.cassandra.entity.AlarmMessageBySubjectInterval;
import es.amplia.cassandra.entity.AlarmMessageBySubjectUserInterval;
import es.amplia.cassandra.entity.Page;
import es.amplia.model.AuditMessage;

import java.util.Date;

public interface AlarmMessagesService {

    void save(AuditMessage message);
    Page<AlarmMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState);
    Page<AlarmMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int pageSize);
    Page<AlarmMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to, String pagingState);
    Page<AlarmMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to, String pagingState, int pageSize);
    Page<AlarmMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                             Date from, Date to, String pagingState);
    Page<AlarmMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user, Date from,
                                                                             Date to, String pagingState, int pageSize);
}
