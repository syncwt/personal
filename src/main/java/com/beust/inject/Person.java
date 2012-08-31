package com.beust.inject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Person {
  private String name;
  private String address;
  private GeoService geoService;

  @Inject
  public Person(@Assisted("name") String name, @Assisted("address") String address,
      GeoService geoService, PersonFactory pf) {
    this.name = name;
    this.address = address;
    this.geoService = geoService;
  }

  public boolean livesNearby(Person person) {
    return geoService.livesNear(address, person.getAddress(), 10);
  }

  private String getAddress() {
    return address;
  }

  public static void main(String[] args) {
    Module module1 = new FactoryModuleBuilder()
        .build(PersonFactory.class);
    Module module2 = new Module() {
      @Override
      public void configure(Binder binder) {
        binder.bind(GeoService.class).to(GeoServiceImpl.class).in(Scopes.SINGLETON);
      }
    };

    Injector injector = Guice.createInjector(module1, module2);
    PersonFactory pf = injector.getInstance(PersonFactory.class);
    Person p1 = pf.create("Alfred", "1 a st");
    Person p2 = pf.create("Bob", "2 b st");
    p1.livesNearby(p2);
  }
}

class Address {
}

interface PersonFactory {
  Person create(@Assisted("name") String name, @Assisted("address") String address);
}

interface GeoService {
  boolean livesNear(String address1, String address2, int miles);
}

class GeoServiceImpl implements GeoService {
  @Override
  public boolean livesNear(String address1, String address2, int miles) {
    System.out.println("GeoService.livesNear(" + address1 + ", " + address2 + ")");
    return true;
  }
  
}

