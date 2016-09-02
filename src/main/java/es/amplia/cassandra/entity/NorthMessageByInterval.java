package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.NORTH_MESSAGES_BY_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_INTERVAL_TABLE)
public class NorthMessageByInterval extends AuditMessageEntity {

    public static class NorthMessageByIntervalBuilder extends AuditMessageEntityBuilder {

        public static NorthMessageByIntervalBuilder builder() {
            return new NorthMessageByIntervalBuilder();
        }

        private NorthMessageByIntervalBuilder() {}

        @Override
        protected NorthMessageByInterval getEntity() {
            return (NorthMessageByInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new NorthMessageByInterval();
        }
    }
}
