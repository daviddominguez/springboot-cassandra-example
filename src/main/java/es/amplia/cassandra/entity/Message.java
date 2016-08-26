package es.amplia.cassandra.entity;

import es.amplia.model.AuditMessage.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface Message {

    interface Names {
        String KEYSPACE = "audit";
        String NORTH_MESSAGES_BY_INTERVAL_TABLE = "north_messages_by_interval";
        String NORTH_MESSAGES_BY_USER_INTERVAL_TABLE = "north_messages_by_user_and_interval";
        String NORTH_MESSAGES_BY_USER_SUBJECT_INTERVAL_TABLE = "north_messages_by_user_subject_and_interval";
        String INTERVAL_FIELD = "interval";
        String AUDIT_ID_FIELD = "audit_id";
        String COMPONENT_TYPE_FIELD = "component_type";
        String MSG_NAME_FIELD = "msg_name";
        String MSG_TYPE_FIELD = "msg_type";
        String MSG_DIRECTION_FIELD = "msg_direction";
        String SUBJECT_FIELD = "subject";
        String SUBJECT_TYPE_FIELD = "subject_type";
        String USER_FIELD = "user";
        String TRANSACTION_ID_FIELD = "transaction_id";
        String SEQUENCE_ID_FIELD = "sequence_id";
        String MSG_STATUS_FIELD = "msg_status";
        String MSG_SIZE_BYTES_FIELD = "msg_size_bytes";
        String MSG_CONTEXT_FIELD = "msg_context";
        String OCCUR_TIME_FIELD = "occur_time";
    }

    long getInterval();

    void setInterval(long interval);

    UUID getAuditId();

    void setAuditId(UUID auditId);

    ComponentType getComponentType();

    void setComponentType(ComponentType componentType);

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

    Date getOccurTime();

    void setOccurTime(Date occurTime);
}
