package es.amplia.cassandra.service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByInterval.NorthMessageByIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserInterval.NorthMessageByUserIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval.NorthMessageByUserSubjectIntervalBuilder;
import es.amplia.cassandra.repository.NorthMessagesByIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserSubjectIntervalRepository;
import es.amplia.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class NorthMessagesServiceImpl implements NorthMessagesService {

    private Session session;
    private NorthMessagesByIntervalRepository northMessagesByIntervalRepository;
    private NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository;
    private NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository;
    private NorthMessagesByIntervalAccessor northMessagesByIntervalAccessor;
    private NorthMessagesByUserIntervalAccessor northMessagesByUserIntervalAccessor;
    private NorthMessagesByUserSubjectIntervalAccessor northMessagesByUserSubjectIntervalAccessor;

    @Autowired
    public NorthMessagesServiceImpl(
            Session session,
            NorthMessagesByIntervalRepository northMessagesByIntervalRepository,
            NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository,
            NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository,
            NorthMessagesByIntervalAccessor northMessagesByIntervalAccessor,
            NorthMessagesByUserIntervalAccessor northMessagesByUserIntervalAccessor,
            NorthMessagesByUserSubjectIntervalAccessor northMessagesByUserSubjectIntervalAccessor
    ) {
        this.session = session;
        this.northMessagesByIntervalRepository = northMessagesByIntervalRepository;
        this.northMessagesByUserIntervalRepository = northMessagesByUserIntervalRepository;
        this.northMessagesByUserSubjectIntervalRepository = northMessagesByUserSubjectIntervalRepository;
        this.northMessagesByIntervalAccessor = northMessagesByIntervalAccessor;
        this.northMessagesByUserIntervalAccessor = northMessagesByUserIntervalAccessor;
        this.northMessagesByUserSubjectIntervalAccessor = northMessagesByUserSubjectIntervalAccessor;
    }

    @Override
    public void save(AuditMessage message) {
        BatchStatement batch = new BatchStatement();
        batch.add(northMessagesByIntervalRepository.saveQuery(new NorthMessageByIntervalBuilder().build(message)));
        batch.add(northMessagesByUserIntervalRepository.saveQuery(new NorthMessageByUserIntervalBuilder().build(message)));
        batch.add(northMessagesByUserSubjectIntervalRepository.saveQuery(new NorthMessageByUserSubjectIntervalBuilder().build(message)));
        session.execute(batch);
    }

    public List<NorthMessageByInterval> getMessagesByInterval(Date from, Date to) {
        return Collections.emptyList();
    }
}
