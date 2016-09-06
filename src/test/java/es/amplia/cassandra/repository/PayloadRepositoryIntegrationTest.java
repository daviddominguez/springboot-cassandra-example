package es.amplia.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.Payload;
import es.amplia.model.builder.AuditMessageBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static es.amplia.cassandra.entity.AuditMessageEntity.Names.ID_FIELD;
import static es.amplia.cassandra.entity.Payload.Names.MSG_PAYLOAD_FIELD;
import static es.amplia.cassandra.entity.Payload.PayloadBuilder.builder;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class PayloadRepositoryIntegrationTest {

    @Autowired
    private PayloadRepository repository;

    @Autowired
    private Session session;

    @Test
    public void given_a_payload_when_saved_into_repository_then_verify_is_correctly_saved()
            throws ParseException {
        Payload payload = given_a_payload();
        BoundStatement statement = when_saved_into_repository(payload);

        verify_insert_query_is_well_formed(payload, statement,
                "INSERT INTO audit.payloads_by_id");
    }

    @Test
    public void given_a_persisted_north_message_by_interval_when_queried_then_verify_is_expected_message()
            throws ParseException {
        Payload persisted = given_a_persisted_message(given_a_payload());

        Payload queried = repository.get(persisted.getId());

        verify_both_messages_are_equal(queried, persisted);
    }

    private Payload given_a_persisted_message(Payload payload) {
        BoundStatement statement = when_saved_into_repository(payload);
        payload.setId(statement.getUUID(ID_FIELD));
        session.execute(statement);
        return payload;
    }

    private void verify_both_messages_are_equal(Payload queried, Payload persisted) {
        assertThat(queried, equalTo(persisted));
    }

    private void verify_insert_query_is_well_formed(Payload payload, BoundStatement statement, String expectedQuery) {
        assertThat(statement.preparedStatement().getQueryString(), startsWith(expectedQuery));
        assertThat(statement.getUUID(ID_FIELD), equalTo(payload.getId()));
        assertThat(statement.getList(MSG_PAYLOAD_FIELD, String.class), equalTo(payload.getMsgPayload()));
    }

    private BoundStatement when_saved_into_repository(Payload payload) {
        return (BoundStatement) repository.saveQuery(payload);
    }

    private Payload given_a_payload() {
        return builder().build(AuditMessageBuilder.builder()
                        .msgPayload(asList("payload_value_1", "payload_value_2"))
                        .build());
    }
}
