package utils;

import antlr.cidlParser;
import com.sun.codemodel.*;
import gui.Model;
import gui.components.Component;
import gui.components.Composite;
import gui.components.CompositeComponent;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

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
        context.terminalNodeToComponentMap = mapTerminalNodesToComponents(model.components);
        context.codeModel = codeModel;

        try {
            generateInterfaceShells(model.components, rootPackage, context);
            generateClassShells(model.components, rootPackage, context);
        } catch (JClassAlreadyExistsException e) {
            System.out.println("Error: Class already exists");
        }

        generateSubcomponentFields(model.components, context);
        generateMethodHunks(model.components, context);
        generateAssemblies(model.components, context);
        generateDelegations(model.components, context);

        try {
            //For debugging
            //printCodeToConsole(codeModel);
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
        context.setComponentInterface(component, newInterface);

        if (component instanceof CompositeComponent compositeComponent) {
            JPackage subPackage = rootPackage.subPackage(packageName);
            for (Component childComponent : compositeComponent.getChildComponents()) {
                generateInterfaceShell(childComponent, subPackage, context);
            }
        }
    }

    private static void generateMethodHunks(List<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.getComponentClass(component);
            component.getProvidedMethods().forEach(
                    method -> generateMethodHunk(componentClass, method, JMod.PUBLIC, context)
            );
            component.getRequiredMethods().forEach(
                    method -> generateMethodHunk(componentClass, method, JMod.PRIVATE, context)
            );
            JDefinedClass componentInterfaceClass = context.getComponentInterface(component);
            component.getProvidedMethods().forEach(
                    method -> generateMethodHunk(componentInterfaceClass, method, JMod.PUBLIC, context)
            );
        }
    }

    private static void generateMethodHunk(JDefinedClass componentClass, cidlParser.Method_headerContext method, int mods, GenerationContext context) {
        List<JType> methodTypes = method.TYPE().stream().map(t -> getTypeFromTerminalNode(t, context)).toList();
        List<String> parameterNames = method.NAME().stream().map(ParseTree::getText).toList();
        JType returnType = methodTypes.getFirst();
        assert methodTypes.size() == parameterNames.size() + 1;
        JMethod jMethod = componentClass.method(mods, returnType, method.METHOD_NAME().getText());
        for (int i = 0; i < parameterNames.size(); i++) {
            jMethod.param(methodTypes.get(i + 1), parameterNames.get(i));
        }
        jMethod.javadoc().add(method.information_flow_spec().getText());
    }

    private static void generateSubcomponentFields(Collection<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.getComponentClass(component);
            cidlParser.List_subcomponentsContext subcomponents = component.getComponentContext().list_subcomponents();
            if (subcomponents != null) {
                subcomponents.NAME().forEach(typeName -> {
                    if (context.terminalNodeToComponentMap.containsKey(typeName.getText())) {
                        ComponentArtefacts subComponentArtefacts = context.getComponentArtefactsFromTerminalNode(typeName);
                        JFieldVar newField = componentClass.field(JMod.PRIVATE, subComponentArtefacts.componentInterface, subComponentArtefacts.defaultFieldName);
                        componentClass.getConstructor(new JType[]{}).body().decl(subComponentArtefacts.componentClass, subComponentArtefacts.defaultFieldName, JExpr._new(subComponentArtefacts.componentClass));
                        componentClass.getConstructor(new JType[]{}).body().assign(JExpr._this().ref(newField.name()), newField);
                    } else {
                        JType type = getInterfaceTypeFromTerminalNode(typeName, context);
                        componentClass.field(JMod.PRIVATE, type, toFirstLetterLowerCase(typeName.getText()));
                    }
                });
            }
        }
    }


    private static void generateAssemblies(List<Component> components, GenerationContext context) {
        for (Component component : components) {
            JDefinedClass componentClass = context.getComponentClass(component);
            for (cidlParser.AssemblyContext assembly : component.getComponentContext().assembly()) {
                ComponentArtefacts requiringComponentArtefacts = context.getComponentArtefactsFromTerminalNode(assembly.NAME(0));
                ComponentArtefacts providingComponentArtefacts = context.getComponentArtefactsFromTerminalNode(assembly.NAME(1));
                JFieldVar requiringComponentField = componentClass.fields().get(requiringComponentArtefacts.defaultFieldName);
                JFieldVar providingComponentField = componentClass.fields().get(providingComponentArtefacts.defaultFieldName);
                assert requiringComponentField != null && providingComponentField != null;
                initializeRequiredField(context.getComponentArtefacts(component), requiringComponentArtefacts, providingComponentArtefacts, context);
                Optional<JMethod> requiringComponentMethod = requiringComponentArtefacts.componentClass.methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(0).getText())).findAny();
                Optional<JMethod> providingComponentMethod = providingComponentArtefacts.componentInterface.methods().stream().filter(method -> method.name().equals(assembly.METHOD_NAME(1).getText())).findAny();
                if (requiringComponentMethod.isPresent() && providingComponentMethod.isPresent()) {
                    JFieldRef providingComponentReference = JExpr._this().ref(providingComponentArtefacts.defaultFieldName);
                    JInvocation returnExpression = providingComponentReference.invoke(providingComponentMethod.get());
                    requiringComponentMethod.get().params().forEach(returnExpression::arg);
                    requiringComponentMethod.get().body()._return(returnExpression);
                }
            }
        }
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

    private static void initializeRequiredField(ComponentArtefacts parentComponent, ComponentArtefacts requiringComponent, ComponentArtefacts requiredComponent, GenerationContext context) {
        if (!requiringComponent.componentClass.fields().containsKey(requiredComponent.defaultFieldName)) {
            String setterName = concatCamelCase("set", requiredComponent.defaultFieldName);
            requiringComponent.componentClass.field(JMod.PRIVATE, requiredComponent.componentInterface, requiredComponent.defaultFieldName);
            JMethod setter = requiringComponent.componentClass.method(JMod.PUBLIC, context.codeModel.VOID, setterName);
            setter.param(requiredComponent.componentInterface, requiredComponent.defaultFieldName);
            setter.body().assign(JExpr._this().ref(requiredComponent.defaultFieldName), JExpr.ref(requiredComponent.defaultFieldName));
            JFieldVar referenceInParent = parentComponent.componentClass.fields().get(requiredComponent.defaultFieldName);
            parentComponent.componentClass.getConstructor(new JType[]{}).body().invoke(JExpr.ref(requiringComponent.defaultFieldName), setterName).arg(referenceInParent);
        }
    }

    private static String toFirstLetterLowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private static void generateDelegations(Collection<Component> components, GenerationContext context) {
        for (Component component : components.stream().filter(c -> (c instanceof CompositeComponent)).toList()) {
            assert component instanceof Composite;
            ComponentArtefacts delegatingComponentArtefacts = context.getComponentArtefacts(component);
            ((Composite) component).getCompositeDelegations().forEach(delegationContext -> {
                ComponentArtefacts providingComponentArtefacts = context.getComponentArtefactsFromTerminalNode(delegationContext.NAME());
                JFieldVar providingComponentField = delegatingComponentArtefacts.componentClass.fields().get(providingComponentArtefacts.defaultFieldName);
                assert providingComponentField != null;
                assert providingComponentArtefacts.componentInterface != null;
                Optional<JMethod> delegatingComponentMethod = delegatingComponentArtefacts.componentClass.methods().stream().filter(method -> method.name().equals(delegationContext.METHOD_NAME(0).getText())).findAny();
                Optional<JMethod> providingComponentMethod = providingComponentArtefacts.componentInterface.methods().stream().filter(method -> method.name().equals(delegationContext.METHOD_NAME(1).getText())).findAny();
                if (delegatingComponentMethod.isPresent() && providingComponentMethod.isPresent()) {
                    JInvocation returnExpression = invoke(JExpr._this().ref(providingComponentField), providingComponentMethod.get());
                    delegatingComponentMethod.get().params().forEach(returnExpression::arg);
                    delegatingComponentMethod.get().body()._return(returnExpression);
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
        ComponentArtefacts componentArtefacts = context.getComponentArtefacts(component);
        JDefinedClass newClass = rootPackage._class(className);
        newClass._implements(componentArtefacts.componentInterface);
        newClass.constructor(JMod.PUBLIC);
        componentArtefacts.componentClass = newClass;
        componentArtefacts.defaultFieldName = toFirstLetterLowerCase(className);

        if (component instanceof CompositeComponent compositeComponent) {
            JPackage subPackage = rootPackage.subPackage(toFirstLetterLowerCase(className));
            for (Component childComponent : compositeComponent.getChildComponents()) {
                generateClassShell(childComponent, subPackage, context);
            }
        }
    }


    private static Map<String, Component> mapTerminalNodesToComponents(Collection<Component> components) {
        Map<String, Component> map = new HashMap<>();
        components.forEach(component -> map.put(component.getComponentContext().NAME(0).getText(), component));
        return map;
    }

    private static JType getInterfaceTypeFromTerminalNode(TerminalNode typeName, GenerationContext context) {
        JType classType = getTypeFromTerminalNode(typeName, context);
        if (classType instanceof JClass) {
            if(((JClass) classType)._implements().hasNext()) {
                return ((JClass) classType)._implements().next();
            }
        }
        return classType;
    }

    private static JType getTypeFromTerminalNode(TerminalNode typeName, GenerationContext context) {
        if (context.terminalNodeToComponentMap.containsKey(typeName.getText())) {
            return context.getComponentArtefactsFromTerminalNode(typeName).componentInterface;
        }
        try {
            return context.codeModel.parseType(typeName.getText());
        } catch (ClassNotFoundException e) {
            return context.codeModel.directClass(typeName.getText());
        }
    }

    private static class GenerationContext {
        public Map<String, Component> terminalNodeToComponentMap = new HashMap<>();
        public Map<Component, ComponentArtefacts> componentToArtefactsMap = new HashMap<>();
        public JCodeModel codeModel;

        public void setComponentClass(Component component, JDefinedClass jClass) {
            initializeComponentIfUnset(component);
            componentToArtefactsMap.get(component).componentClass = jClass;
        }

        public void setComponentInterface(Component component, JDefinedClass jClass) {
            initializeComponentIfUnset(component);
            componentToArtefactsMap.get(component).componentInterface = jClass;
        }

        public void setComponentDefaultFieldName(Component component, String fieldName) {
            initializeComponentIfUnset(component);
            componentToArtefactsMap.get(component).defaultFieldName = fieldName;
        }


        public JDefinedClass getComponentClass(Component component) {
            assert componentToArtefactsMap.containsKey(component);
            return componentToArtefactsMap.get(component).componentClass;
        }

        public JDefinedClass getComponentInterface(Component component) {
            assert componentToArtefactsMap.containsKey(component);
            return componentToArtefactsMap.get(component).componentInterface;
        }

        public String getComponentDefaultFieldName(Component component) {
            assert componentToArtefactsMap.containsKey(component);
            return componentToArtefactsMap.get(component).defaultFieldName;
        }

        public ComponentArtefacts getComponentArtefactsFromTerminalNode(TerminalNode node) {
            assert node != null;
            assert terminalNodeToComponentMap.containsKey(node.getText()) && componentToArtefactsMap.containsKey(terminalNodeToComponentMap.get(node.getText()));
            return componentToArtefactsMap.get(terminalNodeToComponentMap.get(node.getText()));
        }

        public ComponentArtefacts getComponentArtefacts(Component component) {
            assert component != null;
            assert componentToArtefactsMap.containsKey(component);
            return componentToArtefactsMap.get(component);
        }

        private void initializeComponentIfUnset(Component component) {
            assert component != null;
            if (!componentToArtefactsMap.containsKey(component)) {
                componentToArtefactsMap.put(component, new ComponentArtefacts());
            }
        }

    }
    private static class ComponentArtefacts {
        JDefinedClass componentClass;
        JDefinedClass componentInterface;
        String defaultFieldName;
    }
}
