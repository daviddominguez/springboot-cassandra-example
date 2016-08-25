package es.amplia.cassandra.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.TestSpringBootCassandraApplication;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import es.amplia.cassandra.repository.NorthMessagesByIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserSubjectIntervalRepository;
import es.amplia.cassandra.service.NorthMessagesService;
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
public class CassandraConfigurationUnitTest {

    @Autowired
    private Cluster cluster;

    @Autowired
    private Session session;

    @Autowired
    private MappingManager mappingManager;

    @Autowired
    private NorthMessagesByIntervalAccessor northMessagesByIntervalAccessor;

    @Autowired
    private NorthMessagesByUserIntervalAccessor northMessagesByUserIntervalAccessor;

    @Autowired
    private NorthMessagesByUserSubjectIntervalAccessor northMessagesByUserSubjectIntervalAccessor;

    @Autowired
    private NorthMessagesByIntervalRepository northMessagesByIntervalRepository;

    @Autowired
    private NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository;

    @Autowired
    private NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository;

    @Autowired
    private NorthMessagesService northMessagesService;


    @Test
    public void when_application_loads_then_verify_all_services_are_well_injected() {
        assertThat(cluster, notNullValue());
        assertThat(session, notNullValue());
        assertThat(mappingManager, notNullValue());
        assertThat(cluster.getMetadata().getKeyspace("audit"), notNullValue());
        assertThat(session.getCluster(), is(cluster));
        assertThat(mappingManager.getSession(), is(session));
        assertThat(northMessagesByIntervalAccessor, notNullValue());
        assertThat(northMessagesByUserIntervalAccessor, notNullValue());
        assertThat(northMessagesByUserSubjectIntervalAccessor, notNullValue());
        assertThat(northMessagesByIntervalRepository, notNullValue());
        assertThat(northMessagesByUserIntervalRepository, notNullValue());
        assertThat(northMessagesByUserSubjectIntervalRepository, notNullValue());
        assertThat(northMessagesService, notNullValue());
    }
}
