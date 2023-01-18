package com.sitspl.crudbase.infra.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static StringUtils me;

    private final static String TOKEN_BOUND = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final static String DIGIT_BOUND = "0123456789";

    private static Pattern[] patterns = new Pattern[] {
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE), Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // bracket remove...
            Pattern.compile("<"), Pattern.compile(">"), Pattern.compile("<>"), Pattern.compile("^>"),
            // other pattern...
            Pattern.compile("<([^>]*?)(?=<|$)"), Pattern.compile("(^|>)([^<]*?)(?=>)"), Pattern.compile("<([^>]*?)(?=<|$)"), Pattern.compile("(^|>)([^<]*?)(?=>)"), Pattern.compile("&lt;"), Pattern.compile("&gt;") };

    public static StringUtils getInstance() {
        if (me == null)
            me = new StringUtils();

        return me;
    }

    public boolean hasValue(String string) {
        boolean flag = false;
        if (string != null && !string.isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public String extractOnlyAlphanumeric(String string) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            String c = new Character(string.charAt(i)).toString();
            if (TOKEN_BOUND.contains(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String removeString(String original, String toRemove) {
        if (original.contains(toRemove))
            return original.substring(0, original.indexOf(toRemove)) + original.substring(original.indexOf(toRemove) + toRemove.length(), original.length());

        else
            return original;
    }

    public String replace(String originalContent, String toReplace, String replaceWith) {
        StringBuffer ret = new StringBuffer(originalContent);

        int indexes[] = getIndexes(originalContent, toReplace);
        if (indexes.length > 0) {
            for (int i = indexes.length - 1; i >= 0; i--) {
                ret.replace(0, ret.length(), ret.substring(0, indexes[i]) + replaceWith + ret.substring(indexes[i] + toReplace.length(), ret.length()));
            }
        }

        return ret.toString();
    }

    public StringBuffer replace(StringBuffer ret, String toReplace, String replaceWith) {
        int indexes[] = getIndexes(ret.toString(), toReplace);
        if (indexes.length > 0) {
            for (int i = indexes.length - 1; i >= 0; i--) {
                ret.replace(0, ret.length(), ret.substring(0, indexes[i]) + replaceWith + ret.substring(indexes[i] + toReplace.length(), ret.length()));
            }
        }

        return ret;
    }

    public int[] getIndexes(String content, String token) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        // go thru content
        for (int i = 0; i < content.length(); i++) {
            // look for token in content, with starting position i
            int index = content.indexOf(token, i);
            // if found,
            if (index != -1) {
                // add the index to arraylist
                indexes.add(new Integer(index));
                // shift search position forward
                i = index;
            }
        }

        // convert to int
        int[] ret = new int[indexes.size()];
        for (int i = 0; i < indexes.size(); i++)
            ret[i] = ((Integer) indexes.get(i)).intValue();

        indexes = null;

        return ret;
    }

    public String getCappedString(String str, Integer len) {
        if (str != null && str.length() > len) {
            return str.substring(0, len) + "...";
        }

        return str;
    }

    public String removeRestrictedCharactersForURLs(String str) {
        char replaceWith = '-';
        if (str.contains("\""))
            str = str.replace('\"', replaceWith);
        if (str.contains("\'"))
            str = str.replace('\'', replaceWith);
        if (str.contains(","))
            str = str.replace(',', replaceWith);
        if (str.contains("?"))
            str = str.replace('?', replaceWith);
        if (str.contains("/"))
            str = str.replace('/', replaceWith);
        if (str.contains("!"))
            str = str.replace('!', replaceWith);
        if (str.contains("@"))
            str = str.replace('@', replaceWith);
        if (str.contains("#"))
            str = str.replace('#', replaceWith);
        if (str.contains("$"))
            str = str.replace('$', replaceWith);
        if (str.contains("%"))
            str = str.replace('%', replaceWith);
        if (str.contains("^"))
            str = str.replace('^', replaceWith);
        if (str.contains("&"))
            str = str.replace('&', replaceWith);
        if (str.contains("*"))
            str = str.replace('*', replaceWith);
        if (str.contains(" "))
            str = str.replace(' ', replaceWith);

        return str;
    }

    public String removeRestrictedCharactersForFilenames(String str) {
        if (str != null) {
            if (str.contains(" "))
                str = str.replace(' ', '_');
            if (str.contains("\'"))
                str = str.replace('\'', '_');
            if (str.contains(","))
                str = str.replace(',', '_');
            if (str.contains("?"))
                str = str.replace('?', '_');
            if (str.contains("/"))
                str = str.replace('/', '_');
            if (str.contains("!"))
                str = str.replace('!', '_');
            if (str.contains("@"))
                str = str.replace('@', '_');
            if (str.contains("#"))
                str = str.replace('#', '_');
            if (str.contains("$"))
                str = str.replace('$', '_');
            if (str.contains("%"))
                str = str.replace('%', '_');
            if (str.contains("^"))
                str = str.replace('^', '_');
            if (str.contains("&"))
                str = str.replace('&', '_');
            if (str.contains("*"))
                str = str.replace('*', '_');
        }
        return str;
    }

    public String removeSquareBrackets(String string) {
        // remove []
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);
            if (currentChar != '[' || currentChar != ']') {
                sb.append(currentChar);
            }
        }

        return sb.toString();
    }

    public String removeSingleQuote(String string) {
        return removeCharacter(string, '\'');
    }

    public String removeCharacter(String string, char character) {
        if (string != null) {
            if (string.indexOf(character) == -1) {
                return string;
            } else {
                StringBuffer ret = new StringBuffer();
                for (int i = 0; i < string.length(); i++) {
                    if (string.charAt(i) != character)
                        ret.append(string.charAt(i));
                }

                return ret.toString();
            }
        } else
            return string;
    }

    public String getLastToken(String string, String delim) {
        int[] indexes = getIndexes(string, delim);
        return string.substring(indexes[indexes.length - 1] + 1, string.length());
    }

    public String getFirstToken(String string, String delim) {
        return string.substring(0, string.indexOf(delim));
    }

    public String getLastTokenExcluded(String string, String delim) {
        int[] indexes = getIndexes(string, delim);
        return string.substring(0, indexes[indexes.length - 1]);
    }

    public String generateRandomTokenInDigits(int gen_length, Random random) {
        StringBuffer sb = new StringBuffer();
        int len = DIGIT_BOUND.length();
        for (int i = 0; i < gen_length; i++) {
            sb.append(DIGIT_BOUND.charAt(random.nextInt(len)));
        }
        return sb.toString();
    }

    // ************************************** ARRAY METHODS
    // ****************************************
    public boolean findStringInStringArray(String string, String[] array) {
        boolean ret = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(string)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public String[] buildStringArrayFromString(String string, String delim) {
        if (string != null) {
            StringTokenizer st = new StringTokenizer(string, delim);
            String[] ret = new String[st.countTokens()];

            int count = 0;
            while (st.hasMoreTokens()) {
                ret[count] = st.nextToken();
                count++;
            }

            return ret;
        } else
            return null;
    }

    public String buildStringFromStringArray(String[] array, String delim) {
        StringBuffer ret = new StringBuffer();

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                ret.append(array[i]);

                if (i + 1 < array.length)
                    ret.append(delim);
            }
        }

        return ret.toString();
    }

    public List<String> buildListFromString(String string, String delim) {
        return buildListFromString(string, delim, false);
    }

    public List<String> buildListFromString(String string, String delim, boolean includeEmpty) {
        if (string == null)
            return null;
        List<String> ret = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(string, delim, includeEmpty);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (includeEmpty && token.equals(delim)) { // empty string
                ret.add(null);
                if (!st.hasMoreTokens())
                    ret.add(null); // if is last token, add one more
            } else {
                ret.add(token);
                if (includeEmpty && st.hasMoreTokens()) {
                    st.nextToken(); // take out delim
                    if (!st.hasMoreTokens())
                        ret.add(null); // if is last token, add one more
                }
            }
        }

        return ret;
    }

    public String buildStringFromList(Collection list, String delim) {
        StringBuffer ret = new StringBuffer();

        if (list != null) {
            for (Iterator itr = list.iterator(); itr.hasNext();) {
                ret.append(itr.next());

                if (itr.hasNext())
                    ret.append(delim);
            }
        }

        return ret.toString();
    }

    public String truncate(String text, Integer maxLen) {
        if (text.length() <= maxLen)
            return text;
        return text.substring(0, maxLen - 3) + "...";
    }

    public String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String replaceHrefValue(final String html, final String id) {
        String resp = null;
        Pattern pattern = Pattern.compile("<a id=\"" + id + "\"\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");
        Matcher matcher = pattern.matcher(html.trim());
        if (matcher.find()) {
            Pattern pattern1 = Pattern.compile("href=\"([^\"]*)\"");
            Matcher matcher1 = pattern1.matcher(matcher.group(0));
            if (matcher1.find()) {
                resp = matcher1.group(0);
            }
        }
        return resp;
    }

    public String stripXSS(String value) {
        if (value != null) {

            // Avoid null characters
            value = value.replaceAll("", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

    public String getTruncatedStringWithoutDots(String value, Integer maxLength) {
        if (hasValue(value) && maxLength != null) {
            if (value.length() > maxLength)
                return value.substring(0, maxLength);
            else
                return value;
        }

        return "";
    }

    public String getStringAcronym(String value, Integer maxLength) {
        StringBuilder stringForConcat = new StringBuilder("");
        if (hasValue(value) && maxLength != null) {
            if (value.length() > maxLength) {
                for (int i = 0; i < value.length(); i++) {
                    if (Character.isUpperCase(value.charAt(i))) {
                        stringForConcat.append(value.charAt(i));
                    }
                }
            } else
                stringForConcat.append(value);
        }

        return stringForConcat.toString();
    }

    public String trimMultipleSpace(String value) {
        return value == null ? value : value.trim().replaceAll("\\s+", " ");
    }
    
    public String getExceptionAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
