package es.amplia.cassandra.bucket;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

abstract class AbstractBucket implements Bucket {

    @Override
    public List<Long> getIntervals(Date start, Date end) {
        checkArgument(start.before(end) || start.equals(end), "end date must be after start date");
        return ContiguousSet.create(Range.closed(getInterval(start), getInterval(end)), DiscreteDomain.longs()).asList();
    }
}
