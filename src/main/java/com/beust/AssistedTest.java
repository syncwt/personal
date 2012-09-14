package com.beust;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInjectBinding;
import com.google.inject.assistedinject.AssistedInjectTargetVisitor;
import com.google.inject.assistedinject.AssistedMethod;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.spi.DefaultBindingTargetVisitor;

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
    public Adult(@Assisted("name") String name, @Assisted("nick") String nickName, Data data) {
        super(name + " nick:" + nickName + " (adult)", data);
    }
}

class Child extends BasePerson {
    @Inject
    public Child(@Assisted("name") String name, @Assisted("nick") String nickName, Data data) {
        super(name + " nick:" + nickName + " (child)", data);
    }
}

interface IPersonFactory {
    public @Named("adult") IPerson createAdult(@Assisted("name") String name, @Assisted("nick") String nick);
    public @Named("child") IPerson createChild(@Assisted("name") String name, @Assisted("nick") String nick);
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
        System.out.println(factory.createAdult("Cedric", "foo").getName());
        System.out.println(factory.createAdult("Alois", "bar").getName());
        System.out.println(factory.createChild("Christopher", "baz").getName());
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
        displayBindings(injector);
    }

    static class Visitor extends DefaultBindingTargetVisitor<Object, Void> implements
            AssistedInjectTargetVisitor<Object, Void> {

        @Override
        public Void visit(AssistedInjectBinding<?> binding) {
            // Loop over each method in the factory...
            for (AssistedMethod method : binding.getAssistedMethods()) {
                System.out.println("Non-assisted Dependencies: "
                        + method.getDependencies() + ", Factory Method: "
                        + method.getFactoryMethod()
                        + ", Implementation Constructor: "
                        + method.getImplementationConstructor()
                        + ", Implementation Type: "
                        + method.getImplementationType());
            }
            return null;
        }
    }

    private static void displayBindings(Injector injector) {
        Binding<IPersonFactory> binding = injector.getBinding(IPersonFactory.class);
        binding.acceptTargetVisitor(new Visitor());

   }
}