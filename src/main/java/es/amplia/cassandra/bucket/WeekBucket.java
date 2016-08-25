package es.amplia.cassandra.bucket;

import org.joda.time.LocalDate;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.Weeks.weeksBetween;

public class WeekBucket extends AbstractBucket {

    @Override
    public long getInterval(Date timestamp) {
        checkNotNull(timestamp, "date cannot be null");
        // 342000000 is 05/01/1970, the first 1970's week day.
        return weeksBetween(new LocalDate(342000000), new LocalDate(timestamp.getTime())).plus(1).getWeeks();
    }
}
