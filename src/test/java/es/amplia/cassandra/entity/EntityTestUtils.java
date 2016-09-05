package es.amplia.cassandra.entity;

import es.amplia.model.AuditMessage;
import es.amplia.model.builder.AuditMessageBuilder;

import java.util.Date;

import static es.amplia.model.AuditMessage.ComponentType.WEBSOCKET;
import static es.amplia.model.AuditMessage.MsgDirection.IN;
import static es.amplia.model.AuditMessage.MsgStatus.SUCCESS;
import static es.amplia.model.AuditMessage.MsgType.REQUEST;
import static es.amplia.model.AuditMessage.NameType.OPERATION;
import static es.amplia.model.AuditMessage.ProcessType.REST_NORTH;
import static es.amplia.model.AuditMessage.SubjectType.DEVICE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

class EntityTestUtils {

    static void verify_auditMessage_and_entity_has_the_same_data(AuditMessage auditMessage, AuditMessageEntity messageEntity) {
        assertThat(auditMessage.getComponent(), is(messageEntity.getComponentType()));
        assertThat(auditMessage.getMsgName(), is(messageEntity.getMsgName()));
        assertThat(auditMessage.getMsgType(), is(messageEntity.getMsgType()));
        assertThat(auditMessage.getMsgDirection(), is(messageEntity.getMsgDirection()));
        assertThat(auditMessage.getSubject(), is(messageEntity.getSubject()));
        assertThat(auditMessage.getSubjectType(), is(messageEntity.getSubjectType()));
        assertThat(auditMessage.getUser(), is(messageEntity.getUser()));
        assertThat(auditMessage.getLocalCorrelationId(), is(messageEntity.getLocalCorrelationId()));
        assertThat(auditMessage.getGlobalCorrelationId(), is(messageEntity.getGlobalCorrelationId()));
        assertThat(auditMessage.getSequenceId(), is(messageEntity.getSequenceId()));
        assertThat(auditMessage.getMsgStatus(), is(messageEntity.getMsgStatus()));
        assertThat(auditMessage.getSecured(), is(messageEntity.getSecured()));
        assertThat(auditMessage.getMsgSizeBytes(), is(messageEntity.getMsgSizeBytes()));
        assertThat(auditMessage.getMsgContext(), is(messageEntity.getMsgContext()));
        assertThat(auditMessage.getTimestamp(), is(messageEntity.getOccurTime()));
    }

    static AuditMessage given_an_auditMessage() {
        return AuditMessageBuilder.builder()
                .process(REST_NORTH)
                .component(WEBSOCKET)
                .msgName(OPERATION)
                .msgType(REQUEST)
                .msgDirection(IN)
                .subject("subject")
                .subjectType(DEVICE)
                .user("user")
                .localCorrelationId("local_correlation_id")
                .globalCorrelationId("global_correlation_id")
                .sequenceId("sequence_id")
                .msgStatus(SUCCESS)
                .secured(false)
                .msgSizeBytes(100)
                .msgContext(singletonMap("context_key", "context_value"))
                .msgPayload(asList("payload_value_1", "payload_value_2"))
                .timestamp(new Date())
                .version(1)
                .build();
    }
}
