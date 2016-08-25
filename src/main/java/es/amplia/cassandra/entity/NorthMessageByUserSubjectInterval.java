package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import es.amplia.model.AuditMessage;

@Table(keyspace = "audit", name="north_messages_by_user_subject_and_interval")
public class NorthMessageByUserSubjectInterval extends AbstractMessage {

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

    public static class NorthMessageByUserSubjectIntervalBuilder extends AbstractMessageBuilder {

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByUserSubjectInterval();
        }

        @Override
        public NorthMessageByUserSubjectInterval build(AuditMessage auditMessage) {
            return (NorthMessageByUserSubjectInterval) super.build(auditMessage);
        }
    }
}
