package es.amplia.model.builder;

import es.amplia.model.AuditMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuditMessageBuilder {

    public static AuditMessageBuilder builder() {
        return new AuditMessageBuilder();
    }

    private final AuditMessage auditMessage = new AuditMessage();

    public final AuditMessage build () {
        return auditMessage;
    }

    public AuditMessageBuilder process(AuditMessage.ProcessType process) {
        auditMessage.setProcess(process);
        return this;
    }

    public AuditMessageBuilder component(AuditMessage.ComponentType component) {
        auditMessage.setComponent(component);
        return this;
    }

    public AuditMessageBuilder msgName(AuditMessage.NameType msgName) {
        auditMessage.setMsgName(msgName);
        return this;
    }

    public AuditMessageBuilder msgType(AuditMessage.MsgType msgType) {
        auditMessage.setMsgType(msgType);
        return this;
    }

    public AuditMessageBuilder msgDirection(AuditMessage.MsgDirection msgDirection) {
        auditMessage.setMsgDirection(msgDirection);
        return this;
    }

    public AuditMessageBuilder subject(String subject) {
        auditMessage.setSubject(subject);
        return this;
    }

    public AuditMessageBuilder subjectType(AuditMessage.SubjectType subjectType) {
        auditMessage.setSubjectType(subjectType);
        return this;
    }

    public AuditMessageBuilder user(String user) {
        auditMessage.setUser(user);
        return this;
    }

    public AuditMessageBuilder localCorrelationId(String localCorrelationId) {
        auditMessage.setLocalCorrelationId(localCorrelationId);
        return this;
    }

    public AuditMessageBuilder globalCorrelationId(String globalCorrelationId) {
        auditMessage.setGlobalCorrelationId(globalCorrelationId);
        return this;
    }

    public AuditMessageBuilder sequenceId(String sequenceId) {
        auditMessage.setSequenceId(sequenceId);
        return this;
    }

    public AuditMessageBuilder msgStatus(AuditMessage.MsgStatus msgStatus) {
        auditMessage.setMsgStatus(msgStatus);
        return this;
    }

    public AuditMessageBuilder secured(Boolean secured) {
        auditMessage.setSecured(secured);
        return this;
    }

    public AuditMessageBuilder msgSizeBytes(int msgSizeBytes) {
        auditMessage.setMsgSizeBytes(msgSizeBytes);
        return this;
    }

    public AuditMessageBuilder msgContext(Map<String, String> msgContext) {
        checkNotNull(msgContext);
        auditMessage.setMsgContext(new HashMap<>(msgContext));
        return this;
    }

    public AuditMessageBuilder msgPayload(List<String> msgPayload) {
        checkNotNull(msgPayload);
        auditMessage.setMsgPayload(msgPayload);
        return this;
    }

    public AuditMessageBuilder timestamp(Date timestamp) {
        auditMessage.setTimestamp(timestamp);
        return this;
    }

    public AuditMessageBuilder version(int version) {
        auditMessage.setVersion(version);
        return this;
    }
}
