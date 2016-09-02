package es.amplia.cassandra.entity;

import java.util.Date;

public interface BucketEntity extends Entity {

    long getInterval();
    void setInterval(long interval);
    Date getOccurTime();
    void setOccurTime(Date occurTime);
}
