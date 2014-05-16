package com.beust;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class ThreadDumpTool {

    public static void main(String[] args) throws IOException {
        new ThreadDumpTool().run(args);
    }

    class Group implements Comparable<Group> {
        private final String name;
        private final List<Thread> threads;

        public Group(String name, List<Thread> threads) {
            this.name = name;
            this.threads = threads;
        }

        @Override
        public int compareTo(Group o) {
            return Integer.compare(o.threads.size(), threads.size());
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(getClass())
                    .add("name", name)
                    .add("thread#", threads.size())
                    .toString();
        }
    }

    private static Pattern P = Pattern.compile("(.+) id=.*state=([^ ]+).*");

    class Thread {
        String header;
        List<String> stackTrace = Lists.newArrayList();
        String name;
        String state;
        String line;
        private String group;

        public Thread(String header, BufferedReader br) throws IOException {
            this.header = header;
            parseHeader(header);
            line = br.readLine();
            while (line != null && ! line.contains("state=")) {
                stackTrace.add(line);
                line = br.readLine();
            }
        }

        public boolean stackTraceContains(Pattern... patterns) {
            for (String frame : stackTrace) {
                for (Pattern p : patterns) {
                    if (p.matcher(frame).matches()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void parseHeader(String line) {
            Matcher matcher = /* Pattern.compile("(.+) id=.*state=([^ ]+).*") */P.matcher(line);
//            System.out.println("Matcher: " + matcher.matches() + " " + matcher.group(1)
//                    + " " + matcher.group(2));

            if (matcher.matches()) {
                if (matcher.groupCount() == 2) {
                    this.name = matcher.group(1);
                    this.state = matcher.group(2);
                } else {
                    throw new IllegalArgumentException("Couldn't parse header " + header);
                }
            }
        }

        String getLine() {
            return line;
        }

        public String getState() {
            return state;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(getClass())
                    .add("name", name)
                    .add("state", state)
                    .add("stack size", stackTrace.size())
                    .toString();
        }

        public String getName() {
            return name;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getGroup() {
            return group;
        }

        public List<String> getStackTrace() {
            return stackTrace;
        }

        public String getDetails() {
            StringBuilder result = new StringBuilder(
                    "Thread: " + getName() + " State: " + getState() + "\n");
            for (int i = stackTrace.size() - 1; i > 0; i--) {
                result.append("  ").append(stackTrace.get(i)).append("\n");
            }
            return result.toString();
        }
    }

    private static void error(String s) {
        System.err.println(s);
    }

    private List<Thread> parseThreads(BufferedReader br) {
        List<Thread> result = Lists.newArrayList();
        try {
            String line = br.readLine();
            int i = 0;
            while (line != null) {
                if (i++ % 10000 == 0) {
                    System.out.println("Processed " + i + " lines");
                }
                if (line.contains("state=")) {
                    result.add(new Thread(line, br));
                }
                line = br.readLine();
            }
        } catch(IOException ex) {
            error("Couldn't parse the thread dump: " + ex.getMessage());
        }

        return result;
    }

    private static final Pattern ACCEPT = Pattern.compile("^.*accept\\(.*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern FINALIZER = Pattern.compile("^.*finalizer.*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern WAIT = Pattern.compile("^.*wait.*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern SLEEP = Pattern.compile("^.*sleep.*$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern READ = Pattern.compile("^.*Socket.*read.*$",
            Pattern.CASE_INSENSITIVE);

    private void analyzeStackTraces(List<Thread> threads) {
        Multimap<Integer, Thread> sizeMap = ArrayListMultimap.create();
        Multimap<Integer, Thread> hashCodeMap = ArrayListMultimap.create();
        for (Thread thread : threads) {
            List<String> stackTrace = thread.getStackTrace();
            sizeMap.put(stackTrace.size(), thread);
            hashCodeMap.put(stackTrace.hashCode(), thread);
            System.out.println("Size: " + stackTrace.size()  + " hash:  "
                    + stackTrace.hashCode());
        }

        for (Thread thread : threads) {
            List<String> stackTrace = thread.getStackTrace();
            int sizeCount = sizeMap.get(stackTrace.size()).size();
            int hashCount = hashCodeMap.get(stackTrace.hashCode()).size();
            if ((sizeCount < 10 || hashCount < 5)
                    && ! thread.stackTraceContains(ACCEPT, WAIT, FINALIZER, SLEEP, READ)) {
                System.out.println("Interesting thread: " + thread.getDetails());
                System.out.println("");
            }
        }
    }

    private void run(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(
                new File("/Users/cbeust/Downloads/thread-dump.txt")));
        List<Thread> threads = parseThreads(br);
        Multimap<String, Thread> stateMap = ArrayListMultimap.create();
        Set<String> names = Sets.newHashSet();
        for (Thread thread : threads) {
            stateMap.put(thread.getState(), thread);
            names.add(thread.getName());
        }

        Set<String> groupNames = extractGroupNames(names);

        Multimap<String, Thread> groupMap = ArrayListMultimap.create();
        for (Thread t : threads) {
            for (String group : groupNames) {
                if (t.getName().startsWith(group)) {
                    t.setGroup(group);
                    groupMap.put(group, t);
                }
            }
        }

        List<Group> groups = Lists.newArrayList();
        for (String groupName : groupMap.keySet()) {
            groups.add(new Group(groupName, Lists.newArrayList(groupMap.get(groupName))));
        }
        Collections.sort(groups);

//        analyzeStackTraces(threads);
        System.out.println("Total: " + threads.size() + " threads:\n");
        System.out.println("Groups:\n" + Joiner.on("\n").join(groups));
        System.out.println("Runnables:");
        for (Group group : groups) {
            List<Thread> runnableThreads = Lists.newArrayList();
            for (Thread t : group.threads) {
                if ("RUNNABLE".equals(t.getState())) {
                    runnableThreads.add(t);
                }
            }
            if (runnableThreads.size() > 0) {
                System.out.println("=== " + group.name);
                System.out.println("  " + Joiner.on("\n  ").join(runnableThreads));
            }
        }
    }

    private Set<String> extractGroupNames(Set<String> names) {
        Set<String> result = Sets.newHashSet();
        for (String name : names) {
            int i = name.length() - 1;
            while (i >= 0 && Character.isDigit(name.charAt(i))) {
                i--;
            }
            
            result.add(name.substring(0, i));
        }
        return result;
    }

    private String extractGroupName(String string) {
        int index = string.length() - 1;
        char c = string.charAt(index);
        while (c >= 0 && Character.isDigit(c)) {
            index--;
            c = string.charAt(index);
        }
        String result = string.substring(0, index + 1);
        return result;
    }
}
