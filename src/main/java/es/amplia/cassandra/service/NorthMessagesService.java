package es.amplia.cassandra.service;

import es.amplia.model.AuditMessage;

public interface NorthMessagesService {

    void save(AuditMessage message);
}
