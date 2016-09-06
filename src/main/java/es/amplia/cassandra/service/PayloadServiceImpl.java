package es.amplia.cassandra.service;

import es.amplia.cassandra.entity.Payload;
import es.amplia.cassandra.repository.PayloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class PayloadServiceImpl implements PayloadService {

    private PayloadRepository payloadRepository;

    @Autowired
    public PayloadServiceImpl(PayloadRepository payloadRepository) {
        this.payloadRepository = payloadRepository;
    }

    @Override
    public Payload getMessagePayload(UUID id) {
        return payloadRepository.get(id);
    }
}
