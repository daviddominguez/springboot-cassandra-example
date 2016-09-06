package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.SOUTH_MESSAGES_BY_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = SOUTH_MESSAGES_BY_INTERVAL_TABLE)
public class SouthMessageByInterval extends AuditMessageEntity {

    public static class SouthMessageByIntervalBuilder extends AuditMessageEntityBuilder {

        public static SouthMessageByIntervalBuilder builder() {
            return new SouthMessageByIntervalBuilder();
        }

        private SouthMessageByIntervalBuilder() {}

        @Override
        protected SouthMessageByInterval getEntity() {
            return (SouthMessageByInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new SouthMessageByInterval();
        }
    }
}
