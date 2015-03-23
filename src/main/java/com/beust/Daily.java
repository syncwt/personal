package com.beust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Daily {

	private static final int max = 2;
	private static final Map<Integer, Integer> distributions = new HashMap<>();

	private static void addNumber(int n) {
	    Integer count = distributions.get(n);
	    if (count == null) {
	        distributions.put(n, 1);
	    } else {
	        distributions.put(n, count + 1);
	    }
	}

	public static void main(String[] args) {
	    float total = 0;
		float sampleSize = 100;
		for (int i = 0; i < sampleSize; i++) {
			total += days();
		}
		System.out.println("Average: " + (total / sampleSize));
		System.out.println("Distributions:");
		float t = 0;
		for (Integer n : distributions.keySet()) {
		    t += distributions.get(n);
		}
		for (Integer n : distributions.keySet()) {
		    System.out.println("  " + n + ": " + (distributions.get(n) / t));
		}
	}

	private static int days() {
		Set<Integer> collected = new HashSet<>();
		int days = 0;
		Random r = new Random();
		while (collected.size() != max) {
			int c = r.nextInt(max);
			addNumber(c);
//			System.out.println("   Daily: " + c);
			if (! collected.contains(c)) {
				collected.add(c);
			}
			days++;
		}

		System.out.println("Collected " + collected.size() + " in " + days + " days");
		return days;
	}
}
