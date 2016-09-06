package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.ALARM_MESSAGES_BY_INTERVAL_TABLE;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;

@Table(keyspace = KEYSPACE, name = ALARM_MESSAGES_BY_INTERVAL_TABLE)
public class AlarmMessageByInterval extends AuditMessageEntity {

    public static class AlarmMessageByIntervalBuilder extends AuditMessageEntityBuilder {

        public static AlarmMessageByIntervalBuilder builder() {
            return new AlarmMessageByIntervalBuilder();
        }

        private AlarmMessageByIntervalBuilder() {}

        @Override
        protected AlarmMessageByInterval getEntity() {
            return (AlarmMessageByInterval) super.getEntity();
        }

        @Override
        protected AuditMessageEntity instantiateConcreteAuditMessageEntity() {
            return new AlarmMessageByInterval();
        }
    }
}
