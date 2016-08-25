package es.amplia.cassandra.bucket;

import java.util.Collection;
import java.util.Date;

public interface Bucket {

    long getInterval(Date timestamp);
    Collection<Long> getIntervals(Date start, Date end);
}
