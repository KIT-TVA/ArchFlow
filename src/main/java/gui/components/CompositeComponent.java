package gui.components;

import java.util.Collection;

public interface CompositeComponent extends Component {
    Collection<Component> getChildComponents();
    void addChildComponent(Component child);
    void removeChildComponent(Component child);
}
