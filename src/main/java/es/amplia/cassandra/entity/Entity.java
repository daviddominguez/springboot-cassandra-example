package es.amplia.cassandra.entity;

import java.util.UUID;

public interface Entity {

    interface Names {
        public final static String KEYSPACE = "audit";
    }

    UUID getId();
    void setId(UUID id);
}
