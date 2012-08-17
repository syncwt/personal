package com.beust;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;

@Singleton
class Data {
    public String getData() {
        return "injected data " + hashCode();
    }
}

interface IPerson {
    public String getName();
}

class Person implements IPerson {
    private String name;
    private Data data;

    @Inject
    public Person(@Assisted String name, Data data) {
        this.name = name;
        this.data = data;
    }
    @Override
    public String getName() {
        return name + " data:" + data.getData();
    }
}

interface IPersonFactory {
    public IPerson create(String name);
}

/**
 * Build with
 *    <groupId>com.google.inject.extensions</groupId>
 *    <artifactId>guice-assistedinject</artifactId>
 *    <version>3.0</version>
 */
public class AssistedTest {
    @Inject
    private IPersonFactory factory;

    private void run() {
        System.out.println(factory.create("Cedric").getName());
        System.out.println(factory.create("Alois").getName());
    }

    public static void main(String[] args) {
        Module module= new FactoryModuleBuilder()
            .implement(IPerson.class, Person.class).build(IPersonFactory.class);
        Injector injector = Guice.createInjector(module);

        injector.getInstance(AssistedTest.class).run();
    }

}