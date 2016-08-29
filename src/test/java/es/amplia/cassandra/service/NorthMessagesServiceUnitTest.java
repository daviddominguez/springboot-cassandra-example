package es.amplia.cassandra.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.annotations.Param;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.Message;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.model.AuditMessage;
import es.amplia.model.AuditMessage.*;
import es.amplia.model.builder.AuditMessageBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static es.amplia.cassandra.entity.Message.Names.*;
import static java.text.DateFormat.SHORT;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class NorthMessagesServiceUnitTest {

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy'T'H:mm:ss.SSS");

    @Autowired
    private NorthMessagesService northMessagesService;

    @Autowired
    private Session session;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void cleanUp () throws ParseException {
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_USER_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE));
        given_a_repository_with_a_collection_of_persisted_messages();
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("29/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))).all();

        List<NorthMessageByInterval> northMessagesByInterval = northMessagesService.getMessagesByInterval(from, to);
        assertThat(northMessagesByInterval, hasSize(rows.size()));
        for (NorthMessageByInterval message : northMessagesByInterval) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_without_messages_then_verify_returned_messages_is_empty() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages();
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/1975 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/1975 0:00:00");

        List<NorthMessageByInterval> northMessagesByInterval = northMessagesService.getMessagesByInterval(from, to);
        assertThat(northMessagesByInterval, empty());
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_and_user_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages();
        String user = given_a_list_of_users().get(0);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("29/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))
                .and(eq(USER_FIELD, user))).all();

        List<NorthMessageByUserInterval> northMessagesByUserInterval = northMessagesService.getMessagesByUserInterval(user, from, to);
        assertThat(northMessagesByUserInterval, hasSize(rows.size()));
        for (NorthMessageByUserInterval message : northMessagesByUserInterval) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_user_and_subject_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages();
        String user = given_a_list_of_users().get(0);
        String subject = given_a_list_of_subjects().get(0);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("29/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))
                .and(eq(USER_FIELD, user)).and(eq(SUBJECT_FIELD, subject))).all();

        List<NorthMessageByUserSubjectInterval> northMessagesByUserSubjectInterval = northMessagesService.getMessagesByUserSubjectInterval(user, subject, from, to);
        assertThat(northMessagesByUserSubjectInterval, hasSize(rows.size()));
        for (NorthMessageByUserSubjectInterval message : northMessagesByUserSubjectInterval) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_a_too_big_interval_then_exception_thrown() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages();
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/03/2016 0:00:00");

        northMessagesService.getMessagesByInterval(from, to);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages();
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/05/2016 0:00:00");

        northMessagesService.getMessagesByUserInterval("", from, to);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_subject_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages();
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2017 0:00:00");

        northMessagesService.getMessagesByUserSubjectInterval("", "", from, to);
    }

    private void given_a_repository_with_a_collection_of_persisted_messages() throws ParseException {
        List<AuditMessage> auditMessages = given_a_collection_of_auditMessages();
        for (AuditMessage auditMessage : auditMessages) {
            northMessagesService.save(auditMessage);
        }
    }

    private List<String> given_a_collection_of_dates() {
        return asList("01/01/2016T0:00:00.000", "02/01/2016T0:00:00.000", "03/01/2016T0:00:00.000",
                "04/01/2016T0:00:00.000", "05/01/2016T0:00:00.000", "06/01/2016T0:00:00.000",
                "07/01/2016T0:00:00.000", "08/01/2016T0:00:00.000", "09/01/2016T0:00:00.000",
                "10/01/2016T0:00:00.000", "11/01/2016T0:00:00.000", "12/01/2016T0:00:00.000",
                "13/01/2016T0:00:00.000", "14/01/2016T0:00:00.000", "15/01/2016T0:00:00.000",
                "16/01/2016T0:00:00.000", "17/01/2016T0:00:00.000", "18/01/2016T0:00:00.000",
                "19/01/2016T0:00:00.000", "20/01/2016T0:00:00.000", "21/01/2016T0:00:00.000",
                "22/01/2016T0:00:00.000", "23/01/2016T0:00:00.000", "24/01/2016T0:00:00.000",
                "25/01/2016T0:00:00.000", "26/01/2016T0:00:00.000", "27/01/2016T0:00:00.000",
                "28/01/2016T0:00:00.000", "29/01/2016T0:00:00.000", "30/01/2016T0:00:00.000"
        );
    }
    private List<AuditMessage> given_a_collection_of_auditMessages() throws ParseException {
        List<String> dates = given_a_collection_of_dates();
        List<AuditMessage> auditMessages = new ArrayList<>();
        for (String date: dates) {
            auditMessages.add(given_an_auditMessage(format.parse(date)));
        }
        return auditMessages;
    }

    private List<String> given_a_list_of_users() {
        return asList("user_1", "user_2", "user_3");
    }

    private List<String> given_a_list_of_subjects() {
        return asList("subject_1", "subject_2", "subject_3", "subject_4", "subject_5");
    }

    private List<String> given_a_list_of_transaction_ids() {
        return asList("transactionId_1", "transactionId_2", "transactionId_3");
    }

    private List<String> given_a_list_of_sequence_ids() {
        return asList("sequenceId_1", "sequenceId_2", "sequenceId_3");
    }

    private AuditMessage given_an_auditMessage(Date date) {
        List<String> users = given_a_list_of_users();
        List<String> subjects = given_a_list_of_subjects();
        List<String> transactionIds = given_a_list_of_transaction_ids();
        List<String> sequenceIds = given_a_list_of_sequence_ids();

        return AuditMessageBuilder.builder()
                .process(asList(ProcessType.values()).get(new Random().nextInt(ProcessType.values().length)))
                .component(asList(ComponentType.values()).get(new Random().nextInt(ComponentType.values().length)))
                .msgName(asList(NameType.values()).get(new Random().nextInt(NameType.values().length)))
                .msgType(asList(MsgType.values()).get(new Random().nextInt(MsgType.values().length)))
                .msgDirection(asList(MsgDirection.values()).get(new Random().nextInt(MsgDirection.values().length)))
                .subject(subjects.get(new Random().nextInt(subjects.size())))
                .subjectType(asList(SubjectType.values()).get(new Random().nextInt(SubjectType.values().length)))
                .user(users.get(new Random().nextInt(users.size())))
                .transactionId(transactionIds.get(new Random().nextInt(transactionIds.size())))
                .sequenceId(sequenceIds.get(new Random().nextInt(sequenceIds.size())))
                .msgStatus(asList(MsgStatus.values()).get(new Random().nextInt(MsgStatus.values().length)))
                .msgSizeBytes(100)
                .msgContext(singletonMap("payload_key", "payload_value"))
                .timestamp(date)
                .version(1)
                .build();
    }

    private void verify_abstractMessage_has_all_expected_values(Message message) throws ParseException {
        assertThat(message.getInterval(), notNullValue());
        assertThat(message.getAuditId(), notNullValue());
        assertThat(message.getComponentType(), isIn(ComponentType.values()));
        assertThat(message.getMsgName(), isIn(NameType.values()));
        assertThat(message.getMsgType(), isIn(MsgType.values()));
        assertThat(message.getMsgDirection(), isIn(MsgDirection.values()));
        assertThat(message.getSubject(), anyOf(isIn(given_a_list_of_subjects()), equalTo("subjectA")));
        assertThat(message.getSubjectType(), isIn(SubjectType.values()));
        assertThat(message.getUser(), anyOf(isIn(given_a_list_of_users()), equalTo("userA")));
        assertThat(message.getTransactionId(), isIn(given_a_list_of_transaction_ids()));
        assertThat(message.getSequenceId(), isIn(given_a_list_of_sequence_ids()));
        assertThat(message.getMsgStatus(), isIn(MsgStatus.values()));
        assertThat(message.getMsgSizeBytes(), is(100));
        assertThat(message.getMsgContext(), hasKey("payload_key"));
        assertThat(message.getMsgContext(), hasValue("payload_value"));
        assertThat(message.getOccurTime(), notNullValue());
    }
}
