package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.ALARM_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = ALARM_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE)
public class AlarmMessageBySubjectUserInterval extends AuditMessageEntity {

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

    public static class AlarmMessageBySubjectUserIntervalBuilder extends AuditMessageEntityBuilder {

        public static AlarmMessageBySubjectUserIntervalBuilder builder() {
            return new AlarmMessageBySubjectUserIntervalBuilder();
        }

        private AlarmMessageBySubjectUserIntervalBuilder() {}

        @Override
        protected AlarmMessageBySubjectUserInterval getEntity() {
            return (AlarmMessageBySubjectUserInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new AlarmMessageBySubjectUserInterval();
        }
    }
}
