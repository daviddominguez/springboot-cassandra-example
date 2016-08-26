package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Table;
import es.amplia.model.AuditMessage;

import static es.amplia.cassandra.entity.Message.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Message.Names.NORTH_MESSAGES_BY_INTERVAL_TABLE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_INTERVAL_TABLE)
public class NorthMessageByInterval extends AbstractMessage {

    public static class NorthMessageByIntervalBuilder extends AbstractMessageBuilder {

        public static NorthMessageByIntervalBuilder builder() {
            return new NorthMessageByIntervalBuilder();
        }

        private NorthMessageByIntervalBuilder() {}

        @Override
        protected NorthMessageByInterval getMessage() {
            return (NorthMessageByInterval) super.getMessage();
        }

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByInterval();
        }
    }
}
