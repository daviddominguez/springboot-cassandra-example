package es.amplia.cassandra.entity;

import es.amplia.model.AuditMessage.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface Message {

    long getInterval();

    void setInterval(long interval);

    UUID getAuditId();

    void setAuditId(UUID auditId);

    ComponentType getComponent();

    void setComponent(ComponentType component);

    NameType getMsgName();

    void setMsgName(NameType msgName);

    MsgType getMsgType();

    void setMsgType(MsgType msgType);

    MsgDirection getMsgDirection();

    void setMsgDirection(MsgDirection msgDirection);

    String getSubject();

    void setSubject(String subject);

    SubjectType getSubjectType();

    void setSubjectType(SubjectType subjectType);

    String getUser();

    void setUser(String user);

    String getTransactionId();

    void setTransactionId(String transactionId);

    String getSequenceId();

    void setSequenceId(String sequenceId);

    MsgStatus getMsgStatus();

    void setMsgStatus(MsgStatus msgStatus);

    int getMsgSizeBytes();

    void setMsgSizeBytes(int msgSizeBytes);

    Map<String, String> getMsgContext();

    void setMsgContext(Map<String, String> msgContext);

    Date getTimestamp();

    void setTimestamp(Date timestamp);
}
