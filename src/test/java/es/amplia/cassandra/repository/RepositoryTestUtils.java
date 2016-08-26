package es.amplia.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import es.amplia.cassandra.entity.Message;

import java.text.ParseException;

import static es.amplia.cassandra.entity.Message.Names.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

class RepositoryTestUtils {

    @SuppressWarnings("unchecked")
    static Statement when_saved_into_repository(AbstractRepository repository, Message message) {
        return repository.saveQuery(message);
    }

    static Message given_a_persisted_message (Message messageToSave, Session session, AbstractRepository repository)
            throws ParseException {
        BoundStatement statement = (BoundStatement) when_saved_into_repository(repository, messageToSave);
        messageToSave.setAuditId(statement.getUUID(AUDIT_ID_FIELD));
        messageToSave.setInterval(statement.getLong(INTERVAL_FIELD));
        session.execute(statement);
        return messageToSave;
    }

    static void verify_message_parameters_are_correct(Message message, BoundStatement statement, String expectedQuery) {
        assertThat(statement.preparedStatement().getQueryString(), startsWith(expectedQuery));
        assertThat(statement.getString(TRANSACTION_ID_FIELD), equalTo(message.getTransactionId()));
        assertThat(statement.getString(SUBJECT_FIELD), equalTo(message.getSubject()));
        assertThat(statement.getLong(INTERVAL_FIELD), equalTo(message.getInterval()));
        assertThat(statement.getString(COMPONENT_TYPE_FIELD), equalTo(message.getComponentType().name()));
        assertThat(statement.getString(MSG_NAME_FIELD), equalTo(message.getMsgName().name()));
        assertThat(statement.getInt(MSG_SIZE_BYTES_FIELD), equalTo(message.getMsgSizeBytes()));
        assertThat(statement.getUUID(AUDIT_ID_FIELD), notNullValue());
        assertThat(statement.getString(SUBJECT_TYPE_FIELD), equalTo(message.getSubjectType().name()));
        assertThat(statement.getString(MSG_DIRECTION_FIELD), equalTo(message.getMsgDirection().name()));
        assertThat(statement.getTimestamp(OCCUR_TIME_FIELD), equalTo(message.getOccurTime()));
        assertThat(statement.getString(USER_FIELD), equalTo(message.getUser()));
        assertThat(statement.getMap(MSG_CONTEXT_FIELD, String.class, String.class), equalTo(message.getMsgContext()));
        assertThat(statement.getString(MSG_TYPE_FIELD), equalTo(message.getMsgType().name()));
        assertThat(statement.getString(SEQUENCE_ID_FIELD), equalTo(message.getSequenceId()));
        assertThat(statement.getString(MSG_STATUS_FIELD), equalTo(message.getMsgStatus().name()));
    }

    static void verify_both_messages_are_equal(Message persisted, Message queried) {
        assertThat(persisted.getInterval(), equalTo(queried.getInterval()));
        assertThat(persisted.getAuditId(), notNullValue());
        assertThat(persisted.getComponentType(), equalTo(queried.getComponentType()));
        assertThat(persisted.getTransactionId(), equalTo(queried.getTransactionId()));
        assertThat(persisted.getSubject(), equalTo(queried.getSubject()));
        assertThat(persisted.getMsgName(), equalTo(queried.getMsgName()));
        assertThat(persisted.getMsgSizeBytes(), equalTo(queried.getMsgSizeBytes()));
        assertThat(persisted.getSubjectType(), equalTo(queried.getSubjectType()));
        assertThat(persisted.getMsgDirection(), equalTo(queried.getMsgDirection()));
        assertThat(persisted.getOccurTime(), equalTo(queried.getOccurTime()));
        assertThat(persisted.getUser(), equalTo(queried.getUser()));
        assertThat(persisted.getMsgContext(), equalTo(queried.getMsgContext()));
        assertThat(persisted.getMsgType(), equalTo(queried.getMsgType()));
        assertThat(persisted.getSequenceId(), equalTo(queried.getSequenceId()));
        assertThat(persisted.getMsgStatus(), equalTo(queried.getMsgStatus()));
    }
}
