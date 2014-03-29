package com.beust;

import java.util.Set;

import com.google.common.collect.Sets;

public class School {
  private final String name;
  private final String nickname;

  public School(String name, String nickname) {
    this.name = name;
    this.nickname = nickname;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    School other = (School) obj;

    return other.nickname.equals(nickname) || other.name.equals(name);
  }

  public static void main(String[] args) {
    School school1 = new School("University of Pennsylvania", "upenn");
    School school2 = new School("University of Pennsylvania", "Penn State");
    System.out.println("Equals: " + school1.equals(school2));
    Set<School> schools = Sets.newHashSet();
    schools.add(school1);
    schools.add(school2);
    System.out.println("Set: " + schools);
  }
  
}
