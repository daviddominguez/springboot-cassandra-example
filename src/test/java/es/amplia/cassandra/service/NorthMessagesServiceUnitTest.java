package es.amplia.cassandra.service;

import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.entity.Message;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.model.AuditMessage;
import es.amplia.model.AuditMessage.*;
import es.amplia.model.builder.AuditMessageBuilder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static es.amplia.model.AuditMessage.ComponentType.WEBSOCKET;
import static es.amplia.model.AuditMessage.MsgDirection.IN;
import static es.amplia.model.AuditMessage.MsgStatus.SUCCESS;
import static es.amplia.model.AuditMessage.MsgType.RESPONSE;
import static es.amplia.model.AuditMessage.NameType.DMM;
import static es.amplia.model.AuditMessage.ProcessType.REST_NORTH;
import static es.amplia.model.AuditMessage.SubjectType.IMSI;
import static java.text.DateFormat.SHORT;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class NorthMessagesServiceUnitTest {

    @Autowired
    private NorthMessagesService northMessagesService;

    @Test
    public void given_a_repository_with_a_collection_of_persisted_messages_from_several_intervals_when_queried_by_specific_interval_then_verify_returned_messages_are_in_that_interval() throws ParseException {
        given_a_repository_with_a_collection_of_persisted_messages_from_several_intervals();
        Date from = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("02/01/2016 0:00:00");
        Date to = DateFormat.getDateTimeInstance(SHORT, SHORT).parse("07/01/2016 0:00:00");

        List<NorthMessageByInterval> northMessagesByInterval = northMessagesService.getMessagesByInterval(from, to);
        Assert.assertThat(northMessagesByInterval, Matchers.hasSize(6));
    }

    private void given_a_repository_with_a_collection_of_persisted_messages_from_several_intervals() throws ParseException {
        List<AuditMessage> auditMessages = given_a_collection_of_auditMessages();
        for (AuditMessage auditMessage : auditMessages) {
            northMessagesService.save(auditMessage);
        }
    }

    private List<AuditMessage> given_a_collection_of_auditMessages() throws ParseException {
        return asList(
                given_an_auditMessage("01/01/2016 0:00:00"),
                given_an_auditMessage("02/01/2016 0:00:00"),
                given_an_auditMessage("03/01/2016 0:00:00"),
                given_an_auditMessage("04/01/2016 0:00:00"),
                given_an_auditMessage("05/01/2016 0:00:00"),
                given_an_auditMessage("06/01/2016 0:00:00"),
                given_an_auditMessage("07/01/2016 0:00:00"),
                given_an_auditMessage("08/01/2016 0:00:00"),
                given_an_auditMessage("09/01/2016 0:00:00"),
                given_an_auditMessage("10/01/2016 0:00:00"),
                given_an_auditMessage("11/01/2016 0:00:00"),
                given_an_auditMessage("12/01/2016 0:00:00"),
                given_an_auditMessage("13/01/2016 0:00:00"),
                given_an_auditMessage("14/01/2016 0:00:00"),
                given_an_auditMessage("15/01/2016 0:00:00"),
                given_an_auditMessage("16/01/2016 0:00:00"),
                given_an_auditMessage("17/01/2016 0:00:00"),
                given_an_auditMessage("18/01/2016 0:00:00"),
                given_an_auditMessage("19/01/2016 0:00:00")
        );
    }

    private List<String> given_a_list_of_users() {
        return asList("user1", "user2", "user3", "user4");
    }

    private List<String> given_a_list_of_subjects() {
        return asList("subject1", "subject2", "subject3", "subject4");
    }

    private AuditMessage given_an_auditMessage(String timestamp) throws ParseException {
        List<String> users = given_a_list_of_users();
        List<String> subjects = given_a_list_of_subjects();

        return AuditMessageBuilder.builder()
                .process(asList(ProcessType.values()).get(new Random().nextInt(ProcessType.values().length)))
                .component(asList(ComponentType.values()).get(new Random().nextInt(ComponentType.values().length)))
                .msgName(asList(NameType.values()).get(new Random().nextInt(NameType.values().length)))
                .msgType(asList(MsgType.values()).get(new Random().nextInt(MsgType.values().length)))
                .msgDirection(asList(MsgDirection.values()).get(new Random().nextInt(MsgDirection.values().length)))
                .subject(subjects.get(new Random().nextInt(subjects.size())))
                .subjectType(asList(SubjectType.values()).get(new Random().nextInt(SubjectType.values().length)))
                .user(users.get(new Random().nextInt(users.size())))
                .transactionId("transactionId")
                .sequenceId("sequenceId")
                .msgStatus(asList(MsgStatus.values()).get(new Random().nextInt(MsgStatus.values().length)))
                .msgSizeBytes(100)
                .msgContext(singletonMap("payload_key", "payload_value"))
                .timestamp(DateFormat.getDateTimeInstance(SHORT, SHORT).parse(timestamp))
                .version(1)
                .build();
    }
}
