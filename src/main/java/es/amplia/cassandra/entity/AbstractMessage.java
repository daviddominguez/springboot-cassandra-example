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
import static es.amplia.cassandra.entity.Message.Names.*;

abstract class AbstractMessage implements Message {

    @Column(name = INTERVAL_FIELD)
    private long interval;

    @Column(name = AUDIT_ID_FIELD)
    private UUID auditId;

    @Column(name = COMPONENT_TYPE_FIELD)
    private ComponentType componentType;

    @Column(name = MSG_NAME_FIELD)
    private NameType msgName;

    @Column(name = MSG_TYPE_FIELD)
    private MsgType msgType;

    @Column(name = MSG_DIRECTION_FIELD)
    private MsgDirection msgDirection;

    @Column(name = SUBJECT_FIELD)
    private String subject;

    @Column(name = SUBJECT_TYPE_FIELD)
    private SubjectType subjectType;

    @Column(name = USER_FIELD)
    private String user;

    @Column(name = TRANSACTION_ID_FIELD)
    private String transactionId;

    @Column(name = SEQUENCE_ID_FIELD)
    private String sequenceId;

    @Column(name = MSG_STATUS_FIELD)
    private MsgStatus msgStatus;

    @Column(name = MSG_SIZE_BYTES_FIELD)
    private int msgSizeBytes;

    @Column(name = MSG_CONTEXT_FIELD, codec = MsgContextCodec.class)
    private Map<String, String> msgContext;

    @Column(name = OCCUR_TIME_FIELD)
    private Date occurTime;

    @PartitionKey(0)
    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public void setInterval(long interval) {
        this.interval = interval;
    }

    @ClusteringColumn(2)
    @Override
    public UUID getAuditId() {
        return auditId;
    }

    @Override
    public void setAuditId(UUID auditId) {
        this.auditId = auditId;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    @Override
    public NameType getMsgName() {
        return msgName;
    }

    @Override
    public void setMsgName(NameType msgName) {
        this.msgName = msgName;
    }

    @Override
    public MsgType getMsgType() {
        return msgType;
    }

    @Override
    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Override
    public MsgDirection getMsgDirection() {
        return msgDirection;
    }

    @Override
    public void setMsgDirection(MsgDirection msgDirection) {
        this.msgDirection = msgDirection;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public SubjectType getSubjectType() {
        return subjectType;
    }

    @Override
    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String getSequenceId() {
        return sequenceId;
    }

    @Override
    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public MsgStatus getMsgStatus() {
        return msgStatus;
    }

    @Override
    public void setMsgStatus(MsgStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    @Override
    public int getMsgSizeBytes() {
        return msgSizeBytes;
    }

    @Override
    public void setMsgSizeBytes(int msgSizeBytes) {
        this.msgSizeBytes = msgSizeBytes;
    }

    @Override
    public Map<String, String> getMsgContext() {
        return msgContext;
    }

    @Override
    public void setMsgContext(Map<String, String> msgContext) {
        this.msgContext = msgContext;
    }

    @ClusteringColumn(1)
    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractMessage)) return false;
        AbstractMessage that = (AbstractMessage) o;
        return equal(interval, that.interval) &&
                equal(msgSizeBytes, that.msgSizeBytes) &&
                equal(auditId, that.auditId) &&
                equal(componentType, that.componentType) &&
                equal(msgName, that.msgName) &&
                equal(msgType, that.msgType) &&
                equal(msgDirection, that.msgDirection) &&
                equal(subject, that.subject) &&
                equal(subjectType, that.subjectType) &&
                equal(user, that.user) &&
                equal(transactionId, that.transactionId) &&
                equal(sequenceId, that.sequenceId) &&
                equal(msgStatus, that.msgStatus) &&
                equal(msgContext, that.msgContext) &&
                equal(occurTime, that.occurTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(interval, auditId, componentType, msgName, msgType, msgDirection, subject,
                subjectType, user, transactionId, sequenceId, msgStatus, msgSizeBytes, msgContext, occurTime);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("interval", interval)
                .add("auditId", auditId)
                .add("componentType", componentType)
                .add("msgName", msgName)
                .add("msgType", msgType)
                .add("msgDirection", msgDirection)
                .add("subject", subject)
                .add("subjectType", subjectType)
                .add("user", user)
                .add("transactionId", transactionId)
                .add("sequenceId", sequenceId)
                .add("msgStatus", msgStatus)
                .add("msgSizeBytes", msgSizeBytes)
                .add("msgContext", msgContext)
                .add("occurTime", occurTime)
                .omitNullValues()
                .toString();
    }

    static abstract class AbstractMessageBuilder {

        private final AbstractMessage message;

        protected AbstractMessageBuilder() {
            message = instantiateConcreteMessage();
        }

        protected AbstractMessage getMessage() {
            return message;
        }

        protected abstract AbstractMessage instantiateConcreteMessage();

        public final AbstractMessage build() {
            return getMessage();
        }

        public final AbstractMessage build(AuditMessage auditMessage) {
            return this.componentType(auditMessage.getComponent())
                    .msgName(auditMessage.getMsgName())
                    .msgType(auditMessage.getMsgType())
                    .msgDirection(auditMessage.getMsgDirection())
                    .subject(auditMessage.getSubject())
                    .subjectType(auditMessage.getSubjectType())
                    .user(auditMessage.getUser())
                    .transactionId(auditMessage.getTransactionId())
                    .sequenceId(auditMessage.getSequenceId())
                    .msgStatus(auditMessage.getMsgStatus())
                    .msgSizeBytes(auditMessage.getMsgSizeBytes())
                    .msgContext(auditMessage.getMsgContext())
                    .occurTime(auditMessage.getTimestamp())
                    .build();
        }

        public AbstractMessageBuilder interval(long interval) {
            message.setInterval(interval);
            return this;
        }

        public AbstractMessageBuilder auditId(UUID auditId) {
            message.setAuditId(auditId);
            return this;
        }

        public AbstractMessageBuilder componentType(ComponentType component) {
            message.setComponentType(component);
            return this;
        }

        public AbstractMessageBuilder msgName(NameType msgName) {
            message.setMsgName(msgName);
            return this;
        }

        public AbstractMessageBuilder msgType(MsgType msgType) {
            message.setMsgType(msgType);
            return this;
        }

        public AbstractMessageBuilder msgDirection(MsgDirection msgDirection) {
            message.setMsgDirection(msgDirection);
            return this;
        }

        public AbstractMessageBuilder subject(String subject) {
            message.setSubject(subject);
            return this;
        }

        public AbstractMessageBuilder subjectType(SubjectType subjectType) {
            message.setSubjectType(subjectType);
            return this;
        }

        public AbstractMessageBuilder user(String user) {
            message.setUser(user);
            return this;
        }

        public AbstractMessageBuilder transactionId(String transactionId) {
            message.setTransactionId(transactionId);
            return this;
        }

        public AbstractMessageBuilder sequenceId(String sequenceId) {
            message.setSequenceId(sequenceId);
            return this;
        }

        public AbstractMessageBuilder msgStatus(MsgStatus msgStatus) {
            message.setMsgStatus(msgStatus);
            return this;
        }

        public AbstractMessageBuilder msgSizeBytes(int msgSizeBytes) {
            message.setMsgSizeBytes(msgSizeBytes);
            return this;
        }

        public AbstractMessageBuilder msgContext(Map<String, String> msgContext) {
            message.setMsgContext(msgContext);
            return this;
        }

        public AbstractMessageBuilder occurTime(Date timestamp) {
            message.setOccurTime(timestamp);
            return this;
        }
    }
}
