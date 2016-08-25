package es.amplia.cassandra.bucket;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.Minutes.minutesBetween;

public class MinuteBucket extends AbstractBucket {

    @Override
    public long getInterval(Date timestamp) {
        checkNotNull(timestamp, "date cannot be null");
        return minutesBetween(new LocalDateTime(0, DateTimeZone.UTC), new LocalDateTime(timestamp.getTime())).plus(1).getMinutes();
    }
}
