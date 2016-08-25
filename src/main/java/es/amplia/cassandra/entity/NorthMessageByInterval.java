package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Table;
import es.amplia.model.AuditMessage;

@Table(keyspace = "audit", name="north_messages_by_interval")
public class NorthMessageByInterval extends AbstractMessage {

    public static class NorthMessageByIntervalBuilder extends AbstractMessageBuilder {

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByInterval();
        }

        @Override
        public NorthMessageByInterval build(AuditMessage auditMessage) {
            return (NorthMessageByInterval) super.build(auditMessage);
        }
    }
}
