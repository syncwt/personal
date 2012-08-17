package com.beust;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@Singleton
class Data {
    public String getData() {
        return "injected data " + hashCode();
    }
}

interface IPerson {
    public String getName();
}

class BasePerson implements IPerson {
    private String name;
    private Data data;

    @Inject
    public BasePerson(@Assisted String name, Data data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public String getName() {
        return name + " data:" + data.getData();
    }
}

class Adult extends BasePerson {
    @Inject
    public Adult(@Assisted String name, Data data) {
        super(name + " (adult)", data);
    }
}

class Child extends BasePerson {
    @Inject
    public Child(@Assisted String name, Data data) {
        super(name + " (child)", data);
    }
}

interface IPersonFactory {
    public @Named("adult") IPerson createAdult(String name);
    public @Named("child") IPerson createChild(String name);
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
        System.out.println(factory.createAdult("Cedric").getName());
        System.out.println(factory.createAdult("Alois").getName());
        System.out.println(factory.createChild("Christopher").getName());
    }

    public static void main(String[] args) {
        final Module module = new FactoryModuleBuilder()
            .implement(IPerson.class, Names.named("adult"), Adult.class)
            .implement(IPerson.class, Names.named("child"), Child.class)
            .build(IPersonFactory.class);

        // Or install in an existing module:
        Module module2 = new Module() {
            @Override
            public void configure(Binder binder) {
                binder.install(module);
            }
            
        };
        Injector injector = Guice.createInjector(module);

        injector.getInstance(AssistedTest.class).run();
    }

}