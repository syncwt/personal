package com.beust.injection;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public class Main {

  public static class Dependency {
    
  }
  public static class Person {
    private final Dependency dependency;
    private final String name;

    public Person(String name, Dependency dependency) {
      this.name = name;
      this.dependency = dependency;
      System.out.println("Person created with name:" + name + " dependency:" + dependency);
    }

    public void f() {
      System.out.println("Dependency: " + dependency);
    }
  }

  public static class PersonProvider implements Provider<Person> {
    @Override
    public Person get() {
      return new Person("default", null);
    }
  }

  public static void main(String[] args) {
    final TypeLiteral<LoadingCache<String, Person>> typeLiteral =
        new TypeLiteral<LoadingCache<String, Person>>() {};
    Module m = new Module() {

      @Override
      public void configure(Binder binder) {
        LoadingCache<String, Person> c = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, Person>() {
                @Inject
                private Dependency dependency;

                @Override
                public Person load(String key) throws Exception {
                    return new Person(key, dependency);
                }
            });

        binder.bind(Key.get(typeLiteral)).toInstance(c);
      }
    };

    Injector inj = Guice.createInjector(m);
    LoadingCache<String, Person> map = inj.getInstance(Key.get(typeLiteral));
    Person p = map.getUnchecked("Cedric");
  }

  public Person get() {
    // TODO Auto-generated method stub
    return null;
  }
}
