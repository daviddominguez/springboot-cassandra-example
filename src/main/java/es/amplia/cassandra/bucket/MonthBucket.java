package es.amplia.cassandra.bucket;

import org.joda.time.LocalDate;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.Months.monthsBetween;

public class MonthBucket extends AbstractBucket {

    @Override
    public long getInterval(Date timestamp) {
        checkNotNull(timestamp, "date cannot be null");
        return monthsBetween(new LocalDate(0), new LocalDate(timestamp.getTime())).plus(1).getMonths();
    }
}
