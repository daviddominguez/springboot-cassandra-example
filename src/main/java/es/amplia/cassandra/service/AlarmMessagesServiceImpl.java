package es.amplia.cassandra.service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import es.amplia.cassandra.entity.*;
import es.amplia.cassandra.entity.AlarmMessageByInterval.AlarmMessageByIntervalBuilder;
import es.amplia.cassandra.entity.AlarmMessageBySubjectInterval.AlarmMessageBySubjectIntervalBuilder;
import es.amplia.cassandra.entity.AlarmMessageBySubjectUserInterval.AlarmMessageBySubjectUserIntervalBuilder;
import es.amplia.cassandra.repository.AlarmMessagesByIntervalRepository;
import es.amplia.cassandra.repository.AlarmMessagesBySubjectIntervalRepository;
import es.amplia.cassandra.repository.AlarmMessagesBySubjectUserIntervalRepository;
import es.amplia.cassandra.repository.PayloadRepository;
import es.amplia.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class AlarmMessagesServiceImpl implements AlarmMessagesService {

    private Session session;
    private AlarmMessagesByIntervalRepository alarmMessagesByIntervalRepository;
    private AlarmMessagesBySubjectIntervalRepository alarmMessagesBySubjectIntervalRepository;
    private AlarmMessagesBySubjectUserIntervalRepository alarmMessagesBySubjectUserIntervalRepository;
    private PayloadRepository payloadRepository;

    @Autowired
    public AlarmMessagesServiceImpl(
            Session session,
            AlarmMessagesByIntervalRepository alarmMessagesByIntervalRepository,
            AlarmMessagesBySubjectIntervalRepository alarmMessagesBySubjectIntervalRepository,
            AlarmMessagesBySubjectUserIntervalRepository alarmMessagesBySubjectUserIntervalRepository,
            PayloadRepository payloadRepository
    ) {
        this.session = session;
        this.alarmMessagesByIntervalRepository = alarmMessagesByIntervalRepository;
        this.alarmMessagesBySubjectIntervalRepository = alarmMessagesBySubjectIntervalRepository;
        this.alarmMessagesBySubjectUserIntervalRepository = alarmMessagesBySubjectUserIntervalRepository;
        this.payloadRepository = payloadRepository;
    }

    @Override
    public void save(AuditMessage message) {
        BatchStatement batch = new BatchStatement();
        Payload payload = Payload.PayloadBuilder.builder().msgPayload(message.getMsgPayload()).build();
        batch.add(payloadRepository.saveQuery(payload));
        batch.add(alarmMessagesByIntervalRepository
                .saveQuery((AlarmMessageByInterval) AlarmMessageByIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(alarmMessagesBySubjectIntervalRepository
                .saveQuery((AlarmMessageBySubjectInterval) AlarmMessageBySubjectIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        batch.add(alarmMessagesBySubjectUserIntervalRepository
                .saveQuery((AlarmMessageBySubjectUserInterval) AlarmMessageBySubjectUserIntervalBuilder.builder()
                        .payloadId(payload.getId())
                        .build(message)));
        session.execute(batch);
    }

    @Override
    public Page<AlarmMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState) {
        return alarmMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, null);
    }

    @Override
    public Page<AlarmMessageByInterval> getMessagesByInterval(Date from, Date to, String pagingState, int fetchSize) {
        return alarmMessagesByIntervalRepository.getMessagesByInterval(from, to, pagingState, fetchSize);
    }

    @Override
    public Page<AlarmMessageBySubjectInterval> getMessagesBySubjectInterval(String user, Date from, Date to,
                                                                      String pagingState) {
        return alarmMessagesBySubjectIntervalRepository.getMessagesBySubjectInterval(user, from, to, pagingState, null);
    }

    @Override
    public Page<AlarmMessageBySubjectInterval> getMessagesBySubjectInterval(String subject, Date from, Date to,
                                                                      String pagingState, int fetchSize) {
        return alarmMessagesBySubjectIntervalRepository.getMessagesBySubjectInterval(subject, from, to, pagingState, fetchSize);
    }

    @Override
    public Page<AlarmMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState) {
        return alarmMessagesBySubjectUserIntervalRepository.getMessagesBySubjectUserInterval(subject, user, from, to,
                pagingState, null);
    }

    @Override
    public Page<AlarmMessageBySubjectUserInterval> getMessagesBySubjectUserInterval(String subject, String user,
                                                                                    Date from, Date to,
                                                                                    String pagingState, int fetchSize) {
        return alarmMessagesBySubjectUserIntervalRepository.getMessagesBySubjectUserInterval(subject, user, from, to,
                pagingState, fetchSize);
    }
}
