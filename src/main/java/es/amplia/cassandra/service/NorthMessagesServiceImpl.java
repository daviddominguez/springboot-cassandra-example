package es.amplia.cassandra.service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.entity.*;
import es.amplia.cassandra.entity.NorthMessageByInterval.NorthMessageByIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserInterval.NorthMessageByUserIntervalBuilder;
import es.amplia.cassandra.entity.NorthMessageByUserSubjectInterval.NorthMessageByUserSubjectIntervalBuilder;
import es.amplia.cassandra.repository.NorthMessagesByIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserIntervalRepository;
import es.amplia.cassandra.repository.NorthMessagesByUserSubjectIntervalRepository;
import es.amplia.cassandra.repository.PayloadRepository;
import es.amplia.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class NorthMessagesServiceImpl implements NorthMessagesService {

    private Session session;
    private NorthMessagesByIntervalRepository northMessagesByIntervalRepository;
    private NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository;
    private NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository;
    private PayloadRepository payloadRepository;

    @Autowired
    public NorthMessagesServiceImpl(
            Session session,
            NorthMessagesByIntervalRepository northMessagesByIntervalRepository,
            NorthMessagesByUserIntervalRepository northMessagesByUserIntervalRepository,
            NorthMessagesByUserSubjectIntervalRepository northMessagesByUserSubjectIntervalRepository,
            PayloadRepository payloadRepository
    ) {
        this.session = session;
        this.northMessagesByIntervalRepository = northMessagesByIntervalRepository;
        this.northMessagesByUserIntervalRepository = northMessagesByUserIntervalRepository;
        this.northMessagesByUserSubjectIntervalRepository = northMessagesByUserSubjectIntervalRepository;
        this.payloadRepository = payloadRepository;
    }

    @Override
    public void save(AuditMessage message) {
        BatchStatement batch = new BatchStatement();
        Payload payload = Payload.PayloadBuilder.builder().msgPayload(message.getMsgPayload()).build();
        batch.add(payloadRepository.saveQuery(payload));
        batch.add(northMessagesByIntervalRepository
                .saveQuery((NorthMessageByInterval) NorthMessageByIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(northMessagesByUserIntervalRepository
                .saveQuery((NorthMessageByUserInterval) NorthMessageByUserIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(northMessagesByUserSubjectIntervalRepository
                .saveQuery((NorthMessageByUserSubjectInterval) NorthMessageByUserSubjectIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        session.execute(batch);
    }

    @Override
    public Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState) {
        return northMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, null);
    }

    @Override
    public Page<NorthMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int fetchSize) {
        return northMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, fetchSize);
    }

    @Override
    public Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to,
                                                                      String pagingState) {
        return northMessagesByUserIntervalRepository.getMessagesByUserInterval(user, from, to, pagingState, null);
    }

    @Override
    public Page<NorthMessageByUserInterval> getMessagesByUserInterval(String user, Date from, Date to,
                                                                      String pagingState, int fetchSize) {
        return northMessagesByUserIntervalRepository.getMessagesByUserInterval(user, from, to, pagingState, fetchSize);
    }

    @Override
    public Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject,
                                                                                    Date from, Date to,
                                                                                    String pagingState) {
        return northMessagesByUserSubjectIntervalRepository.getMessagesByUserSubjectInterval(user, subject, from, to,
                pagingState, null);
    }

    @Override
    public Page<NorthMessageByUserSubjectInterval> getMessagesByUserSubjectInterval(String user, String subject,
                                                                                    Date from, Date to,
                                                                                    String pagingState, int fetchSize) {
        return northMessagesByUserSubjectIntervalRepository.getMessagesByUserSubjectInterval(user, subject, from, to,
                pagingState, fetchSize);
    }
}
