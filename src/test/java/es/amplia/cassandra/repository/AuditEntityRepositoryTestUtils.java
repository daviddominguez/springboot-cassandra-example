package es.amplia.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.entity.AuditMessageEntity;

import java.text.ParseException;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

class AuditEntityRepositoryTestUtils {

    @SuppressWarnings("unchecked")
    static BoundStatement when_saved_into_repository(BucketRepository repository, AuditMessageEntity auditMessageEntity) {
        return (BoundStatement) repository.saveQuery(auditMessageEntity);
    }

    static AuditMessageEntity given_a_persisted_message (AuditMessageEntity auditMessageEntityToSave, Session session, BucketRepository repository) {
        BoundStatement statement = when_saved_into_repository(repository, auditMessageEntityToSave);
        auditMessageEntityToSave.setId(statement.getUUID(ID_FIELD));
        auditMessageEntityToSave.setInterval(statement.getLong(INTERVAL_FIELD));
        session.execute(statement);
        return auditMessageEntityToSave;
    }

    static void verify_insert_query_is_well_formed(AuditMessageEntity auditMessageEntity, BoundStatement statement, String expectedQuery) {
        assertThat(statement.preparedStatement().getQueryString(), startsWith(expectedQuery));
        assertThat(statement.getString(LOCAL_CORRELATION_ID_FIELD), equalTo(auditMessageEntity.getLocalCorrelationId()));
        assertThat(statement.getString(GLOBAL_CORRELATION_ID_FIELD), equalTo(auditMessageEntity.getGlobalCorrelationId()));
        assertThat(statement.getString(SUBJECT_FIELD), equalTo(auditMessageEntity.getSubject()));
        assertThat(statement.getLong(INTERVAL_FIELD), equalTo(auditMessageEntity.getInterval()));
        assertThat(statement.getString(COMPONENT_TYPE_FIELD), equalTo(auditMessageEntity.getComponentType().name()));
        assertThat(statement.getString(MSG_NAME_FIELD), equalTo(auditMessageEntity.getMsgName().name()));
        assertThat(statement.getInt(MSG_SIZE_BYTES_FIELD), equalTo(auditMessageEntity.getMsgSizeBytes()));
        assertThat(statement.getUUID(ID_FIELD), notNullValue());
        assertThat(statement.getString(SUBJECT_TYPE_FIELD), equalTo(auditMessageEntity.getSubjectType().name()));
        assertThat(statement.getString(MSG_DIRECTION_FIELD), equalTo(auditMessageEntity.getMsgDirection().name()));
        assertThat(statement.getTimestamp(OCCUR_TIME_FIELD), equalTo(auditMessageEntity.getOccurTime()));
        assertThat(statement.getString(USER_FIELD), equalTo(auditMessageEntity.getUser()));
        assertThat(statement.getMap(MSG_CONTEXT_FIELD, String.class, String.class), equalTo(auditMessageEntity.getMsgContext()));
        assertThat(statement.getString(MSG_TYPE_FIELD), equalTo(auditMessageEntity.getMsgType().name()));
        assertThat(statement.getString(SEQUENCE_ID_FIELD), equalTo(auditMessageEntity.getSequenceId()));
        assertThat(statement.getString(MSG_STATUS_FIELD), equalTo(auditMessageEntity.getMsgStatus().name()));
        assertThat(statement.getBool(SECURED_FIELD), equalTo(auditMessageEntity.getSecured()));
        assertThat(statement.getUUID(PAYLOAD_ID_FIELD), equalTo(auditMessageEntity.getPayloadId()));
    }

    static void verify_both_messages_are_equal(AuditMessageEntity persisted, AuditMessageEntity queried) {
        assertThat(persisted, equalTo(queried));
    }
}
