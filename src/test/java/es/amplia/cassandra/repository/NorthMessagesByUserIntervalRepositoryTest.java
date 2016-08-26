package es.amplia.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.bucket.MonthBucket;
import es.amplia.cassandra.bucket.WeekBucket;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.model.builder.AuditMessageBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static es.amplia.cassandra.entity.NorthMessageByUserInterval.NorthMessageByUserIntervalBuilder.builder;
import static es.amplia.cassandra.repository.RepositoryTestUtils.*;
import static es.amplia.model.AuditMessage.ComponentType.WEBSOCKET;
import static es.amplia.model.AuditMessage.MsgDirection.IN;
import static es.amplia.model.AuditMessage.MsgStatus.SUCCESS;
import static es.amplia.model.AuditMessage.MsgType.RESPONSE;
import static es.amplia.model.AuditMessage.NameType.DMM;
import static es.amplia.model.AuditMessage.ProcessType.REST_NORTH;
import static es.amplia.model.AuditMessage.SubjectType.IMSI;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class NorthMessagesByUserIntervalRepositoryTest {

    @Autowired
    private NorthMessagesByUserIntervalRepository repository;

    @Autowired
    private Session session;

    @Test
    public void given_a_north_message_by_interval_when_created_then_verify_uses_month_bucket_implementation() {
        assertThat(repository.getBucket(), instanceOf(MonthBucket.class));
    }

    @Test
    public void given_a_north_message_by_user_interval_when_saved_into_repository_then_verify_is_correctly_saved() throws ParseException {
        NorthMessageByUserInterval northMessageByUserInterval = given_a_message_by_user_interval();
        Statement statement = when_saved_into_repository(repository, northMessageByUserInterval);
        verify_is_correctly_saved(northMessageByUserInterval, statement);
    }

    @Test
    public void given_a_persisted_north_message_by_user_interval_when_queried_then_verify_is_expected_message() throws ParseException {
        NorthMessageByUserInterval persistedMessage =
                (NorthMessageByUserInterval) given_a_persisted_message(given_a_message_by_user_interval(), session, repository);

        NorthMessageByUserInterval queriedMessage = repository.get(
                        persistedMessage.getInterval(),
                        persistedMessage.getUser(),
                        persistedMessage.getAuditId());

        verify_both_messages_are_equal(queriedMessage, persistedMessage);
    }

    private void verify_is_correctly_saved(NorthMessageByUserInterval northMessageByUserInterval, Statement statement) {
        BoundStatement boundStatement = (BoundStatement) statement;
        verify_message_parameters_are_correct(northMessageByUserInterval, boundStatement,
                "INSERT INTO audit.north_messages_by_user_and_interval");
    }

    private NorthMessageByUserInterval given_a_message_by_user_interval() throws ParseException {
        return (NorthMessageByUserInterval) builder()
                .build(AuditMessageBuilder.builder()
                        .process(REST_NORTH)
                        .component(WEBSOCKET)
                        .msgName(DMM)
                        .msgType(RESPONSE)
                        .msgDirection(IN)
                        .subject("subject")
                        .subjectType(IMSI)
                        .user("user")
                        .transactionId("transactionId")
                        .sequenceId("sequenceId")
                        .msgStatus(SUCCESS)
                        .msgSizeBytes(100)
                        .msgContext(singletonMap("payload_key", "payload_value"))
                        .timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2016-01-01T0:00:00.000"))
                        .version(1)
                        .build());
    }
}