package raul.phonebook.matcher;

import net.minidev.json.JSONArray;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;

public class LocalDateTimesAsJSONArrayWithin extends TypeSafeMatcher<JSONArray> {

    private final long period;
    private final ChronoUnit unit;
    private final LocalDateTime date;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public LocalDateTimesAsJSONArrayWithin(long period,
                                           ChronoUnit unit,
                                           LocalDateTime date) {
        this.period = period;
        this.unit = unit;
        this.date = date;
    }

    @Override
    public boolean matchesSafely(JSONArray localDateTimeAsStringJSONArray) {
        try {
            var localDateTimes = localDateTimeAsStringJSONArray.stream()
                    .map(x -> LocalDateTime.parse(x.toString(), dateFormatter))
                    .collect(Collectors.toList());

            for (var localDateTime: localDateTimes)
                if (!LocalDateTimeMatchers.within(period, unit, date).matches(localDateTime))
                    return false;

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(" all dates are within " + period + " " + unit.toString() + " of " + date.toString());
    }

}
