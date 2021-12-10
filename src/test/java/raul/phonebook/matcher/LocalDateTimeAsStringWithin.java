package raul.phonebook.matcher;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;

public class LocalDateTimeAsStringWithin extends TypeSafeMatcher<String> {

    private final long period;
    private final ChronoUnit unit;
    private final LocalDateTime date;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public LocalDateTimeAsStringWithin(long period,
                                       ChronoUnit unit,
                                       LocalDateTime date) {
        this.period = period;
        this.unit = unit;
        this.date = date;
    }

    @Override
    public boolean matchesSafely(String localDateTimeAsString) {
        try {
            var localDateTime = LocalDateTime.parse(localDateTimeAsString, dateFormatter);
            return LocalDateTimeMatchers.within(period, unit, date).matches(localDateTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(" is within " + period + " " + unit.toString() + " of " + date.toString());
    }

}
