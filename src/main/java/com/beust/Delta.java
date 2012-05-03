package com.beust;

import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.Sets;

public class Delta {

    public static void main(String[] args) {
        String o = "whitelisted_facebook_uid: \"209835\"\nwhitelisted_facebook_uid: \"5\"\nwhitelisted_facebook_uid: \"514999\"\nwhitelisted_facebook_uid: \"1105621\"";
        String n = "            whitelisted_facebook_uid: \"209835\"\nwhitelisted_facebook_uid: \"6\"\nwhitelisted_facebook_uid: \"514999\"\nwhitelisted_facebook_uid: \"1105621\"\nwhitelisted_facebook_uid: \"3500177\"";
        System.out.println(new Delta().computeDelta(o, n));
    }

    private String computeDelta(String oldConfig, String newConfig) {
        StringBuilder result = new StringBuilder();
        Set<String> set1 = configToSet(newConfig);
        set1.removeAll(configToSet(oldConfig));
        result.append("Added lines:" + set1 + "\n");

        Set<String> set2 = configToSet(oldConfig);
        set2.removeAll(configToSet(newConfig));
        result.append("Removed lines:" + set2 + "\n");

        return result.toString();
    }
    
    private Set<String> configToSet(String config) {
        Set<String> result = Sets.newHashSet();
        Scanner s = new Scanner(config).useDelimiter("\n");
        while (s.hasNext()) {
            result.add(s.next().trim());
        }
        return result;
    }
}
