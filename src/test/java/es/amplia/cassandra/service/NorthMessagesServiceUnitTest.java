package es.amplia.cassandra.service;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.*;
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
import java.util.*;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;
import static es.amplia.cassandra.entity.AuditMessageEntity.Names.*;
import static es.amplia.cassandra.entity.Payload.Names.*;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;
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

    @Autowired
    private MappingManager mappingManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void cleanUp () throws ParseException {
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_USER_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE));
        session.execute(truncate(KEYSPACE, PAYLOAD_BY_ID_TABLE));
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("29/01/2016 0:00:00");
        List<Row> rows = session.execute(select()
                .all()
                .from(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to))).all();

        Page<NorthMessageByInterval> response = northMessagesService.getMessagesByInterval(from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (NorthMessageByInterval message : response.content) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_with_more_rows_than_fetch_size_configured_when_queried_by_then_verify_returned_messages_are_paged() throws ParseException {
        int rowNum = 1000;
        int fetchSizeConfiguredInCassandraTestConfiguration = 100;
        given_a_repository_with_a_collection_of_persisted_messages(rowNum);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("29/01/2016 0:00:00");

        String page = null;
        int pagesQueried = 0;
        List<NorthMessageByInterval> pagedRows = new ArrayList<>();

        Mapper<NorthMessageByInterval> mapper = mappingManager.mapper(NorthMessageByInterval.class);
        List<NorthMessageByInterval> fetchedRows = mapper.map(session.execute(select()
                .all()
                .from(KEYSPACE, NORTH_MESSAGES_BY_INTERVAL_TABLE)
                .allowFiltering()
                .where(gte(OCCUR_TIME_FIELD, from))
                .and(lte(OCCUR_TIME_FIELD, to)))).all();
        do {
            Page<NorthMessageByInterval> response = northMessagesService.getMessagesByInterval(from, to, page);
            page = response.pageContext;
            assertThat(response.content, hasSize(lessThanOrEqualTo(fetchSizeConfiguredInCassandraTestConfiguration)));
            pagedRows.addAll(response.content);
            for (NorthMessageByInterval message : response.content) {
                verify_abstractMessage_has_all_expected_values(message);
            }
            pagesQueried++;
        }
        while (page != null);
        assertThat(pagedRows, containsInAnyOrder(fetchedRows.toArray()));
        assertThat(rowNum/fetchSizeConfiguredInCassandraTestConfiguration, is(pagesQueried));
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_without_messages_then_verify_returned_messages_is_empty() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/1975 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/1975 0:00:00");

        Page<NorthMessageByInterval> response = northMessagesService.getMessagesByInterval(from, to, null);
        assertThat(response.content, empty());
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_and_user_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
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

        Page<NorthMessageByUserInterval> response = northMessagesService.getMessagesByUserInterval(user, from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (NorthMessageByUserInterval message : response.content) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_specific_interval_user_and_subject_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
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

        Page<NorthMessageByUserSubjectInterval> response = northMessagesService.getMessagesByUserSubjectInterval(user, subject, from, to, null);
        assertThat(response.content, hasSize(rows.size()));
        for (NorthMessageByUserSubjectInterval message : response.content) {
            verify_abstractMessage_has_all_expected_values(message);
        }
    }

    @Test
    public void given_a_repository_when_queried_by_a_too_big_interval_then_exception_thrown() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages(30);
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/03/2016 0:00:00");

        northMessagesService.getMessagesByInterval(from, to, null);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/05/2016 0:00:00");

        northMessagesService.getMessagesByUserInterval("", from, to, null);
    }

    @Test
    public void given_a_repository_when_queried_by_an_user_subject_and_a_too_big_interval_then_exception_thrown() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("specified time range is too big, be more specific");
        given_a_repository_with_a_collection_of_persisted_messages(30);
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2017 0:00:00");

        northMessagesService.getMessagesByUserSubjectInterval("", "", from, to, null);
    }

    private void given_a_repository_with_a_collection_of_persisted_messages(int num) throws ParseException {
        List<AuditMessage> auditMessages = given_a_collection_of_auditMessages(num);
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

    private List<AuditMessage> given_a_collection_of_auditMessages(int num) throws ParseException {
        List<String> dates = given_a_collection_of_dates();
        List<AuditMessage> auditMessages = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            auditMessages.add(given_an_auditMessage(format.parse(dates.get(new Random().nextInt(dates.size())))));
        }
        return auditMessages;
    }

    private List<String> given_a_list_of_users() {
        return asList("user_1", "user_2", "user_3");
    }

    private List<String> given_a_list_of_subjects() {
        return asList("subject_1", "subject_2", "subject_3", "subject_4", "subject_5");
    }

    private List<String> given_a_list_of_local_correlation_ids() {
        return asList("local_correlationId_1", "local_correlationId_2", "local_correlationId_3");
    }

    private List<String> given_a_list_of_global_correlation_ids() {
        return asList("global_correlationId_1", "global_correlationId_2", "global_correlationId_3");
    }

    private List<String> given_a_list_of_sequence_ids() {
        return asList("sequenceId_1", "sequenceId_2", "sequenceId_3");
    }

    private AuditMessage given_an_auditMessage(Date date) {
        List<String> users = given_a_list_of_users();
        List<String> subjects = given_a_list_of_subjects();
        List<String> localCorrelationIds = given_a_list_of_local_correlation_ids();
        List<String> globalCorrelationIds = given_a_list_of_global_correlation_ids();
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
                .localCorrelationId(localCorrelationIds.get(new Random().nextInt(localCorrelationIds.size())))
                .globalCorrelationId(globalCorrelationIds.get(new Random().nextInt(globalCorrelationIds.size())))
                .sequenceId(sequenceIds.get(new Random().nextInt(sequenceIds.size())))
                .msgStatus(asList(MsgStatus.values()).get(new Random().nextInt(MsgStatus.values().length)))
                .secured(new Random().nextBoolean())
                .msgSizeBytes(100)
                .msgContext(singletonMap("context_key", "context_value"))
                .msgPayload(asList("payload_value_1", "payload_value_2"))
                .timestamp(date)
                .version(1)
                .build();
    }

    private void verify_abstractMessage_has_all_expected_values(AuditMessageEntity auditMessageEntity) throws ParseException {
        assertThat(auditMessageEntity.getInterval(), notNullValue());
        assertThat(auditMessageEntity.getId(), notNullValue());
        assertThat(auditMessageEntity.getComponentType(), isIn(ComponentType.values()));
        assertThat(auditMessageEntity.getMsgName(), isIn(NameType.values()));
        assertThat(auditMessageEntity.getMsgType(), isIn(MsgType.values()));
        assertThat(auditMessageEntity.getMsgDirection(), isIn(MsgDirection.values()));
        assertThat(auditMessageEntity.getSubject(), anyOf(isIn(given_a_list_of_subjects()), equalTo("subjectA")));
        assertThat(auditMessageEntity.getSubjectType(), isIn(SubjectType.values()));
        assertThat(auditMessageEntity.getUser(), anyOf(isIn(given_a_list_of_users()), equalTo("userA")));
        assertThat(auditMessageEntity.getLocalCorrelationId(), isIn(given_a_list_of_local_correlation_ids()));
        assertThat(auditMessageEntity.getGlobalCorrelationId(), isIn(given_a_list_of_global_correlation_ids()));
        assertThat(auditMessageEntity.getSequenceId(), isIn(given_a_list_of_sequence_ids()));
        assertThat(auditMessageEntity.getMsgStatus(), isIn(MsgStatus.values()));
        assertThat(auditMessageEntity.getSecured(), notNullValue());
        assertThat(auditMessageEntity.getMsgSizeBytes(), is(100));
        assertThat(auditMessageEntity.getMsgContext(), hasKey("context_key"));
        assertThat(auditMessageEntity.getMsgContext(), hasValue("context_value"));
        assertThat(auditMessageEntity.getPayloadId(), notNullValue());
        assertThat(auditMessageEntity.getOccurTime(), notNullValue());
        verify_payload_row_exists(auditMessageEntity.getPayloadId());
    }

    private void verify_payload_row_exists(UUID id) {
        Mapper<Payload> mapper = mappingManager.mapper(Payload.class);
        Payload payload = mapper.map(session.execute(select()
                .all()
                .from(KEYSPACE, PAYLOAD_BY_ID_TABLE)
                .where(eq(Payload.Names.ID_FIELD, id)))).one();
        assertThat(payload, notNullValue());
    }
}
