package es.amplia.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.AlarmMessageBySubjectUserInterval;
import es.amplia.model.builder.AuditMessageBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static es.amplia.cassandra.entity.AlarmMessageBySubjectUserInterval.AlarmMessageBySubjectUserIntervalBuilder.builder;
import static es.amplia.cassandra.repository.AuditEntityRepositoryTestUtils.*;
import static es.amplia.model.AuditMessage.ComponentType.WEBSOCKET;
import static es.amplia.model.AuditMessage.MsgDirection.IN;
import static es.amplia.model.AuditMessage.MsgStatus.SUCCESS;
import static es.amplia.model.AuditMessage.MsgType.RESPONSE;
import static es.amplia.model.AuditMessage.NameType.DMM;
import static es.amplia.model.AuditMessage.ProcessType.ALARM;
import static es.amplia.model.AuditMessage.SubjectType.IMSI;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class AlarmMessagesBySubjectUserIntervalRepositoryIntegrationTest {

    @Autowired
    private AlarmMessagesBySubjectUserIntervalRepository repository;

    @Autowired
    private Session session;

    @Test
    public void given_an_alarm_message_by_user_subject_interval_when_saved_into_repository_then_verify_is_correctly_saved()
            throws ParseException {
        AlarmMessageBySubjectUserInterval alarmMessageBySubjectUserInterval = given_a_message_by_subject_user_interval();
        BoundStatement statement = when_saved_into_repository(repository, alarmMessageBySubjectUserInterval);

        verify_insert_query_is_well_formed(alarmMessageBySubjectUserInterval, statement,
                "INSERT INTO audit.alarm_messages_by_subject_user_and_interval");
    }

    @Test
    public void given_a_persisted_alarm_message_by_user_subject_interval_when_queried_then_verify_is_expected_message() throws ParseException {
        AlarmMessageBySubjectUserInterval persistedMessage =
                (AlarmMessageBySubjectUserInterval) given_a_persisted_message(
                        given_a_message_by_subject_user_interval(), session, repository);

        AlarmMessageBySubjectUserInterval queriedMessage = repository.get(
                persistedMessage.getInterval(),
                persistedMessage.getSubject(),
                persistedMessage.getUser(),
                persistedMessage.getOccurTime(),
                persistedMessage.getId());

        verify_both_messages_are_equal(queriedMessage, persistedMessage);
    }

    private AlarmMessageBySubjectUserInterval given_a_message_by_subject_user_interval() throws ParseException {
        return (AlarmMessageBySubjectUserInterval) builder()
                .build(AuditMessageBuilder.builder()
                        .process(ALARM)
                        .component(WEBSOCKET)
                        .msgName(DMM)
                        .msgType(RESPONSE)
                        .msgDirection(IN)
                        .subject("subject")
                        .subjectType(IMSI)
                        .user("user")
                        .localCorrelationId("localCorrelationId")
                        .globalCorrelationId("globalCorrelationId")
                        .sequenceId("sequenceId")
                        .msgStatus(SUCCESS)
                        .secured(TRUE)
                        .msgSizeBytes(100)
                        .msgContext(singletonMap("context_key", "context_value"))
                        .msgPayload(asList("payload_value_1", "payload_value_2"))
                        .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2016-01-01T0:00:00.000"))
                        .version(1)
                        .build());
    }
}
