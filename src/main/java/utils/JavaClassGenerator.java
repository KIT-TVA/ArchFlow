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
            generateInterfaceShells(model.components, rootPackage, context);
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

    private static void generateInterfaceShells(List<Component> components, JPackage rootPackage, GenerationContext context) throws JClassAlreadyExistsException {
        for (Component component : components) {
            if (component.getParentComponent() == null) {
                generateInterfaceShell(component, rootPackage, context);
            }
        }
    }

    private static void generateInterfaceShell(Component component, JPackage rootPackage, GenerationContext context) throws JClassAlreadyExistsException {
        String packageName = component.getComponentContext().NAME(0).getText();
        String interfaceName = generateInterfaceName(packageName);
        if (packageName == null || packageName.isEmpty()) {
            packageName = component.getName();
            generateInterfaceName(packageName);
        }

        JDefinedClass newInterface = rootPackage._interface(interfaceName);
        context.nameToComponentMap.put(interfaceName, component);
        context.componentToInterfaceMap.put(component, newInterface);

        if (component instanceof CompositeComponent compositeComponent) {
            JPackage subPackage = rootPackage.subPackage(packageName);
            for (Component childComponent : compositeComponent.getChildComponents()) {
                generateInterfaceShell(childComponent, subPackage, context);
            }
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
            JDefinedClass componentInterfaceClass = context.componentToInterfaceMap.get(component);
            component.getProvidedMethods().forEach(
                    method -> generateMethodHunk(componentInterfaceClass, method, JMod.PUBLIC, context)
            );
        }
    }

    private static void generateMethodHunk(JDefinedClass componentClass, cidlParser.Method_headerContext method, int mods, GenerationContext context) {
        List<JType> methodTypes = method.TYPE().stream().map(t -> getTypeFromString(t.getText(), context)).toList();
        List<String> parameterNames = method.NAME().stream().map(ParseTree::getText).toList();
        JType returnType = methodTypes.getFirst();
        assert methodTypes.size() == parameterNames.size() + 1;
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
                    JType type = getInterfaceTypeFromString(typeName.getText(), context);
                    componentClass.field(JMod.PRIVATE, type, toFirstLetterLowerCase(typeName.getText()));
                });
            }
        }
    }


    private static void generateAssemblies(List<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            for (cidlParser.AssemblyContext assembly : component.getComponentContext().assembly()) {
                String requiringComponentName = generateInterfaceName(assembly.NAME(0).getText());
                Optional<JFieldVar> requiringComponentField = getComponentFieldFromName(componentClass, requiringComponentName);
                String providingComponentName = generateInterfaceName(assembly.NAME(1).getText());
                Optional<JFieldVar> providingComponentField = getComponentFieldFromName(componentClass, providingComponentName);
                Optional<JDefinedClass> requiringComponentClass = Optional.empty();
                Optional<JDefinedClass> providingComponentClass = Optional.empty();
                if (requiringComponentField.isPresent() && providingComponentField.isPresent()) {
                    requiringComponentClass = getFieldClass(context, requiringComponentField.get());
                    providingComponentClass = getFieldInterface(context, providingComponentField.get());
                }
                if (requiringComponentClass.isPresent() && providingComponentClass.isPresent()) {
                    initializeRequiredField(componentClass, requiringComponentClass.get(), providingComponentField.get(), context);
                    Optional<JMethod> requiringComponentMethod = requiringComponentClass.get().methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(0).getText())).findAny();
                    Optional<JMethod> providingComponentMethod = providingComponentClass.get().methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(1).getText())).findAny();
                    if (requiringComponentMethod.isPresent() && providingComponentMethod.isPresent()) {
                        JFieldRef providingComponentReference = JExpr._this().ref(referenceNameFromClass(providingComponentClass.get()));
                        JInvocation returnExpression = providingComponentReference.invoke(providingComponentMethod.get());
                        requiringComponentMethod.get().params().forEach(returnExpression::arg);
                        requiringComponentMethod.get().body()._return(returnExpression);
                    }
                }
            }
        }
    }

    private static Optional<JFieldVar> getComponentFieldFromName(JDefinedClass componentClass, String requiringComponentName) {
        return componentClass.fields().values().stream().filter(var -> var.type().name().equals(requiringComponentName)).findFirst();
    }

    private static String generateClassName(String name) {
        String className = name.substring(0, 1).toUpperCase() + name.substring(1);
        className = className.replace(" ", "");
        return className;
    }

    private static String generateInterfaceName(String name) {
        return "I" + generateClassName(name);
    }

    private static String concatCamelCase(String first, String second) {
        return first + second.substring(0, 1).toUpperCase() + second.substring(1);
    }

    private static String referenceNameFromClass(JClass jClass) {
        return jClass.name().substring(0, 1).toLowerCase() + jClass.name().substring(1);
    }

    private static void initializeRequiredField(JDefinedClass parentComponentClass, JDefinedClass requiringComponentClass, JFieldVar requiredField, GenerationContext context) {
        if (requiringComponentClass.fields().values().stream().noneMatch(field -> field.type().equals(requiredField.type()))) {
            String requiredFieldName = toFirstLetterLowerCase(requiredField.name());
            String setterName = concatCamelCase("set", requiredFieldName);
            requiringComponentClass.field(JMod.PRIVATE, requiredField.type(), requiredFieldName);
            JMethod setter = requiringComponentClass.method(JMod.PUBLIC, context.codeModel.VOID, setterName);
            setter.param(requiredField.type(), requiredFieldName);
            setter.body().assign(JExpr._this().ref(requiredFieldName), JExpr.ref(requiredFieldName));
            JFieldVar referenceInParent = parentComponentClass.fields().get(requiredFieldName);
            parentComponentClass.getConstructor(new JType[]{}).body().invoke(JExpr._this().ref(requiringComponentClass.name()), setterName).arg(referenceInParent);
        }
    }

    private static String toFirstLetterLowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private static Optional<JDefinedClass> getFieldClass(GenerationContext context, JFieldVar requiringComponentField) {
        Optional<JDefinedClass> result = context.componentToJClassMap.values().stream().filter(jDefinedClass -> jDefinedClass.fullName().equals(requiringComponentField.type().fullName())).findAny();
        if (result.isEmpty()) {
            result = context.componentToJClassMap.values().stream().filter(jClass -> {
                for (Iterator<JClass> it = jClass._implements(); it.hasNext(); ) {
                    JClass jInterface = it.next();
                    if (jInterface.fullName().equals(requiringComponentField.type().fullName())) {
                        return true;
                    }
                }
                return false;
            }).findAny();
        }
        return result;
    }

    private static Optional<JDefinedClass> getFieldInterface(GenerationContext context, JFieldVar requiringComponentField) {
        return context.componentToInterfaceMap.values().stream().filter(jDefinedClass -> jDefinedClass.fullName().equals(requiringComponentField.type().fullName())).findAny();
    }

    private static void generateDelegations(Collection<Component> components, GenerationContext context) {
        for (Component component : components.stream().filter(c -> (c instanceof CompositeComponent)).toList()) {
            assert component instanceof Composite;
            JDefinedClass componentClass = context.componentToJClassMap.get(component);
            ((Composite) component).getCompositeDelegations().forEach(delegationContext -> {
                String providingComponentName = generateInterfaceName(delegationContext.NAME().getText());
                Optional<JFieldVar> providingComponentField = getComponentFieldFromName(componentClass, providingComponentName);
                Optional<JDefinedClass> providingComponentClass = Optional.empty();
                if (providingComponentField.isPresent()) {
                    providingComponentClass = getFieldClass(context, providingComponentField.get());
                }
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
        className = generateClassName(className);
        JDefinedClass newClass = rootPackage._class(className);
        newClass._implements(context.getInterfaceFromName(generateInterfaceName(className)));
        newClass.constructor(JMod.PUBLIC);
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

    private static JType getInterfaceTypeFromString(String typeName, GenerationContext context) {
        JType classType = getTypeFromString(typeName, context);
        if (classType instanceof JClass) {
            if(((JClass) classType)._implements().hasNext()) {
                return ((JClass) classType)._implements().next();
            }
        }
        return classType;
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
        public Map<Component, JDefinedClass> componentToInterfaceMap = new HashMap<>();
        public JCodeModel codeModel;

        public JDefinedClass getJClassFromName(String name) {
            if (nameToComponentMap.containsKey(name)) {
                return componentToJClassMap.get(nameToComponentMap.get(name));
            } else {
                return null;
            }
        }

        public JDefinedClass getInterfaceFromName(String name) {
            if (nameToComponentMap.containsKey(name)) {
                return componentToInterfaceMap.get(nameToComponentMap.get(name));
            } else {
                return null;
            }
        }
    }
}
