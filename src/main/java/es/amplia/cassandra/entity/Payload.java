package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;
import es.amplia.model.AuditMessage;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static es.amplia.cassandra.entity.Entity.Names.KEYSPACE;
import static es.amplia.cassandra.entity.Payload.Names.*;

@Table(keyspace = KEYSPACE, name = PAYLOAD_BY_ID_TABLE)
public class Payload implements Entity {

    public interface Names {
        String PAYLOAD_BY_ID_TABLE = "payloads_by_id";

        String ID_FIELD = "id";
        String MSG_PAYLOAD_FIELD = "msg_payload";
    }

    @PartitionKey()
    @Column(name = ID_FIELD)
    private UUID id;

    @Column(name = MSG_PAYLOAD_FIELD)
    private List<String> msgPayload;

    public UUID getId() {
        return id;
    }

    public void setId(UUID auditId) {
        this.id = auditId;
    }

    public List<String> getMsgPayload() {
        return msgPayload;
    }

    public void setMsgPayload(List<String> msgPayload) {
        this.msgPayload = msgPayload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payload)) return false;
        Payload that = (Payload) o;
        return equal(id, that.id) &&
                equal(msgPayload, that.msgPayload);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, msgPayload);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("id", id)
                .add("msgPayload", msgPayload)
                .omitNullValues()
                .toString();
    }

    public static class PayloadBuilder {

        private final Payload payload = new Payload();

        public static PayloadBuilder builder() {
            return new PayloadBuilder();
        }

        public final Payload build() {
            return payload;
        }

        public final Payload build(AuditMessage auditMessage) {
            return msgPayload(auditMessage.getMsgPayload()).build();
        }

        public PayloadBuilder id(UUID id) {
            payload.setId(id);
            return this;
        }

        public PayloadBuilder msgPayload(List<String> msgPayload) {
            payload.setMsgPayload(msgPayload);
            return this;
        }
    }
}
