package gui;

import gui.components.assembly.components.Assembly;
import gui.components.Component;
import lattice.Lattice;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public List<Component> components = new ArrayList<Component>();
    public List<Assembly> assemblies = new ArrayList<Assembly>();
    Lattice lattice;

    public Model(){
    }

    public void add_component(Component c){
        components.add(c);
    }
    public void add_assembly(Assembly a){
        assemblies.add(a);
    }
    public void setLattice(Lattice lattice) { this.lattice = lattice; }
    public Lattice getLattice(){ return lattice; }

}
