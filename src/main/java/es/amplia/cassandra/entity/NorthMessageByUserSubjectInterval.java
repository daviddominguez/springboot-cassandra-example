package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.Message.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Message.Names.NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE)
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

        public static NorthMessageByUserSubjectIntervalBuilder builder() {
            return new NorthMessageByUserSubjectIntervalBuilder();
        }

        private NorthMessageByUserSubjectIntervalBuilder() {}

        @Override
        protected NorthMessageByUserSubjectInterval getMessage() {
            return (NorthMessageByUserSubjectInterval) super.getMessage();
        }

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByUserSubjectInterval();
        }
    }
}
