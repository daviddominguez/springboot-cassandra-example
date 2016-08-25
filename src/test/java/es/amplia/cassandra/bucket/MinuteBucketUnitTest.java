package es.amplia.cassandra.bucket;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MinuteBucketUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    private Bucket bucket = new MinuteBucket();

    @Test
    public void given_a_null_date_when_get_interval_invoked_on_bucker_then_exception_thrown () {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("date cannot be null");

        when_get_interval_invoked_on_bucket(bucket, null);
    }

    @Test
    public void given_a_couple_of_dates_when_get_intervals_invoked_swapping_dates_then_exception_thrown () {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("end date must be after start date");

        when_get_intervals_invoked_on_bucket(bucket, new Date(), new Date(0));
    }

    @Test
    public void given_several_ranges_of_dates_when_get_intervals_invoked_on_bucket_then_it_returns_expected_interval_between_them () throws ParseException {
        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 0:00:00.000")),
                contains(1L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 0:20:59.999")),
                contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 23:59:59.999"), format.parse("02/01/1970 0:00:59.999")),
                contains(1440L, 1441L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket,  format.parse("23/08/2016 10:10:00.000"), format.parse("23/08/2016 10:15:59.999")),
                contains(24532451L, 24532452L, 24532453L, 24532454L, 24532455L, 24532456L));
    }

    @Test
    public void given_a_collection_of_dates_when_get_interval_invoked_on_bucket_then_it_returns_expected_interva () throws ParseException {
        Map<Long, Date> dates = given_a_collection_of_dates();

        for (Map.Entry<Long, Date> dateAndInterval : dates.entrySet()) {
            Long expectedInterval = dateAndInterval.getKey();
            Date dateToTest = dateAndInterval.getValue();

            assertThat(when_get_interval_invoked_on_bucket(bucket, dateToTest), is(expectedInterval));
        }
    }

    private Map<Long, Date> given_a_collection_of_dates () throws ParseException {
        Map<Long, Date> map = new HashMap<>();
        map.put(1L, format.parse("01/01/1970 0:00:00.000"));
        map.put(1L, format.parse("01/01/1970 0:00:59.999"));
        map.put(2L, format.parse("01/01/1970 0:01:00.000"));
        map.put(2L, format.parse("01/01/1970 0:01:59.999"));
        map.put(3L, format.parse("01/01/1970 0:02:00.000"));
        map.put(3L, format.parse("01/01/1970 0:02:59.999"));
        map.put(60L, format.parse("01/01/1970 0:59:00.000"));
        map.put(60L, format.parse("01/01/1970 0:59:59.999"));
        map.put(61L, format.parse("01/01/1970 1:00:00.000"));
        map.put(61L, format.parse("01/01/1970 1:00:59.999"));
        map.put(24531841L, format.parse("23/08/2016 0:00:00.000"));
        return map;
    }

    private long when_get_interval_invoked_on_bucket(Bucket bucket, Date dateToTest) {
        return bucket.getInterval(dateToTest);
    }

    private Collection<Long> when_get_intervals_invoked_on_bucket(Bucket bucket, Date start, Date end) {
        return bucket.getIntervals(start, end);
    }
}
