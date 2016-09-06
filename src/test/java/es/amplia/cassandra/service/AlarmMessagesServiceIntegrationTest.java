package es.amplia.cassandra.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.PagingStateException;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.*;
import es.amplia.model.AuditMessage;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static es.amplia.cassandra.entity.AuditMessageEntity.Names.*;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Payload.Names.PAYLOAD_BY_ID_TABLE;
import static es.amplia.cassandra.service.ServiceTestUtils.*;
import static java.text.DateFormat.SHORT;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class AlarmMessagesServiceIntegrationTest {

    @Autowired
    private AlarmMessagesService alarmMessagesService;

    @Autowired
    private PayloadService payloadService;

    @Autowired
    private Session session;

    @Autowired
    private MappingManager mappingManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void cleanUp () throws ParseException {
        session.execute(truncate(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, ALARM_MESSAGES_BY_SUBJECT_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, ALARM_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, PAYLOAD_BY_ID_TABLE));
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))).all();

        Page<AlarmMessageByInterval> response = alarmMessagesService.getMessagesByInterval(from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (AlarmMessageByInterval message : response.content) {
            verify_abstractMessage_has_all_expected_values(session, message);
        }
    }

    @Test
    public void given_a_repository_with_more_rows_than_fetch_size_configured_when_queried_by_then_verify_returned_messages_are_paged() throws ParseException {
        int rowNum = 1000;
        int fetchSize = 100;
        given_a_repository_with_a_collection_of_persisted_messages(rowNum);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");

        String page = null;
        int pagesQueried = 0;
        List<AlarmMessageByInterval> pagedRows = new ArrayList<>();

        Mapper<AlarmMessageByInterval> mapper = mappingManager.mapper(AlarmMessageByInterval.class);
        List<AlarmMessageByInterval> fetchedRows = mapper.map(session.execute(select()
                .all()
                .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to)))).all();
        do {
            Page<AlarmMessageByInterval> response = alarmMessagesService.getMessagesByInterval(from, to, page, fetchSize);
            page = response.pageContext;
            assertThat(response.content, hasSize(lessThanOrEqualTo(fetchSize)));
            pagedRows.addAll(response.content);
            for (AlarmMessageByInterval message : response.content) {
                verify_abstractMessage_has_all_expected_values(session, message);
            }
            if (page != null)
                pagesQueried++;
        }
        while (page != null);
        assertThat(pagedRows, hasSize(fetchedRows.size()));
        assertThat(pagedRows, containsInAnyOrder(fetchedRows.toArray()));
        assertThat(fetchedRows.size()/fetchSize, is(pagesQueried));
    }

    @Test
    public void given_a_repository_with_more_rows_than_fetch_size_configured_when_queried_changing_page_size_then_verify_returned_messages_are_paged() throws ParseException {
        int numRowsInRepository = 1000;
        int fetchSize = 50;
        given_a_repository_with_a_collection_of_persisted_messages(numRowsInRepository);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");

        String page = null;
        int pagesQueried = 0;
        List<AlarmMessageByInterval> pagedRows = new ArrayList<>();

        Mapper<AlarmMessageByInterval> mapper = mappingManager.mapper(AlarmMessageByInterval.class);
        List<AlarmMessageByInterval> fetchedRows = mapper.map(session.execute(select()
                .all()
                .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to)))).all();
        do {
            Page<AlarmMessageByInterval> response = alarmMessagesService.getMessagesByInterval(from, to, page, fetchSize);
            page = response.pageContext;
            assertThat(response.content, hasSize(lessThanOrEqualTo(fetchSize)));
            pagedRows.addAll(response.content);
            for (AlarmMessageByInterval message : response.content) {
                verify_abstractMessage_has_all_expected_values(session, message);
            }
            pagesQueried++;
            fetchSize *= 2;
        }
        while (page != null);
        assertThat(pagedRows, containsInAnyOrder(fetchedRows.toArray()));
        assertThat(pagesQueried, is(5));
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_without_messages_then_verify_returned_messages_is_empty() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/1975 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/1975 0:00:00");

        Page<AlarmMessageByInterval> response = alarmMessagesService.getMessagesByInterval(from, to, null);
        assertThat(response.content, empty());
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_and_subject_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        String subject = given_a_list_of_subjects().get(0);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))
                .and(eq(SUBJECT_FIELD, subject))).all();

        Page<AlarmMessageBySubjectInterval> response = alarmMessagesService.getMessagesBySubjectInterval(subject, from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (AlarmMessageBySubjectInterval message : response.content) {
            assertThat(message.getSubject(), is(subject));
            verify_abstractMessage_has_all_expected_values(session, message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_user_and_subject_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        String subject = given_a_list_of_subjects().get(0);
        String user = given_a_list_of_users().get(0);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))
                .and(eq(SUBJECT_FIELD, subject)).and(eq(USER_FIELD, user))).all();

        Page<AlarmMessageBySubjectUserInterval> response = alarmMessagesService.getMessagesBySubjectUserInterval(subject, user, from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (AlarmMessageBySubjectUserInterval message : response.content) {
            assertThat(message.getSubject(), is(subject));
            assertThat(message.getUser(), is(user));
            verify_abstractMessage_has_all_expected_values(session, message);
        }
    }

    @Test
    public void given_an_auditMessage_when_saved_then_its_persisted_into_all_related_tables() {
        Date occurTime = new Date();
        AuditMessage auditMessage = given_an_auditMessage(occurTime);
        when_saved(auditMessage);

        Mapper<AlarmMessageByInterval> alarmMessageByIntervalMapper =
                mappingManager.mapper(AlarmMessageByInterval.class);
        List<AlarmMessageByInterval> alarmMessagesByInterval = alarmMessageByIntervalMapper.map(
                session.execute(select().all()
                        .from(KEYSPACE, ALARM_MESSAGES_BY_INTERVAL_TABLE).allowFiltering()
                        .where(eq(OCCUR_TIME_FIELD, occurTime)))).all();

        Mapper<AlarmMessageBySubjectInterval> alarmMessageBySubjectIntervalMapper =
                mappingManager.mapper(AlarmMessageBySubjectInterval.class);
        List<AlarmMessageBySubjectInterval> alarmMessagesByUserInterval = alarmMessageBySubjectIntervalMapper.map(
                session.execute(select().all()
                        .from(KEYSPACE, ALARM_MESSAGES_BY_SUBJECT_INTERVAL_TABLE).allowFiltering()
                        .where(eq(OCCUR_TIME_FIELD, occurTime)))).all();

        Mapper<AlarmMessageBySubjectUserInterval> alarmMessageBySubjectUserIntervalMapper =
                mappingManager.mapper(AlarmMessageBySubjectUserInterval.class);
        List<AlarmMessageBySubjectUserInterval> alarmMessagesByUserSubjectInterval =
                alarmMessageBySubjectUserIntervalMapper.map(
                        session.execute(select().all()
                                .from(KEYSPACE, ALARM_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE).allowFiltering()
                                .where(eq(OCCUR_TIME_FIELD, occurTime)))).all();

        Payload payload = payloadService.getMessagePayload(alarmMessagesByInterval.get(0).getPayloadId());
        assertThat(alarmMessagesByInterval, hasSize(1));
        assertThat(alarmMessagesByUserInterval, hasSize(1));
        assertThat(alarmMessagesByUserSubjectInterval, hasSize(1));
        verify_payload_row_exists(session, alarmMessagesByInterval.get(0).getPayloadId());
        assertThat(alarmMessagesByInterval.get(0).getPayloadId(), allOf(
                equalTo(alarmMessagesByUserInterval.get(0).getPayloadId()),
                equalTo(alarmMessagesByUserSubjectInterval.get(0).getPayloadId())));
        assertThat(payload.getMsgPayload(), is(auditMessage.getMsgPayload()));
    }

    @Test
    public void given_a_repository_when_queried_with_wrong_paging_state_then_exception_thrown() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        expectedException.expect(PagingStateException.class);

        alarmMessagesService.getMessagesByInterval(new Date(), new Date(), "wrongPagingState");
    }

    @Test
    public void given_a_repository_when_queried_by_a_too_big_interval_then_exception_thrown() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("01/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("08/01/2016 0:00:00");

        alarmMessagesService.getMessagesByInterval(from, to, null);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/02/2016 0:00:00");

        alarmMessagesService.getMessagesBySubjectInterval("", from, to, null);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_subject_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/03/2016 0:00:00");

        alarmMessagesService.getMessagesBySubjectUserInterval("", "", from, to, null);
    }

    private void given_a_repository_with_a_collection_of_persisted_messages(int num) throws ParseException {
        List<AuditMessage> auditMessages = given_a_collection_of_auditMessages(given_a_collection_of_dates(), num);
        for (AuditMessage auditMessage : auditMessages) {
            when_saved(auditMessage);
        }
    }

    private void when_saved(AuditMessage auditMessage) {
        alarmMessagesService.save(auditMessage);
    }

    private List<String> given_a_collection_of_dates() {
        return asList("01/01/2016T1:00:00.000", "01/01/2016T2:00:00.000", "01/01/2016T3:00:00.000",
                "01/01/2016T4:00:00.000", "01/01/2016T5:00:00.000", "01/01/2016T6:00:00.000",
                "01/01/2016T7:00:00.000", "01/01/2016T8:00:00.000", "01/01/2016T9:00:00.000",
                "01/01/2016T10:00:00.000", "01/01/2016T11:00:00.000", "01/01/2016T12:00:00.000",
                "01/01/2016T13:00:00.000", "01/01/2016T14:00:00.000", "01/01/2016T15:00:00.000",
                "01/01/2016T16:00:00.000", "01/01/2016T17:00:00.000", "01/01/2016T18:00:00.000",
                "01/01/2016T19:00:00.000", "01/01/2016T20:00:00.000", "01/01/2016T21:00:00.000",
                "01/01/2016T22:00:00.000", "01/01/2016T23:00:00.000", "02/01/2016T0:00:00.000",
                "02/01/2016T1:00:00.000", "02/01/2016T2:00:00.000", "02/01/2016T3:00:00.000",
                "02/01/2016T4:00:00.000", "02/01/2016T5:00:00.000", "02/01/2016T6:00:00.000"
        );
    }
}
