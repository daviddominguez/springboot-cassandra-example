package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static es.amplia.cassandra.entity.Message.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Message.Names.NORTH_MESSAGES_BY_USER_INTERVAL_TABLE;

@Table(keyspace = KEYSPACE, name = NORTH_MESSAGES_BY_USER_INTERVAL_TABLE)
public class NorthMessageByUserInterval extends AbstractMessage {

    @PartitionKey(1)
    @Override
    public String getUser() {
        return super.getUser();
    }

    public static class NorthMessageByUserIntervalBuilder extends AbstractMessageBuilder {

        public static NorthMessageByUserIntervalBuilder builder() {
            return new NorthMessageByUserIntervalBuilder();
        }

        private NorthMessageByUserIntervalBuilder() {}

        @Override
        protected NorthMessageByUserInterval getMessage() {
            return (NorthMessageByUserInterval) super.getMessage();
        }

        @Override
        protected AbstractMessage instantiateConcreteMessage() {
            return new NorthMessageByUserInterval();
        }
    }
}
