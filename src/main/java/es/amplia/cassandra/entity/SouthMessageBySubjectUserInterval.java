package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.SOUTH_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = SOUTH_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE)
public class SouthMessageBySubjectUserInterval extends AuditMessageEntity {

    @PartitionKey(1)
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    @PartitionKey(2)
    @Override
    public String getUser() {
        return super.getUser();
    }

    public static class SouthMessageBySubjectUserIntervalBuilder extends AuditMessageEntityBuilder {

        public static SouthMessageBySubjectUserIntervalBuilder builder() {
            return new SouthMessageBySubjectUserIntervalBuilder();
        }

        private SouthMessageBySubjectUserIntervalBuilder() {}

        @Override
        protected SouthMessageBySubjectUserInterval getEntity() {
            return (SouthMessageBySubjectUserInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new SouthMessageBySubjectUserInterval();
        }
    }
}
