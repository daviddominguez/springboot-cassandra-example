package es.amplia.model;

import com.google.common.base.Objects;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public class AuditMessage {

    public enum ProcessType {CONNECTOR, REST_NORTH, ALARM}
    public enum ComponentType {RACO, PING_RELAY, REST, WEBSOCKET, MQTT, WS_JOB, WS_PROVISION, SNMP}
    public enum NameType {ACCT_START, PING_REQUEST, PING_REPLY, DMM, OPERATION, ACCESS_VALIDATION, REFRESH_PRESENCE, CHANGE_PRESENCE, DEVICES}
    public enum MsgType {EVENT, REQUEST, RESPONSE, CONNECTION, INSERT, UPDATE, DELETE, CALLBACK, NOTIFICATION}
    public enum MsgDirection {IN, OUT}
    public enum SubjectType {IMSI, DEVICE, JOB, IP}
    public enum MsgStatus {SUCCESS, ERROR}

    private ProcessType process;
    private ComponentType component;
    private NameType msgName;
    private MsgType msgType;
    private MsgDirection msgDirection;
    private String subject;
    private SubjectType subjectType;
    private String user;
    private String transactionId;
    private String sequenceId;
    private MsgStatus msgStatus;
    private int msgSizeBytes;
    private Map<String, String> msgContext;
    private Date timestamp;
    private int version;

    public ProcessType getProcess() {
        return process;
    }

    public void setProcess(ProcessType process) {
        this.process = process;
    }

    public ComponentType getComponent() {
        return component;
    }

    public void setComponent(ComponentType component) {
        this.component = component;
    }

    public NameType getMsgName() {
        return msgName;
    }

    public void setMsgName(NameType msgName) {
        this.msgName = msgName;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public MsgDirection getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(MsgDirection msgDirection) {
        this.msgDirection = msgDirection;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public MsgStatus getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(MsgStatus msgStatus) {
        this.msgStatus = msgStatus;
    }

    public int getMsgSizeBytes() {
        return msgSizeBytes;
    }

    public void setMsgSizeBytes(int msgSizeBytes) {
        this.msgSizeBytes = msgSizeBytes;
    }

    public Map<String, String> getMsgContext() {
        return msgContext;
    }

    public void setMsgContext(Map<String, String> msgContext) {
        this.msgContext = msgContext;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessage)) return false;
        AuditMessage that = (AuditMessage) o;
        return equal(msgSizeBytes, that.msgSizeBytes) &&
                equal(version, that.version) &&
                equal(process, that.process) &&
                equal(component, that.component) &&
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
                equal(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(process, component, msgName, msgType, msgDirection, subject, subjectType, user, transactionId,
                sequenceId, msgStatus, msgSizeBytes, msgContext, timestamp, version);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("process", process)
                .add("component", component)
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
                .add("timestamp", timestamp)
                .add("version", version)
                .omitNullValues()
                .toString();
    }
}
