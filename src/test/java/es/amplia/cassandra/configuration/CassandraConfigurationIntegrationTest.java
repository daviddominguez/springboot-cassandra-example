package es.amplia.cassandra.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.repository.*;
import es.amplia.cassandra.service.AlarmMessagesService;
import es.amplia.cassandra.service.NorthMessagesService;
import es.amplia.cassandra.service.PayloadService;
import es.amplia.cassandra.service.SouthMessagesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSpringBootCassandraApplication.class)
public class CassandraConfigurationIntegrationTest {

    @Autowired
    private Cluster cluster;

    @Autowired
    private Session session;

    @Autowired
    private MappingManager mappingManager;

    @Autowired
    private NorthMessagesByIntervalRepository northMessagesByIntervalRepository;

    @Autowired
    private NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository;

    @Autowired
    private NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository;

    @Autowired
    private SouthMessagesByIntervalRepository southMessagesByIntervalRepository;

    @Autowired
    private SouthMessagesBySubjectIntervalRepository southMessagesBySubjectIntervalRepository;

    @Autowired
    private SouthMessagesBySubjectUserIntervalRepository southMessagesBySubjectUserIntervalRepository;

    @Autowired
    private AlarmMessagesByIntervalRepository alarmMessagesByIntervalRepository;

    @Autowired
    private AlarmMessagesBySubjectIntervalRepository alarmMessagesBySubjectIntervalRepository;

    @Autowired
    private AlarmMessagesBySubjectUserIntervalRepository alarmMessagesBySubjectUserIntervalRepository;

    @Autowired
    private PayloadRepository payloadRepository;

    @Autowired
    private NorthMessagesService northMessagesService;

    @Autowired
    private SouthMessagesService southMessagesService;

    @Autowired
    private AlarmMessagesService alarmMessagesService;

    @Autowired
    private PayloadService payloadService;

    @Test
    public void when_application_loads_then_verify_all_services_are_well_injected() {
        assertThat(cluster, notNullValue());
        assertThat(session, notNullValue());
        assertThat(mappingManager, notNullValue());
        assertThat(cluster.getMetadata().getKeyspace("audit"), notNullValue());
        assertThat(session.getCluster(), is(cluster));
        assertThat(mappingManager.getSession(), is(session));
        assertThat(northMessagesByIntervalRepository, notNullValue());
        assertThat(northMessagesByUserIntervalRepository, notNullValue());
        assertThat(northMessagesByUserSubjectIntervalRepository, notNullValue());
        assertThat(southMessagesByIntervalRepository, notNullValue());
        assertThat(southMessagesBySubjectIntervalRepository, notNullValue());
        assertThat(southMessagesBySubjectUserIntervalRepository, notNullValue());
        assertThat(alarmMessagesByIntervalRepository, notNullValue());
        assertThat(alarmMessagesBySubjectIntervalRepository, notNullValue());
        assertThat(alarmMessagesBySubjectUserIntervalRepository, notNullValue());
        assertThat(payloadRepository, notNullValue());
        assertThat(northMessagesService, notNullValue());
        assertThat(southMessagesService, notNullValue());
        assertThat(alarmMessagesService, notNullValue());
        assertThat(payloadService, notNullValue());
    }
}
