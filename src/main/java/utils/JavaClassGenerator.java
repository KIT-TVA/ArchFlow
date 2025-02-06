package utils;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import gui.Model;
import gui.components.Component;
import gui.components.CompositeComponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class JavaClassGenerator {
    public static void generate(Model model) {

        JCodeModel codeModel = new JCodeModel();

        model.components.forEach(component -> {
            if (component.getParentComponent() == null) {

                if (component instanceof CompositeComponent compositeComponent) {
                    generatePackageFromCompositeComponent(codeModel, compositeComponent);
                } else {
                    generateClassFromAtomic(codeModel, component);
                }
            }
        });

        final Map<String, ByteArrayOutputStream> streams = new HashMap<>();

        CodeWriter codeWriter = new CodeWriter() {
            @Override
            public OutputStream openBinary(JPackage jPackage, String name) {
                String fullyQualifiedName = jPackage.name().isEmpty() ? name : jPackage.name().replace(".", "/") + "/" + name;

                if(!streams.containsKey(fullyQualifiedName)) {
                    streams.put(fullyQualifiedName, new ByteArrayOutputStream());
                }
                System.out.println(fullyQualifiedName);
                return streams.get(fullyQualifiedName);
            }

            @Override
            public void close() throws IOException {
                for (OutputStream outputStream : streams.values()) {
                    outputStream.flush();
                    outputStream.close();
                }
            }
        };

        try {
            codeModel.build(codeWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        streams.values().forEach(System.out::println);
    }

    private static void generatePackageFromCompositeComponent(JCodeModel codeModel, CompositeComponent component) {
        JPackage compositePackage = codeModel._package(component.getName());
        try {
            compositePackage._class(component.getName());
        } catch (JClassAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        component.getChildComponents().forEach(childComponent -> {
            if (childComponent instanceof CompositeComponent) {
                generatePackageFromCompositeComponent(codeModel, (CompositeComponent) childComponent);
            } else {
                generateClassFromAtomic(codeModel, childComponent);
            }
        });
    }

    private static void generateClassFromAtomic(JCodeModel codeModel, Component component) {
        try {
            codeModel._class(component.getName());
        } catch (JClassAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }
}
