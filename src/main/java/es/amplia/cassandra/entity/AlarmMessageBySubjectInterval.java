package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.ALARM_MESSAGES_BY_SUBJECT_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = ALARM_MESSAGES_BY_SUBJECT_INTERVAL_TABLE)
public class AlarmMessageBySubjectInterval extends AuditMessageEntity {

    @PartitionKey(1)
    @Override
    public String getSubject() {
        return super.getSubject();
    }

    public static class AlarmMessageBySubjectIntervalBuilder extends AuditMessageEntityBuilder {

        public static AlarmMessageBySubjectIntervalBuilder builder() {
            return new AlarmMessageBySubjectIntervalBuilder();
        }

        private AlarmMessageBySubjectIntervalBuilder() {}

        @Override
        protected AlarmMessageBySubjectInterval getEntity() {
            return (AlarmMessageBySubjectInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new AlarmMessageBySubjectInterval();
        }
    }
}
