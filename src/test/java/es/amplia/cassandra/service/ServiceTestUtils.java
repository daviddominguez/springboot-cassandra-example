package es.amplia.cassandra.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.entity.AuditMessageEntity;
import es.amplia.cassandra.entity.Payload;
import es.amplia.model.AuditMessage;
import es.amplia.model.builder.AuditMessageBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Payload.Names.PAYLOAD_BY_ID_TABLE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

class ServiceTestUtils {

    private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy'T'H:mm:ss.SSS");

    static List<AuditMessage> given_a_collection_of_auditMessages(List<String> dates, int num) throws ParseException {
        List<AuditMessage> auditMessages = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            auditMessages.add(given_an_auditMessage(format.parse(dates.get(new Random().nextInt(dates.size())))));
        }
        return auditMessages;
    }

    static List<String> given_a_list_of_users() {
        return asList("user_1", "user_2", "user_3");
    }

    static List<String> given_a_list_of_subjects() {
        return asList("subject_1", "subject_2", "subject_3", "subject_4", "subject_5");
    }

    private static List<String> given_a_list_of_local_correlation_ids() {
        return asList("local_correlationId_1", "local_correlationId_2", "local_correlationId_3");
    }

    private static List<String> given_a_list_of_global_correlation_ids() {
        return asList("global_correlationId_1", "global_correlationId_2", "global_correlationId_3");
    }

    private static List<String> given_a_list_of_sequence_ids() {
        return asList("sequenceId_1", "sequenceId_2", "sequenceId_3");
    }

    static AuditMessage given_an_auditMessage(Date date) {
        List<String> users = given_a_list_of_users();
        List<String> subjects = given_a_list_of_subjects();
        List<String> localCorrelationIds = given_a_list_of_local_correlation_ids();
        List<String> globalCorrelationIds = given_a_list_of_global_correlation_ids();
        List<String> sequenceIds = given_a_list_of_sequence_ids();

        return AuditMessageBuilder.builder()
                .process(asList(AuditMessage.ProcessType.values()).get(new Random().nextInt(AuditMessage.ProcessType.values().length)))
                .component(asList(AuditMessage.ComponentType.values()).get(new Random().nextInt(AuditMessage.ComponentType.values().length)))
                .msgName(asList(AuditMessage.NameType.values()).get(new Random().nextInt(AuditMessage.NameType.values().length)))
                .msgType(asList(AuditMessage.MsgType.values()).get(new Random().nextInt(AuditMessage.MsgType.values().length)))
                .msgDirection(asList(AuditMessage.MsgDirection.values()).get(new Random().nextInt(AuditMessage.MsgDirection.values().length)))
                .subject(subjects.get(new Random().nextInt(subjects.size())))
                .subjectType(asList(AuditMessage.SubjectType.values()).get(new Random().nextInt(AuditMessage.SubjectType.values().length)))
                .user(users.get(new Random().nextInt(users.size())))
                .localCorrelationId(localCorrelationIds.get(new Random().nextInt(localCorrelationIds.size())))
                .globalCorrelationId(globalCorrelationIds.get(new Random().nextInt(globalCorrelationIds.size())))
                .sequenceId(sequenceIds.get(new Random().nextInt(sequenceIds.size())))
                .msgStatus(asList(AuditMessage.MsgStatus.values()).get(new Random().nextInt(AuditMessage.MsgStatus.values().length)))
                .secured(new Random().nextBoolean())
                .msgSizeBytes(100)
                .msgContext(singletonMap("context_key", "context_value"))
                .msgPayload(asList("payload_value_1", "payload_value_2"))
                .timestamp(date)
                .version(1)
                .build();
    }

    static void verify_abstractMessage_has_all_expected_values(Session session, AuditMessageEntity auditMessageEntity) {
        assertThat(auditMessageEntity.getInterval(), notNullValue());
        assertThat(auditMessageEntity.getId(), notNullValue());
        assertThat(auditMessageEntity.getComponentType(), isIn(AuditMessage.ComponentType.values()));
        assertThat(auditMessageEntity.getMsgName(), isIn(AuditMessage.NameType.values()));
        assertThat(auditMessageEntity.getMsgType(), isIn(AuditMessage.MsgType.values()));
        assertThat(auditMessageEntity.getMsgDirection(), isIn(AuditMessage.MsgDirection.values()));
        assertThat(auditMessageEntity.getSubject(), anyOf(isIn(given_a_list_of_subjects()), equalTo("subjectA")));
        assertThat(auditMessageEntity.getSubjectType(), isIn(AuditMessage.SubjectType.values()));
        assertThat(auditMessageEntity.getUser(), anyOf(isIn(given_a_list_of_users()), equalTo("userA")));
        assertThat(auditMessageEntity.getLocalCorrelationId(), isIn(given_a_list_of_local_correlation_ids()));
        assertThat(auditMessageEntity.getGlobalCorrelationId(), isIn(given_a_list_of_global_correlation_ids()));
        assertThat(auditMessageEntity.getSequenceId(), isIn(given_a_list_of_sequence_ids()));
        assertThat(auditMessageEntity.getMsgStatus(), isIn(AuditMessage.MsgStatus.values()));
        assertThat(auditMessageEntity.getSecured(), notNullValue());
        assertThat(auditMessageEntity.getMsgSizeBytes(), is(100));
        assertThat(auditMessageEntity.getMsgContext(), hasKey("context_key"));
        assertThat(auditMessageEntity.getMsgContext(), hasValue("context_value"));
        assertThat(auditMessageEntity.getPayloadId(), notNullValue());
        assertThat(auditMessageEntity.getOccurTime(), notNullValue());
        verify_payload_row_exists(session, auditMessageEntity.getPayloadId());
    }

    static void verify_payload_row_exists(Session session, UUID id) {
        List<Row> payload = session.execute(select().all()
                .from(KEYSPACE, PAYLOAD_BY_ID_TABLE)
                .where(eq(Payload.Names.ID_FIELD, id))).all();
        assertThat(payload, notNullValue());
        assertThat(payload, hasSize(1));
    }
}
