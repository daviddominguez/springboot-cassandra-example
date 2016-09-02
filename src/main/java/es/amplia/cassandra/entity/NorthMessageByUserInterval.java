package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.NORTH_MESSAGES_BY_USER_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_USER_INTERVAL_TABLE)
public class NorthMessageByUserInterval extends AuditMessageEntity {

    @PartitionKey(1)
    @Override
    public String getUser() {
        return super.getUser();
    }

    public static class NorthMessageByUserIntervalBuilder extends AuditMessageEntityBuilder {

        public static NorthMessageByUserIntervalBuilder builder() {
            return new NorthMessageByUserIntervalBuilder();
        }

        private NorthMessageByUserIntervalBuilder() {}

        @Override
        protected NorthMessageByUserInterval getEntity() {
            return (NorthMessageByUserInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new NorthMessageByUserInterval();
        }
    }
}
