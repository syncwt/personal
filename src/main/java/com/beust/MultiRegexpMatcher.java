package com.beust;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Multiple regexp replacements in a string with only one pass.
 *
 * <p>
 *
 * Specify the pairs of (regexp, replacement) by calling the add() method
 * for each pair, then call replaceString() to perform the replacements.
 * You can specify groups in the regexps like you would if you were performing a
 * single replace(): <code>mrm.add("fb-([\\d])", "FB-$1")</code>.
 *
 * <p>
 *
 * How this class works: by creating a big "or" regexp of all your regexps
 * and by substituing all the groups with numbers that will make sense in
 * this new regexp. Then replaceString() goes through every occurrence returned
 * by the matcher, looks up which regexp was matched, performs the individual
 * replace and moves to the next match, if any.
 *
 * <p>
 *
 * Known issue: won't work for regexps with more than nine replacement groups ("$10").
 */
public class MultiRegexpMatcher {

    /** Use this list to preserve the ordering in which regexps are added */
    private List<String> regexps = Lists.newArrayList();

    private Map<String, String> map = Maps.newHashMap();
    private int index = 2;

    public MultiRegexpMatcher add(Pattern regexp, String replacement) {
        return add(regexp.pattern(), replacement);
    }

    public MultiRegexpMatcher add(String regexp, String replacement) {
        StringBuilder nr = new StringBuilder();
        System.out.println("Adding " + regexp + ":" + replacement);
        Set<Integer> variables = Sets.newHashSet();
        for (int i = 0; i < replacement.length(); i++) {
            if (replacement.charAt(i) == '$' && i < replacement.length() - 1
                    && Character.isDigit(replacement.charAt(i + 1))) {
                int n = replacement.charAt(i + 1) - '0' - 1;
                System.out.println("Replacing " + (n+1) + " with " + (n+index));
                nr.append("$" + (n + index));
                i++;
                variables.add(n);
            } else {
                nr.append(replacement.charAt(i));
            }
        }
        index += variables.size();
        System.out.println(".. done adding " + regexp + ":" + nr);
        regexps.add(regexp);
        map.put(regexp, nr.toString());

        return this;
    }

    public String replaceString(String s) {
        String re = "(" + Joiner.on("|").join(regexps) + ")";
        Matcher matcher = Pattern.compile(re).matcher(s);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String g = matcher.group(1);
            // This part is slightly inefficient but I can't think of a better
            // way to find out which regexps caused this match, and we need
            // to find it in order to retrieve its replacement.
            for (String thisRe : map.keySet()) {
                if (Pattern.matches(thisRe, g)) {
                    matcher.appendReplacement(result, map.get(thisRe));
                }
            }
        }
        matcher.appendTail(result);
//        System.out.println("Returning " + result.toString());
        return result.toString();
    }

    public static void main(String[] args) {
        new MultiRegexpMatcher().test1();
        new MultiRegexpMatcher().test2();
    }

    private void verify(String message, String expected, String... strings) {
      MultiRegexpMatcher mrm = new MultiRegexpMatcher();
      for (int i = 0; i< strings.length; i += 2) {
          mrm.add(strings[i], strings[i + 1]);
      }
      String result = mrm.replaceString(message);
      if (! expected.equals(result)) {
        throw new RuntimeException("Failure " + message);
      }
    }

    public void test1() {
        verify(
            "fb-1 foo c-4 bar fb-7",
            "FB-1 foo 4:C-4 bar FB-7",
            "fb-([\\d])", "FB-$1", "(c-)([\\d])", "$2:C-$2"
            );
    }

    public void test2() {
      verify(
          "bana cori",
          "banana ba cocorico",
          "(ba)(na)", "$1$2$2 $1",
          "(co)(ri)", "$1$1$2$1");
    }
}
