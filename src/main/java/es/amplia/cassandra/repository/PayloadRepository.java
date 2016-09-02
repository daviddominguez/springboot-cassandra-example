package es.amplia.cassandra.repository;

import com.datastax.driver.mapping.MappingManager;
import es.amplia.cassandra.entity.Payload;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Repository
public class PayloadRepository extends Repository<Payload> {

    @Autowired
    public PayloadRepository(MappingManager mappingManager) {
        super(mappingManager, Payload.class);
    }
}
