package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE)
public class NorthMessageByUserSubjectInterval extends AuditMessageEntity {

    @PartitionKey(1)
    @Override
    public String getUser() {
        return super.getUser();
    }

    @PartitionKey(2)
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    public static class NorthMessageByUserSubjectIntervalBuilder extends AuditMessageEntityBuilder {

        public static NorthMessageByUserSubjectIntervalBuilder builder() {
            return new NorthMessageByUserSubjectIntervalBuilder();
        }

        private NorthMessageByUserSubjectIntervalBuilder() {}

        @Override
        protected NorthMessageByUserSubjectInterval getEntity() {
            return (NorthMessageByUserSubjectInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new NorthMessageByUserSubjectInterval();
        }
    }
}
