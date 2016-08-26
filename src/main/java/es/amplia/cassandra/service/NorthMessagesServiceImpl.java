package es.amplia.cassandra.service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.accessor.NorthMessagesByIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserIntervalAccessor;
import es.amplia.cassandra.accessor.NorthMessagesByUserSubjectIntervalAccessor;
import es.amplia.cassandra.entity.NorthMessageByInterval;
import es.amplia.cassandra.entity.NorthMessageByInterval.NorthMessageByIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserInterval;
import es.amplia.cassandra.entity.NorthMessageByUserInterval.NorthMessageByUserIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval.NorthMessageByUserSubjectIntervalBuilder;
import es.amplia.cassandra.repository.NorthMessagesByIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserSubjectIntervalRepository;
import es.amplia.cassandra.repository.Repository;
import es.amplia.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

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
        batch.add(northMessagesByIntervalRepository.saveQuery((NorthMessageByInterval) NorthMessageByIntervalBuilder.builder().build(message)));
        batch.add(northMessagesByUserIntervalRepository.saveQuery((NorthMessageByUserInterval) NorthMessageByUserIntervalBuilder.builder().build(message)));
        batch.add(northMessagesByUserSubjectIntervalRepository.saveQuery((NorthMessageByUserSubjectInterval) NorthMessageByUserSubjectIntervalBuilder.builder().build(message)));
        session.execute(batch);
    }

    @Override
    public List<NorthMessageByInterval> getMessagesByInterval(Date from, Date to) {
        List<Long> interval = getBucketIntervalForRepository(northMessagesByIntervalRepository, from, to, 8);
        return northMessagesByIntervalAccessor.getMessagesByIntervalList(interval).all();
    }

    @Override
    public List<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to) {
        List<Long> interval = getBucketIntervalForRepository(northMessagesByUserIntervalRepository, from, to, 4);
        return northMessagesByUserIntervalAccessor.getMessagesByUserAndIntervalList(user, interval).all();
    }

    @Override
    public List<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject, Date from, Date to) {
        List<Long> interval = getBucketIntervalForRepository(northMessagesByUserSubjectIntervalRepository, from, to, 2);
        return northMessagesByUserSubjectIntervalAccessor.getMessagesByUserAndSubjectAndIntervalList(user, subject, interval).all();
    }

    private List<Long> getBucketIntervalForRepository(Repository repository, Date from, Date to, int maxSize) {
        List<Long> intervals = repository.getBucket().getIntervals(from, to);
        checkArgument(intervals.size() < maxSize, "specified time range is too big, be more specific");
        return intervals;
    }
}
