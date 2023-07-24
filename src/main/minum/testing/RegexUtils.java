package minum.testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handy helpers to make regular expression marginally
 * easier / more efficient, etc.
 */
public class RegexUtils {

    private RegexUtils() {
        // making this private to be clearer it isn't supposed to be instantiated.
    }

    /**
     * Makes it a bit easier to find a value in a string using a
     * Regex. Note, this is not nearly as performant, since
     * each call to this method will compile the regular
     * expression.
     * @return returns the first match found, or an empty string
     */
    public static String find(String regex, String data) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "";
    }

    /**
     * Makes it a bit easier to find a value in a string using a
     * Regex. Note, this is not nearly as performant, since
     * each call to this method will compile the regular
     * expression.
     * <p>
     *     This version is similar to {@link #find(String, String)} except
     *     that it allows you to specify a match group by name.
     *     For example, here's a regex with a named match group,
     *     in this example the name is "namevalue":
     * </p>
     * <p>
     *     <pre>
     *         {@code "\\bname\\b=\"(?<namevalue>.*?)\""}
     *     </pre>
     * </p>
     * <p>
     *     Thus, to use it here, you would search like this:
     * </p>
     * <p>
     *     <pre>
     *         {@code find("\\bname\\b=\"(?<namevalue>.*?)\"", "namevalue", data)}
     *     </pre>
     * </p>
     * <p>
     *     To summarize: in a regex, you specify a matching group by
     *     surrounding it with parentheses.  To name it, you insert
     *     right after the opening parenthesis a question mark and
     *     then a string literal surrounded by angle brackets
     * </p>
     * <p><b>Important!!!</b>: the name of the match group must be alphanumeric - do
     * <b>not</b> use any special characters or punctuation</p>
     * @return returns the first match found, or an empty string
     */
    public static String find(String regex, String matchGroupName, String data) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(matchGroupName) : "";
    }
}
