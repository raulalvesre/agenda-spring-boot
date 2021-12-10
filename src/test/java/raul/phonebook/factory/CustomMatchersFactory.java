package raul.phonebook.factory;

import net.minidev.json.JSONArray;
import org.hamcrest.Matcher;
import raul.phonebook.matcher.JSONArrayOfNulls;
import raul.phonebook.matcher.LocalDateTimeAsStringWithin;
import raul.phonebook.matcher.LocalDateTimesAsJSONArrayWithin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;

public class CustomMatchersFactory {

    public static Matcher<JSONArray> isAJSONArrayOfNulls() {
        return is(new JSONArrayOfNulls());
    }

    public static Matcher<String> localDateTimeAsStringIsWithin(long period,
                                                                ChronoUnit unit,
                                                                LocalDateTime date) {
        return is(new LocalDateTimeAsStringWithin(period, unit, date));
    }

    public static Matcher<JSONArray> localDateTimesAsJSONArrayAreWithin(long period,
                                                                        ChronoUnit unit,
                                                                        LocalDateTime date) {
        return is(new LocalDateTimesAsJSONArrayWithin(period, unit, date));
    }

}
