package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import es.amplia.model.AuditMessage;

@Table(keyspace = "audit", name="north_messages_by_user_and_interval")
public class NorthMessageByUserInterval extends AbstractMessage {

    @PartitionKey(1)
    @Override
    public String getUser() {
        return super.getUser();
    }

    public static class NorthMessageByUserIntervalBuilder extends AbstractMessageBuilder {

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByUserInterval();
        }

        @Override
        public NorthMessageByUserInterval build(AuditMessage auditMessage) {
            return (NorthMessageByUserInterval) super.build(auditMessage);
        }
    }
}
