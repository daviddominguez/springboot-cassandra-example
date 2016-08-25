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

public class DayBucketUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    private Bucket bucket = new DayBucket();

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
        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 23:59:59.999")),
                contains(1L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 23:59:59.999"), format.parse("02/01/1970 0:00:00.000")),
                contains(1L, 2L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("10/01/1970 0:00:00.000")),
                contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("15/01/1970 0:00:00.000"), format.parse("15/02/1970 0:00:00.000")),
                contains(15L, 16L, 17L, 18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L,
                        30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L, 43L, 44L, 45L, 46L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket,  format.parse("17/08/2016 0:00:00.000"), format.parse("20/08/2016 22:51:00.000")),
                contains(17031L,17032L,17033L,17034L));
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
        map.put(1L, format.parse("01/01/1970 23:59:59.999"));
        map.put(2L, format.parse("02/01/1970 0:00:00.000"));
        map.put(2L, format.parse("02/01/1970 23:59:59.999"));
        map.put(3L, format.parse("03/01/1970 0:00:00.000"));
        map.put(3L, format.parse("03/01/1970 23:59:59.999"));
        map.put(10L, format.parse("10/01/1970 0:00:00.000"));
        map.put(10L, format.parse("10/01/1970 23:59:59.999"));
        map.put(31L, format.parse("31/01/1970 0:00:00.000"));
        map.put(31L, format.parse("31/01/1970 23:59:59.999"));
        map.put(32L, format.parse("01/02/1970 0:00:00.000"));
        map.put(32L, format.parse("01/02/1970 23:59:59.999"));
        map.put(365L, format.parse("31/12/1970 0:00:00.000"));
        map.put(365L, format.parse("31/12/1970 23:59:59.999"));
        map.put(366L, format.parse("01/01/1971 0:00:00.000"));
        map.put(366L, format.parse("01/01/1971 23:59:59.999"));
        map.put(17037L, format.parse("23/08/2016 0:00:00.000"));
        map.put(17037L, format.parse("23/08/2016 23:59:59.999"));
        return map;
    }

    private long when_get_interval_invoked_on_bucket(Bucket bucket, Date dateToTest) {
        return bucket.getInterval(dateToTest);
    }

    private Collection<Long> when_get_intervals_invoked_on_bucket(Bucket bucket, Date start, Date end) {
        return bucket.getIntervals(start, end);
    }
}
