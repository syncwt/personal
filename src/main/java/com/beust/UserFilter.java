package com.beust;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

public class UserFilter {

    public static void main(String[] args) throws IOException {
        Set<String> userIds = readIds("/tmp/b");
        Set<String> preregisteredUserIds = readIds("/tmp/a");
        System.out.println("Before: " + userIds.size() + " " + preregisteredUserIds.size());
        userIds.removeAll(preregisteredUserIds);
        System.out.println("After: " + userIds.size()); 
    }

    private static Set<String> readIds(String string) throws IOException {
        Set<String> result = Sets.newHashSet();
        BufferedReader br = new BufferedReader(new FileReader(new File(string)));
        String line = br.readLine();
        while (line != null) {
            result.add(line);
            line = br.readLine();
        }
        return result;
    }
}
