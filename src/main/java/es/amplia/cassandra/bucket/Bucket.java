package es.amplia.cassandra.bucket;

import java.util.Date;
import java.util.List;

public interface Bucket {

    long getInterval(Date timestamp);
    List<Long> getIntervals(Date start, Date end);
}
