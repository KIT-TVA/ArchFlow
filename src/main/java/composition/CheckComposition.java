package composition;

import java.util.ArrayList;
import java.util.List;


import antlr.cidlParser.Method_headerContext;
import antlr.cidlParser.AssemblyContext;
import antlr.cidlParser.DelegationContext;
import gui.Model;
import gui.components.Atomic;
import gui.components.Component;
import gui.components.Composite;
import gui.components.assembly.components.Assembly;
import lattice.Lattice;
import lattice.Node;

public class CheckComposition {
    Model model;
    Lattice lattice;

    public CheckComposition(Model model){
        this.model = model;
        lattice  = model.getLattice();
    }

    private Method_headerContext getMethodContextByComponentAndMethodName(
            String componentName,
            String methodName,
            boolean provided){
        for (int i = 0; i < model.components.size(); i++){
            Component c = model.components.get(i);
            if(c.getName().equals(componentName)){
                if(provided){
                    for (int j = 0; j < c.getProvidedMethods().size(); j++){
                        Method_headerContext methodHeader = c.getProvidedMethods().get(j);
                        if(methodHeader.METHOD_NAME().getText().equals(methodName)){
                            return methodHeader;
                        }
                    }
                }
                else{
                    for (int j = 0; j < c.getRequiredMethods().size(); j++){
                        Method_headerContext methodHeader = c.getRequiredMethods().get(j);
                        if(methodHeader.METHOD_NAME().getText().equals(methodName)){
                            return methodHeader;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean checkComposite(Composite composite){
        System.out.println("Called composite check");
        System.out.println(composite.getSpec());
        List<AssemblyContext> compositeAssemblies = composite.getCompositeAssemblies();
        List<DelegationContext> compositeDelegations = composite.getCompositeDelegations();
        System.out.println(composite.getComponentContext());

        for (AssemblyContext assemblyContext : compositeAssemblies) {
            String componentRequiredName = assemblyContext.NAME(0).getText();
            String methodRequiredName = assemblyContext.METHOD_NAME(0).getText();

            Method_headerContext requiredMethodHeader = getMethodContextByComponentAndMethodName(componentRequiredName, methodRequiredName, false);

            String componentProvidedName = assemblyContext.NAME(1).getText();
            String methodProvidedName = assemblyContext.METHOD_NAME(1).getText();
            Method_headerContext providedMethodHeader = getMethodContextByComponentAndMethodName(componentProvidedName, methodProvidedName, true);

            if (requiredMethodHeader == null || providedMethodHeader == null) {
                System.out.println("Could not find component or method in assembly!");
            }

            if (!checkMethods(requiredMethodHeader, providedMethodHeader)){
                return false;
            }

            System.out.println("required component name: " + componentRequiredName);
            System.out.println("required method name: " + methodRequiredName);
            System.out.println("provided component name: " + componentProvidedName);
            System.out.println("provided method name: " + methodProvidedName);
        }

        for (DelegationContext delegationContext : compositeDelegations) {
            String outerName = composite.getName();
            String outerMethodName = delegationContext.METHOD_NAME(0).getText();

            Method_headerContext outerMethodHeader = getMethodContextByComponentAndMethodName(outerName, outerMethodName, true);

            String innerName = delegationContext.NAME().getText();
            String innerMethodName = delegationContext.METHOD_NAME(1).getText();
            Method_headerContext innerMethodHeader = getMethodContextByComponentAndMethodName(innerName, innerMethodName, true);

            if (outerMethodHeader == null || innerMethodHeader == null) {
                System.out.println("Could not find component or method in assembly!");
            }

            if (!checkMethods(outerMethodHeader, innerMethodHeader)){
                return false;
            }
            System.out.println("outer component name: " + outerName);
            System.out.println("outer method name: " + outerMethodName);
            System.out.println("inner component name: " + innerName);
            System.out.println("inner method name: " + innerMethodName);
        }
        return true;

    }

    private boolean check(Component providing, Component requiring){
        if( providing instanceof Composite) {
            if(!checkComposite((Composite) providing)){
                return false;
            }
            if (requiring instanceof Composite) {
                if(!checkComposite((Composite) requiring)){
                    return false;
                }
            }
        }
        else{
            // TODO Assembly of Atomics
            return true;
        }
        return true;
    }

    //Method that checks all assemblies and return all that are invalid
    public List<Assembly> checkAssembly(){
        System.out.println(model.assemblies);
        List<Assembly> invalidAssemblies = new ArrayList<Assembly>();

        for (int i = 0; i < model.assemblies.size(); i++){
            System.out.println("index : " + i );
            Assembly assembly = model.assemblies.get(i);
            //boolean assemblyValid = true;
            Component providing = assembly.getProvidingComponent();
            Component requiring = assembly.getRequiringComponent();

            System.out.println("providing: " + providing.getName());
            System.out.println("requiring: " + requiring.getName());

            boolean valid = check(providing, requiring);
            System.out.println("validity: " + valid);
            if(!valid){
                System.out.println("Check failed");
                invalidAssemblies.add(assembly);
            }
        }
        return invalidAssemblies;
    }

    public boolean checkMethodSignaturesCompatible(Method_headerContext required, Method_headerContext provided){
        System.out.println("Check method signature names:");
        System.out.println(required.METHOD_NAME().toString()); 
        System.out.println(provided.METHOD_NAME().toString()); 
        //TODO: Check that method signatures are valid by types and not names
        if(!required.METHOD_NAME().toString().equals(provided.METHOD_NAME().toString())){
            System.out.println("Method names not equal");
            return false;
        }
        System.out.println("Method names are equal");
        return true;
    }

    public boolean checkMethods(Method_headerContext required, Method_headerContext provided){ 
        //Check first for valid method signature
        if(!checkMethodSignaturesCompatible(required, provided)){
            System.out.println("methods are incompatible");
            return false;
        }

        //Check precondition
        for(int i = 0; i < required.information_flow_spec().pre().SECURITY_LEVEL().size(); i++){
            String secLevelReq = required.information_flow_spec().pre().SECURITY_LEVEL().get(i).getText();
            String secLevelProv = provided.information_flow_spec().pre().SECURITY_LEVEL().get(i).getText();
            System.out.println("Required pre sec level: " + secLevelReq);
            System.out.println("Provided pre sec level: " + secLevelProv);
            Node req = lattice.getNodePerName(secLevelReq);
            Node prov = lattice.getNodePerName(secLevelProv);
            if(req == null || prov == null){
                System.out.println(secLevelProv + " is not a valid security level according to the lattice");
                System.out.println(secLevelReq + " is not a valid security level according to the lattice");
                return  false;
            }
            if(!lattice.leq(req, prov)){
                return false;
            }

            System.out.println("Required pre sec level: " + secLevelReq);
            System.out.println("Provided pre sec level: " + secLevelProv);
        }

        //Check postcondition
        for(int i = 0; i < required.information_flow_spec().post().SECURITY_LEVEL().size(); i++){
                String secLevelReq = required.information_flow_spec().post().SECURITY_LEVEL().get(i).getText();
                String secLevelProv = provided.information_flow_spec().post().SECURITY_LEVEL().get(i).getText();
                Node req = lattice.getNodePerName(secLevelReq);
                Node prov = lattice.getNodePerName(secLevelProv);
                if(req == null || prov == null){
                    System.out.println(secLevelProv + " is not a valid security level according to the lattice");
                    System.out.println(secLevelReq + " is not a valid security level according to the lattice");
                    return  false;
                }
                if(!lattice.leq(prov, req)){
                    return false;
                }
                System.out.println("Required post sec level: " + secLevelReq);
                System.out.println("Provided post sec level: " + secLevelProv);
        }
        System.out.println(required.information_flow_spec().pre().NAME());
        System.out.println(required.information_flow_spec().pre().SECURITY_LEVEL());
        System.out.println(required.information_flow_spec().post().NAME());
        System.out.println(required.information_flow_spec().post().SECURITY_LEVEL());
        System.out.println(provided.information_flow_spec().pre().NAME());
        System.out.println(provided.information_flow_spec().pre().SECURITY_LEVEL());
        System.out.println(provided.information_flow_spec().post().NAME());
        System.out.println(provided.information_flow_spec().post().SECURITY_LEVEL());
        return true;
    }
}
