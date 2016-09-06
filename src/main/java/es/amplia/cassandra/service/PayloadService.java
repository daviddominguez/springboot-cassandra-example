package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.Payload;

import java.util.UUID;

public interface PayloadService {
    Payload getMessagePayload(UUID id);
}
