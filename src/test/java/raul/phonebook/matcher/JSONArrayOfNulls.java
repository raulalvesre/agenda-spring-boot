package raul.phonebook.matcher;

import net.minidev.json.JSONArray;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

public class JSONArrayOfNulls extends TypeSafeMatcher<JSONArray> {

    @Override
    public boolean matchesSafely(JSONArray jsonArray) {
        for (var item : jsonArray) {
            if (item != null)
                return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(" all elements are null");
    }

}
