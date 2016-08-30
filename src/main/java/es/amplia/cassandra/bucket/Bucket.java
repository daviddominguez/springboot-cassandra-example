package es.amplia.cassandra.bucket;

import java.util.Date;
import java.util.List;

public interface Bucket {

    enum Type {MINUTE, TEN_MINUTES, HOUR, DAY, WEEK, MONTH, TRIMESTER, SEMESTER}

    long getInterval(Date timestamp);
    List<Long> getIntervals(Date start, Date end);
}
