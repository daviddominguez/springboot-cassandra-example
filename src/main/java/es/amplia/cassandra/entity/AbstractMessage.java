package es.amplia.cassandra.entity;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.google.common.base.Objects;
import es.amplia.cassandra.codec.MsgContextCodec;
import es.amplia.model.AuditMessage;
import es.amplia.model.AuditMessage.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;
import static es.amplia.cassandra.entity.Message.Names.*;

abstract class AbstractMessage implements Message {

    private long interval;

    private UUID auditId;

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

    private Date occurTime;

    @PartitionKey(0)
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
    @Column(name = AUDIT_ID_FIELD)
    @Override
    public UUID getAuditId() {
        return auditId;
    }

    @Override
    public void setAuditId(UUID auditId) {
        this.auditId = auditId;
    }

    @Column(name = COMPONENT_TYPE_FIELD)
    @Override
    public ComponentType getComponentType() {
        return componentType;
    }

    @Override
    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    @Column(name = MSG_NAME_FIELD)
    @Override
    public NameType getMsgName() {
        return msgName;
    }

    @Override
    public void setMsgName(NameType msgName) {
        this.msgName = msgName;
    }

    @Column(name = MSG_TYPE_FIELD)
    @Override
    public MsgType getMsgType() {
        return msgType;
    }

    @Override
    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    @Column(name = MSG_DIRECTION_FIELD)
    @Override
    public MsgDirection getMsgDirection() {
        return msgDirection;
    }

    @Override
    public void setMsgDirection(MsgDirection msgDirection) {
        this.msgDirection = msgDirection;
    }

    @Column(name = SUBJECT_FIELD)
    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = SUBJECT_TYPE_FIELD)
    @Override
    public SubjectType getSubjectType() {
        return subjectType;
    }

    @Override
    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Column(name = USER_FIELD)
    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = LOCAL_CORRELATION_ID_FIELD)
    @Override
    public String getLocalCorrelationId() {
        return localCorrelationId;
    }

    @Override
    public void setLocalCorrelationId(String localCorrelationId) {
        this.localCorrelationId = localCorrelationId;
    }

    @Column(name = GLOBAL_CORRELATION_ID_FIELD)
    @Override
    public String getGlobalCorrelationId() {
        return globalCorrelationId;
    }

    @Override
    public void setGlobalCorrelationId(String globalCorrelationId) {
        this.globalCorrelationId = globalCorrelationId;
    }

    @Column(name = SEQUENCE_ID_FIELD)
    @Override
    public String getSequenceId() {
        return sequenceId;
    }

    @Override
    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Column(name = MSG_STATUS_FIELD)
    @Override
    public MsgStatus getMsgStatus() {
        return msgStatus;
    }

    @Override
    public void setMsgStatus(MsgStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    @Column(name = SECURED_FIELD)
    @Override
    public Boolean getSecured() {
        return secured;
    }

    @Override
    public void setSecured(Boolean secured) {
        this.secured = secured;
    }

    @Column(name = MSG_SIZE_BYTES_FIELD)
    @Override
    public int getMsgSizeBytes() {
        return msgSizeBytes;
    }

    @Override
    public void setMsgSizeBytes(int msgSizeBytes) {
        this.msgSizeBytes = msgSizeBytes;
    }

    @Column(name = MSG_CONTEXT_FIELD, codec = MsgContextCodec.class)
    @Override
    public Map<String, String> getMsgContext() {
        return msgContext;
    }

    @Override
    public void setMsgContext(Map<String, String> msgContext) {
        this.msgContext = msgContext;
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
        return Objects.hashCode(interval, auditId, componentType, msgName, msgType, msgDirection, subject,
                subjectType, user, localCorrelationId, globalCorrelationId, sequenceId, msgStatus, secured,
                msgSizeBytes, msgContext, occurTime);
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

        public AbstractMessageBuilder localCorrelationId(String localCorrelationId) {
            message.setLocalCorrelationId(localCorrelationId);
            return this;
        }

        public AbstractMessageBuilder globalCorrelationId(String globalCorrelationId) {
            message.setGlobalCorrelationId(globalCorrelationId);
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

        public AbstractMessageBuilder secured(Boolean secured) {
            message.setSecured(secured);
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
