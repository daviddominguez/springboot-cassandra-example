package es.amplia.cassandra.service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.entity.*;
import es.amplia.cassandra.entity.SouthMessageByInterval.SouthMessageByIntervalBuilder;
import es.amplia.cassandra.entity.SouthMessageBySubjectInterval.SouthMessageBySubjectIntervalBuilder;
import es.amplia.cassandra.entity.SouthMessageBySubjectUserInterval.SouthMessageBySubjectUserIntervalBuilder;
import es.amplia.cassandra.repository.PayloadRepository;
import es.amplia.cassandra.repository.SouthMessagesByIntervalRepository;
import es.amplia.cassandra.repository.SouthMessagesBySubjectIntervalRepository;
import es.amplia.cassandra.repository.SouthMessagesBySubjectUserIntervalRepository;
import es.amplia.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class SouthMessagesServiceImpl implements SouthMessagesService {

    private Session session;
    private SouthMessagesByIntervalRepository southMessagesByIntervalRepository;
    private SouthMessagesBySubjectIntervalRepository southMessagesBySubjectIntervalRepository;
    private SouthMessagesBySubjectUserIntervalRepository southMessagesBySubjectUserIntervalRepository;
    private PayloadRepository payloadRepository;

    @Autowired
    public SouthMessagesServiceImpl(
            Session session,
            SouthMessagesByIntervalRepository southMessagesByIntervalRepository,
            SouthMessagesBySubjectIntervalRepository southMessagesBySubjectIntervalRepository,
            SouthMessagesBySubjectUserIntervalRepository southMessagesBySubjectUserIntervalRepository,
            PayloadRepository payloadRepository
    ) {
        this.session = session;
        this.southMessagesByIntervalRepository = southMessagesByIntervalRepository;
        this.southMessagesBySubjectIntervalRepository = southMessagesBySubjectIntervalRepository;
        this.southMessagesBySubjectUserIntervalRepository = southMessagesBySubjectUserIntervalRepository;
        this.payloadRepository = payloadRepository;
    }

    @Override
    public void save(AuditMessage message) {
        BatchStatement batch = new BatchStatement();
        Payload payload = Payload.PayloadBuilder.builder().msgPayload(message.getMsgPayload()).build();
        batch.add(payloadRepository.saveQuery(payload));
        batch.add(southMessagesByIntervalRepository
                .saveQuery((SouthMessageByInterval) SouthMessageByIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(southMessagesBySubjectIntervalRepository
                .saveQuery((SouthMessageBySubjectInterval) SouthMessageBySubjectIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(southMessagesBySubjectUserIntervalRepository
                .saveQuery((SouthMessageBySubjectUserInterval) SouthMessageBySubjectUserIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        session.execute(batch);
    }

    @Override
    public Page<SouthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState) {
        return southMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, null);
    }

    @Override
    public Page<SouthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int fetchSize) {
        return southMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, fetchSize);
    }

    @Override
    public Page<SouthMessageBySubjectInterval> getMessagesBySubjectInterval(String user, Date from, Date to,
                                                                      String pagingState) {
        return southMessagesBySubjectIntervalRepository.getMessagesBySubjectInterval(user, from, to, pagingState, null);
    }

    @Override
    public Page<SouthMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to,
                                                                      String pagingState, int fetchSize) {
        return southMessagesBySubjectIntervalRepository.getMessagesBySubjectInterval(subject, from, to, pagingState, fetchSize);
    }

    @Override
    public Page<SouthMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState) {
        return southMessagesBySubjectUserIntervalRepository.getMessagesBySubjectUserInterval(subject, user, from, to,
                pagingState, null);
    }

    @Override
    public Page<SouthMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState, int fetchSize) {
        return southMessagesBySubjectUserIntervalRepository.getMessagesBySubjectUserInterval(subject, user, from, to,
                pagingState, fetchSize);
    }
}
