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

public class TenMinutesBucketUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
    private Bucket bucket = new TenMinutesBucket();

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

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 0:09:59.999")),
                contains(1L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 0:10:00.000")),
                contains(1L, 2L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket, format.parse("01/01/1970 0:00:00.000"), format.parse("01/01/1970 0:19:59.999")),
                contains(1L, 2L));

        assertThat(when_get_intervals_invoked_on_bucket(bucket,  format.parse("23/08/2016 0:00:00.000"), format.parse("23/08/2016 1:00:00.000")),
                contains(2453185L, 2453186L, 2453187L, 2453188L, 2453189L, 2453190L, 2453191L));
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
        map.put(1L, format.parse("01/01/1970 0:09:09.999"));
        map.put(2L, format.parse("01/01/1970 0:10:00.000"));
        map.put(2L, format.parse("01/01/1970 0:19:59.999"));
        map.put(3L, format.parse("01/01/1970 0:20:00.000"));
        map.put(3L, format.parse("01/01/1970 0:29:59.999"));
        map.put(6L, format.parse("01/01/1970 0:59:00.000"));
        map.put(6L, format.parse("01/01/1970 0:59:59.999"));
        map.put(7L, format.parse("01/01/1970 1:00:00.000"));
        map.put(7L, format.parse("01/01/1970 1:09:59.999"));
        map.put(2453185L, format.parse("23/08/2016 0:00:00.000"));
        return map;
    }

    private long when_get_interval_invoked_on_bucket(Bucket bucket, Date dateToTest) {
        return bucket.getInterval(dateToTest);
    }

    private Collection<Long> when_get_intervals_invoked_on_bucket(Bucket bucket, Date start, Date end) {
        return bucket.getIntervals(start, end);
    }
}
