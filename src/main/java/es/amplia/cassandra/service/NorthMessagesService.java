package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.*;
import es.amplia.model.AuditMessage;

import java.util.Date;
import java.util.UUID;

public interface NorthMessagesService {

    void save(AuditMessage message);
    Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState);
    Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to, String pagingState);
    Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject, Date from, Date to, String pagingState);
    Payload getMessagePayload(UUID id);
}
