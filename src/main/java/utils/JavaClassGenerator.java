package utils;

import antlr.cidlParser;
import com.sun.codemodel.*;
import gui.Model;
import gui.components.Component;
import gui.components.Composite;
import gui.components.CompositeComponent;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.sun.codemodel.JExpr.invoke;

public class JavaClassGenerator {
    public static void generate(Model model, File target) {

        JCodeModel codeModel = new JCodeModel();
        JPackage rootPackage = codeModel._package("src");

        GenerationContext context = new GenerationContext();
        context.nameToComponentMap = mapNamesToComponents(model.components);
        context.codeModel = codeModel;

        try {
            generateClassShells(model.components, rootPackage, context);
        } catch (JClassAlreadyExistsException e) {
            System.out.println("Error: Class already exists");
        }

        generateFields(model.components, context);
        generateMethodHunks(model.components, context);
        generateAssemblies(model.components, context);
        generateDelegations(model.components, context);

        try {
            printCodeToConsole(codeModel);
            exportCodeToFile(target, codeModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void generateMethodHunks(List<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            component.getProvidedMethods().forEach(
                    method -> generateMethodHunk(componentClass, method, JMod.PUBLIC, context)
            );
            component.getRequiredMethods().forEach(
                    method -> generateMethodHunk(componentClass, method, JMod.PRIVATE, context)
            );
        }
    }

    private static void generateMethodHunk(JDefinedClass componentClass, cidlParser.Method_headerContext method, int mods, GenerationContext context) {
        List<JType> methodTypes = method.TYPE().stream().map(t -> getTypeFromString(t.getText(), context)).toList();
        List<String> parameterNames = method.NAME().stream().map(ParseTree::getText).toList();
        JType returnType = methodTypes.getFirst();
        assert methodTypes.size() == parameterNames.size() + 1;
        if (method.METHOD_NAME() == null) {
            System.out.println(method);
        }
        JMethod jMethod = componentClass.method(mods, returnType, method.METHOD_NAME().getText());
        for (int i = 0; i < parameterNames.size(); i++) {
            jMethod.param(methodTypes.get(i + 1), parameterNames.get(i));
        }
        jMethod.javadoc().add(method.information_flow_spec().getText());
    }

    private static void generateFields(Collection<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            cidlParser.List_subcomponentsContext subcomponents = component.getComponentContext().list_subcomponents();
            if (subcomponents != null) {
                subcomponents.NAME().forEach(typeName -> {
                    JType type = getTypeFromString(typeName.getText(), context);
                    componentClass.field(JMod.PRIVATE, type, typeName.getText());
                });
            }
        }
    }


    private static void generateAssemblies(List<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            for (cidlParser.AssemblyContext assembly : component.getComponentContext().assembly()) {
                String requiringComponentName = assembly.NAME(0).getText();
                JFieldVar requiringComponentField = componentClass.fields().get(requiringComponentName);
                Optional<JDefinedClass> requiringComponentClass = getFieldClass(context, requiringComponentField);
                String providingComponentName = assembly.NAME(1).getText();
                JFieldVar providingComponentField = componentClass.fields().get(providingComponentName);
                Optional<JDefinedClass> providingComponentClass = getFieldClass(context, providingComponentField);
                if (requiringComponentClass.isPresent() && providingComponentClass.isPresent()) {
                    Optional<JMethod> requiringComponentMethod = requiringComponentClass.get().methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(0).getText())).findAny();
                    Optional<JMethod> providingComponentMethod = providingComponentClass.get().methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(1).getText())).findAny();
                    if (requiringComponentMethod.isPresent() && providingComponentMethod.isPresent()) {
                        JInvocation returnExpression = invoke(providingComponentMethod.get());
                        requiringComponentMethod.get().params().forEach(returnExpression::arg);
                        requiringComponentMethod.get().body()._return(returnExpression);
                    }
                }
            }
        }
    }

    private static Optional<JDefinedClass> getFieldClass(GenerationContext context, JFieldVar requiringComponentField) {
        return context.componentToJClassMap.values().stream().filter(jDefinedClass -> jDefinedClass.fullName().equals(requiringComponentField.type().fullName())).findAny();
    }

    private static void generateDelegations(Collection<Component> components, GenerationContext context) {
        for (Component component : components.stream().filter(c -> (c instanceof CompositeComponent)).toList()) {
            assert component instanceof Composite;
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            ((Composite) component).getCompositeDelegations().forEach(delegationContext -> {
                String providingComponentName = delegationContext.NAME().getText();
                JFieldVar providingComponentField = componentClass.fields().get(providingComponentName);
                Optional<JDefinedClass> providingComponentClass = getFieldClass(context, providingComponentField);
                if (providingComponentClass.isPresent()) {
                    Optional<JMethod> requiringComponentMethod = componentClass.methods().stream().filter(method -> method.name().equals(delegationContext.METHOD_NAME(0).getText())).findAny();
                    Optional<JMethod> providingComponentMethod = providingComponentClass.get().methods().stream().filter(method -> method.name().equals(delegationContext.METHOD_NAME(1).getText())).findAny();
                    if (requiringComponentMethod.isPresent() && providingComponentMethod.isPresent()) {
                        JInvocation returnExpression = invoke(providingComponentMethod.get());
                        requiringComponentMethod.get().params().forEach(returnExpression::arg);
                        requiringComponentMethod.get().body()._return(returnExpression);
                    }
                }
            });
        }
    }

    private static void printCodeToConsole(JCodeModel codeModel) throws IOException {
        final Map<String, ByteArrayOutputStream> streams = new HashMap<>();

        CodeWriter codeWriter = new CodeWriter() {
            @Override
            public OutputStream openBinary(JPackage jPackage, String name) {
                String fullyQualifiedName = jPackage.name().isEmpty() ? name : jPackage.name().replace(".", "/") + "/" + name;

                if (!streams.containsKey(fullyQualifiedName)) {
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
        codeModel.build(codeWriter);
        streams.values().forEach(System.out::println);
    }

    private static void exportCodeToFile(File target, JCodeModel codeModel) throws IOException {
        codeModel.build(target);
    }

    private static void generateClassShells(Collection<Component> components, JPackage rootPackage, GenerationContext context) throws JClassAlreadyExistsException {
        for (Component component : components) {
            if (component.getParentComponent() == null) {
                generateClassShell(component, rootPackage, context);
            }
        }
    }

    private static void generateClassShell(Component component, JPackage rootPackage, GenerationContext context) throws JClassAlreadyExistsException {
        String className = component.getComponentContext().NAME(0).getText();
        if (className == null || className.isEmpty()) {
            className = component.getName();
        }

        JDefinedClass newClass = rootPackage._class(className);
        context.nameToComponentMap.put(className, component);
        context.componentToJClassMap.put(component, newClass);

        if (component instanceof CompositeComponent compositeComponent) {
            JPackage subPackage = rootPackage.subPackage(className);
            for (Component childComponent : compositeComponent.getChildComponents()) {
                generateClassShell(childComponent, subPackage, context);
            }
        }
    }


    private static Map<String, Component> mapNamesToComponents(Collection<Component> components) {
        Map<String, Component> map = new HashMap<>();
        components.forEach(component -> map.put(component.getComponentContext().NAME(0).getText(), component));
        return map;
    }

    private static JType getTypeFromMethod(cidlParser.Method_headerContext methodHeaderContext, GenerationContext context) {
        String typeName = methodHeaderContext.TYPE(0).getText();
        return getTypeFromString(typeName, context);
    }

    private static JType getTypeFromString(String typeName, GenerationContext context) {
        if (context.getJClassFromName(typeName) != null) {
            return context.getJClassFromName(typeName);
        }
        try {
            return context.codeModel.parseType(typeName);
        } catch (ClassNotFoundException e) {
            return context.codeModel.directClass(typeName);
        }
    }

    private static class GenerationContext {
        public Map<String, Component> nameToComponentMap = new HashMap<>();
        public Map<Component, JDefinedClass> componentToJClassMap = new HashMap<>();
        public JCodeModel codeModel;

        public JDefinedClass getJClassFromName(String name) {
            if (nameToComponentMap.containsKey(name)) {
                return componentToJClassMap.get(nameToComponentMap.get(name));
            } else {
                return null;
            }
        }
    }
}
