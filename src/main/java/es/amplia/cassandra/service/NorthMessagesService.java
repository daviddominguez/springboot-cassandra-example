package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.model.AuditMessage;

import java.util.Date;
import java.util.List;

public interface NorthMessagesService {

    void save(AuditMessage message);
    List<NorthMessageByInterval> getMessagesByInterval(Date from, Date to);
    List<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to);
    List<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject, Date from, Date to);
}
