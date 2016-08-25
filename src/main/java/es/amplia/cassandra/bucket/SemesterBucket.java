package es.amplia.cassandra.bucket;

import org.joda.time.LocalDate;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.Months.monthsBetween;

public class SemesterBucket extends AbstractBucket {

    @Override
    public long getInterval(Date timestamp) {
        checkNotNull(timestamp, "date cannot be null");
        return monthsBetween(new LocalDate(0), new LocalDate(timestamp.getTime())).dividedBy(6).plus(1).getMonths();
    }
}
