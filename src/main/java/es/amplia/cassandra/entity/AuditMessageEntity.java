package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.google.common.base.Objects;
import es.amplia.cassandra.codec.MsgContextCodec;
import es.amplia.model.AuditMessage;
import es.amplia.model.AuditMessage.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static es.amplia.cassandra.entity.AuditMessageEntity.Names.*;

public abstract class AuditMessageEntity implements BucketEntity {

    public interface Names {
        String NORTH_MESSAGES_BY_INTERVAL_TABLE = "north_messages_by_interval";
        String NORTH_MESSAGES_BY_USER_INTERVAL_TABLE = "north_messages_by_user_and_interval";
        String NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE = "north_messages_by_user_subject_and_interval";
        String SOUTH_MESSAGES_BY_INTERVAL_TABLE = "south_messages_by_interval";
        String SOUTH_MESSAGES_BY_SUBJECT_INTERVAL_TABLE = "south_messages_by_subject_and_interval";
        String SOUTH_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE = "south_messages_by_subject_user_and_interval";
        String ALARM_MESSAGES_BY_INTERVAL_TABLE = "alarm_messages_by_interval";
        String ALARM_MESSAGES_BY_SUBJECT_INTERVAL_TABLE = "alarm_messages_by_subject_and_interval";
        String ALARM_MESSAGES_BY_SUBJECT_USER_INTERVAL_TABLE = "alarm_messages_by_subject_user_and_interval";

        String INTERVAL_FIELD = "interval";
        String ID_FIELD = "id";
        String COMPONENT_TYPE_FIELD = "component_type";
        String MSG_NAME_FIELD = "msg_name";
        String MSG_TYPE_FIELD = "msg_type";
        String MSG_DIRECTION_FIELD = "msg_direction";
        String SUBJECT_FIELD = "subject";
        String SUBJECT_TYPE_FIELD = "subject_type";
        String USER_FIELD = "user";
        String LOCAL_CORRELATION_ID_FIELD = "local_correlation_id";
        String GLOBAL_CORRELATION_ID_FIELD = "global_correlation_id";
        String SEQUENCE_ID_FIELD = "sequence_id";
        String MSG_STATUS_FIELD = "msg_status";
        String SECURED_FIELD = "secured";
        String MSG_SIZE_BYTES_FIELD = "msg_size_bytes";
        String MSG_CONTEXT_FIELD = "msg_context";
        String PAYLOAD_ID_FIELD = "payload_id";
        String OCCUR_TIME_FIELD = "occur_time";
    }

    private long interval;

    private UUID id;

    private ComponentType componentType;

    private NameType msgName;

    private MsgType msgType;

    private MsgDirection msgDirection;

    private String subject;

    private SubjectType subjectType;

    private String user;

    private String localCorrelationId;

    private String globalCorrelationId;

    private String sequenceId;

    private MsgStatus msgStatus;

    private Boolean secured;

    private int msgSizeBytes;

    private Map<String, String> msgContext;

    private UUID payloadId;

    private Date occurTime;

    @PartitionKey()
    @Column(name = INTERVAL_FIELD)
    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public void setInterval(long interval) {
        this.interval = interval;
    }

    @ClusteringColumn(1)
    @Column(name = ID_FIELD)
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID auditId) {
        this.id = auditId;
    }

    @Column(name = COMPONENT_TYPE_FIELD)
    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    @Column(name = MSG_NAME_FIELD)
    public NameType getMsgName() {
        return msgName;
    }

    public void setMsgName(NameType msgName) {
        this.msgName = msgName;
    }

    @Column(name = MSG_TYPE_FIELD)
    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Column(name = MSG_DIRECTION_FIELD)
    public MsgDirection getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(MsgDirection msgDirection) {
        this.msgDirection = msgDirection;
    }

    @Column(name = SUBJECT_FIELD)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = SUBJECT_TYPE_FIELD)
    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Column(name = USER_FIELD)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = LOCAL_CORRELATION_ID_FIELD)
    public String getLocalCorrelationId() {
        return localCorrelationId;
    }

    public void setLocalCorrelationId(String localCorrelationId) {
        this.localCorrelationId = localCorrelationId;
    }

    @Column(name = GLOBAL_CORRELATION_ID_FIELD)
    public String getGlobalCorrelationId() {
        return globalCorrelationId;
    }

    public void setGlobalCorrelationId(String globalCorrelationId) {
        this.globalCorrelationId = globalCorrelationId;
    }

    @Column(name = SEQUENCE_ID_FIELD)
    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Column(name = MSG_STATUS_FIELD)
    public MsgStatus getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(MsgStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    @Column(name = SECURED_FIELD)
    public Boolean getSecured() {
        return secured;
    }

    public void setSecured(Boolean secured) {
        this.secured = secured;
    }

    @Column(name = MSG_SIZE_BYTES_FIELD)
    public int getMsgSizeBytes() {
        return msgSizeBytes;
    }

    public void setMsgSizeBytes(int msgSizeBytes) {
        this.msgSizeBytes = msgSizeBytes;
    }

    @Column(name = MSG_CONTEXT_FIELD, codec = MsgContextCodec.class)
    public Map<String, String> getMsgContext() {
        return msgContext;
    }

    public void setMsgContext(Map<String, String> msgContext) {
        this.msgContext = msgContext;
    }

    @Column(name = PAYLOAD_ID_FIELD)
    public UUID getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(UUID payloadId) {
        this.payloadId = payloadId;
    }

    @Column(name = OCCUR_TIME_FIELD)
    @ClusteringColumn()
    @Override
    public Date getOccurTime() {
        return occurTime;
    }

    @Override
    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessageEntity)) return false;
        AuditMessageEntity that = (AuditMessageEntity) o;
        return equal(interval, that.interval) &&
                equal(msgSizeBytes, that.msgSizeBytes) &&
                equal(id, that.id) &&
                equal(componentType, that.componentType) &&
                equal(msgName, that.msgName) &&
                equal(msgType, that.msgType) &&
                equal(msgDirection, that.msgDirection) &&
                equal(subject, that.subject) &&
                equal(subjectType, that.subjectType) &&
                equal(user, that.user) &&
                equal(localCorrelationId, that.localCorrelationId) &&
                equal(globalCorrelationId, that.globalCorrelationId) &&
                equal(sequenceId, that.sequenceId) &&
                equal(msgStatus, that.msgStatus) &&
                equal(secured, that.secured) &&
                equal(msgContext, that.msgContext) &&
                equal(occurTime, that.occurTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(interval, id, componentType, msgName, msgType, msgDirection, subject,
                subjectType, user, localCorrelationId, globalCorrelationId, sequenceId, msgStatus, secured,
                msgSizeBytes, msgContext, occurTime);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("interval", interval)
                .add("id", id)
                .add("componentType", componentType)
                .add("msgName", msgName)
                .add("msgType", msgType)
                .add("msgDirection", msgDirection)
                .add("subject", subject)
                .add("subjectType", subjectType)
                .add("user", user)
                .add("localCorrelationId", localCorrelationId)
                .add("globalCorrelationId", globalCorrelationId)
                .add("sequenceId", sequenceId)
                .add("msgStatus", msgStatus)
                .add("secured", secured)
                .add("msgSizeBytes", msgSizeBytes)
                .add("msgContext", msgContext)
                .add("occurTime", occurTime)
                .omitNullValues()
                .toString();
    }

    public static abstract class AuditMessageEntityBuilder {

        private final AuditMessageEntity entity;

        protected AuditMessageEntityBuilder() {
            entity = instantiateConcreteAuditMessageEntity();
        }

        protected AuditMessageEntity getEntity() {
            return entity;
        }

        protected abstract AuditMessageEntity instantiateConcreteAuditMessageEntity();

        public final AuditMessageEntity build() {
            return getEntity();
        }

        public final AuditMessageEntity build(AuditMessage auditMessage) {
            return this.componentType(auditMessage.getComponent())
                    .msgName(auditMessage.getMsgName())
                    .msgType(auditMessage.getMsgType())
                    .msgDirection(auditMessage.getMsgDirection())
                    .subject(auditMessage.getSubject())
                    .subjectType(auditMessage.getSubjectType())
                    .user(auditMessage.getUser())
                    .localCorrelationId(auditMessage.getLocalCorrelationId())
                    .globalCorrelationId(auditMessage.getGlobalCorrelationId())
                    .sequenceId(auditMessage.getSequenceId())
                    .msgStatus(auditMessage.getMsgStatus())
                    .secured(auditMessage.getSecured())
                    .msgSizeBytes(auditMessage.getMsgSizeBytes())
                    .msgContext(auditMessage.getMsgContext())
                    .occurTime(auditMessage.getTimestamp())
                    .build();
        }

        public AuditMessageEntityBuilder interval(long interval) {
            entity.setInterval(interval);
            return this;
        }

        public AuditMessageEntityBuilder id(UUID id) {
            entity.setId(id);
            return this;
        }

        public AuditMessageEntityBuilder componentType(ComponentType component) {
            entity.setComponentType(component);
            return this;
        }

        public AuditMessageEntityBuilder msgName(NameType msgName) {
            entity.setMsgName(msgName);
            return this;
        }

        public AuditMessageEntityBuilder msgType(MsgType msgType) {
            entity.setMsgType(msgType);
            return this;
        }

        public AuditMessageEntityBuilder msgDirection(MsgDirection msgDirection) {
            entity.setMsgDirection(msgDirection);
            return this;
        }

        public AuditMessageEntityBuilder subject(String subject) {
            entity.setSubject(subject);
            return this;
        }

        public AuditMessageEntityBuilder subjectType(SubjectType subjectType) {
            entity.setSubjectType(subjectType);
            return this;
        }

        public AuditMessageEntityBuilder user(String user) {
            entity.setUser(user);
            return this;
        }

        public AuditMessageEntityBuilder localCorrelationId(String localCorrelationId) {
            entity.setLocalCorrelationId(localCorrelationId);
            return this;
        }

        public AuditMessageEntityBuilder globalCorrelationId(String globalCorrelationId) {
            entity.setGlobalCorrelationId(globalCorrelationId);
            return this;
        }

        public AuditMessageEntityBuilder sequenceId(String sequenceId) {
            entity.setSequenceId(sequenceId);
            return this;
        }

        public AuditMessageEntityBuilder msgStatus(MsgStatus msgStatus) {
            entity.setMsgStatus(msgStatus);
            return this;
        }

        public AuditMessageEntityBuilder secured(Boolean secured) {
            entity.setSecured(secured);
            return this;
        }

        public AuditMessageEntityBuilder msgSizeBytes(int msgSizeBytes) {
            entity.setMsgSizeBytes(msgSizeBytes);
            return this;
        }

        public AuditMessageEntityBuilder msgContext(Map<String, String> msgContext) {
            entity.setMsgContext(msgContext);
            return this;
        }

        public AuditMessageEntityBuilder occurTime(Date timestamp) {
            entity.setOccurTime(timestamp);
            return this;
        }

        public AuditMessageEntityBuilder payloadId(UUID payloadId) {
            entity.setPayloadId(payloadId);
            return this;
        }
    }
}
