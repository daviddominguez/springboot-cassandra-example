package es.amplia.cassandra.entity;

import es.amplia.cassandra.entity.SouthMessageBySubjectUserInterval.SouthMessageBySubjectUserIntervalBuilder;
import es.amplia.model.AuditMessage;
import org.junit.Test;

import static es.amplia.cassandra.entity.EntityTestUtils.given_an_auditMessage;
import static es.amplia.cassandra.entity.EntityTestUtils.verify_auditMessage_and_entity_has_the_same_data;

public class SouthMessageBySubjectUserIntervalBuilderUnitTest {

    @Test
    public void given_an_auditMessage_when_southMessageByUserInterval_built_from_auditMessage_then_both_objects_has_the_same_data() {
        AuditMessage auditMessage = given_an_auditMessage();
        SouthMessageBySubjectUserInterval southMessageBySubjectUserInterval =
                (SouthMessageBySubjectUserInterval) SouthMessageBySubjectUserIntervalBuilder.builder().build(auditMessage);

        verify_auditMessage_and_entity_has_the_same_data(auditMessage, southMessageBySubjectUserInterval);
    }
}
