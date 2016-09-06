package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.SOUTH_MESSAGES_BY_SUBJECT_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = SOUTH_MESSAGES_BY_SUBJECT_INTERVAL_TABLE)
public class SouthMessageBySubjectInterval extends AuditMessageEntity {

    @PartitionKey(1)
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    public static class SouthMessageBySubjectIntervalBuilder extends AuditMessageEntityBuilder {

        public static SouthMessageBySubjectIntervalBuilder builder() {
            return new SouthMessageBySubjectIntervalBuilder();
        }

        private SouthMessageBySubjectIntervalBuilder() {}

        @Override
        protected SouthMessageBySubjectInterval getEntity() {
            return (SouthMessageBySubjectInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new SouthMessageBySubjectInterval();
        }
    }
}
