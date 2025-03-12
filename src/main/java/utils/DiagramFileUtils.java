package utils;

import antlr.cidlParser;
import gui.Model;
import gui.components.Atomic;
import gui.components.Component;
import gui.components.Composite;
import gui.components.CompositeComponent;
import gui.components.assembly.Assemblable;
import gui.components.assembly.components.*;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DiagramFileUtils {
    private DiagramFileUtils() {
    }

    public static JSONObject generateJson(Collection<Component> components, Collection<Assembly> assemblies) {
        JSONObject json = new JSONObject();
        json.put("components", generateJsonComponentArray(components, null));
        json.put("assemblies", generateJsonAssemblyArray(assemblies));
        return json;
    }

    private static JSONArray generateJsonComponentArray(Collection<Component> components, Component parent) {
        JSONArray json = new JSONArray();
        for (Component component : components) {
            if (component == null || component.getParentComponent() != parent) {
                System.out.println("Component is null. This indicates there is a bug somewhere.");
                continue;
            }
            JSONObject componentJson = new JSONObject();
            componentJson.put("name", component.getName())
                    .put("id", component.hashCode())
                    .put("spec", component.getSpec())
                    .put("type", component instanceof CompositeComponent ? "composite" : "atomic")
                    .put("dimensions", new JSONObject().put("width", component.getContainer().getWidth()).put("height", component.getContainer().getHeight()))
                    .put("position", new JSONObject().put("x", component.getContainer().getLayoutX()).put("y", component.getContainer().getLayoutY()));
            if (component instanceof CompositeComponent) {
                componentJson.put("children", generateJsonComponentArray(((CompositeComponent) component).getChildComponents(), component));
            }
            json.put(componentJson);
        }
        return json;
    }

    private static JSONArray generateJsonAssemblyArray(Collection<Assembly> assemblies) {
        JSONArray json = new JSONArray();
        for (Assembly assembly : assemblies) {
            if (assembly == null) {
                continue;
            }
            JSONObject assemblyJson = new JSONObject();
            if (assembly.getProvidingComponent() != null) {
                assemblyJson.put("providing", assembly.getProvidingComponent().hashCode());
            }
            if (assembly.getRequiringComponent() != null) {
                assemblyJson.put("requiring", assembly.getRequiringComponent().hashCode());
            }
            Assemblable providingNode = getProvidingNode(assembly);
            Assemblable requiringNode = getRequiringNode(assembly);
            assert providingNode != null;
            assert requiringNode != null;
            assemblyJson.put("providingNodeId", providingNode.hashCode());
            assemblyJson.put("requiringNodeId", requiringNode.hashCode());
            if (providingNode instanceof ComponentConnection) {
                assemblyJson.put("providingNodeSide", ((ComponentConnection) providingNode).getSide());
                assemblyJson.put("providingNodePosition", providingNode.getEndPoint());
            } else if (providingNode instanceof Delegation) {
                assemblyJson.put("providingNodeSide", ((Delegation) providingNode).getSide());
                assemblyJson.put("providingNodePosition", providingNode.getStartPoint());
            }
            if (requiringNode instanceof ComponentConnection) {
                assemblyJson.put("requiringNodeSide", ((ComponentConnection) requiringNode).getSide());
                assemblyJson.put("requiringNodePosition", requiringNode.getStartPoint());
            } else if (requiringNode instanceof Delegation) {
                assemblyJson.put("requiringNodeSide", ((Delegation) requiringNode).getSide());
                assemblyJson.put("requiringNodePosition", requiringNode.getStartPoint());
            }
            System.out.printf("Requiring node class: %s%n", requiringNode.getClass());

            JSONArray points = new JSONArray();
            assembly.getChildren().forEach(a -> {
                if (a instanceof Assemblable && !(a instanceof Delegation) && !(a instanceof ComponentConnection)) {
                    points.put(new JSONObject().put("startX", ((Assemblable) a).getStartPoint().getX()).put("startY", ((Assemblable) a).getStartPoint().getY()).put("endX", ((Assemblable) a).getEndPoint().getX()).put("endY", ((Assemblable) a).getEndPoint().getY()));
                }
            });
            assemblyJson.put("points", points);
            json.put(assemblyJson);
        }
        return json;
    }

    //Returns id of component connection or Delegates
    private static Assemblable getProvidingNode(Assembly assembly) {
        assert assembly.getChildren().getFirst() instanceof Assemblable;
        Assemblable first = (Assemblable) assembly.getChildren().getFirst();
        if (first instanceof ComponentConnection || first instanceof Delegation) {
            return first;
        } else {
            if (first.getStart() != null && (first.getStart() instanceof ComponentConnection || first.getStart() instanceof Delegation)) {
                return first.getStart();
            }
        }
        return null;
    }

    private static Assemblable getRequiringNode(Assembly assembly) {
        assert assembly.getChildren().getLast() instanceof Assemblable;
        Assemblable last = (Assemblable) assembly.getChildren().getLast();
        if (last instanceof ComponentConnection || last instanceof Delegation) {
            return last;
        } else {
            if (last.getEnd() != null && last.getEnd() instanceof ComponentConnection || last.getEnd() instanceof Delegation) {
                return last.getEnd();
            }
        }
        System.out.println("Requiring node not found");
        return null;
    }

    private static Map<Integer, Component> getComponents(JSONArray json) {
        Map<Integer, Component> idMap = new HashMap<>();
        json.forEach(a -> {
            assert a instanceof JSONObject;
            int id = ((JSONObject) a).getInt("id");
            Component component;
            Point2D position = getCoordinates(((JSONObject) a).getJSONObject("position"));
            Point2D dimensions = getDimensions(((JSONObject) a).getJSONObject("dimensions"));
            if (((JSONObject) a).getString("type").equals("composite")) {
                Map<Integer, Component> subComponents = getComponents(((JSONObject) a).getJSONArray("children"));
                CompositeComponent compositeComponent = new Composite(position.getX(), position.getY(), (int) dimensions.getX(), (int) dimensions.getY());
                subComponents.values().forEach(compositeComponent::addChildComponent);
                subComponents.values().forEach(c -> c.setParentComponent(compositeComponent));
                component = compositeComponent;
                idMap.putAll(subComponents);
            } else {
                component = new Atomic(position.getX(), position.getY(), (int) dimensions.getX(), (int) dimensions.getY());
            }
            component.setName(((JSONObject) a).getString("name"));
            if (((JSONObject) a).has("spec")) {
                String specText = (((JSONObject) a).getString("spec"));
                InterfaceParser parser = new InterfaceParser();
                cidlParser.ComponentContext componentContext = parser.parse(specText);
                component.setSpec(specText);
                component.setComponentContext(componentContext);
            }
            idMap.put(id, component);
        });
        return idMap;
    }

    private static Point2D getCoordinates(JSONObject position) {
        return new Point2D(position.getDouble("x"), position.getDouble("y"));
    }

    private static Point2D getDimensions(JSONObject dimensions) {
        return new Point2D(dimensions.getDouble("width"), dimensions.getDouble("height"));
    }

    private static Collection<Assembly> getAssemblies(JSONArray json, Map<Integer, Component> componentMap) {
        List<Assembly> assemblies = new ArrayList<>();
        Map<Integer, Assemblable> delegations = new HashMap<>();
        json.forEach(a -> {
            assert a instanceof JSONObject;
            int providingHash = ((JSONObject) a).getInt("providing");
            int requiringHash = ((JSONObject) a).getInt("requiring");
            Component providingComponent = componentMap.get(providingHash);
            Component requiringComponent = componentMap.get(requiringHash);
            int providingNodeId = ((JSONObject) a).getInt("providingNodeId");
            int requiringNodeId = ((JSONObject) a).getInt("requiringNodeId");
            Assemblable providingAssemblable;
            Assemblable requiringAssemblable;
            Point2D firstPoint = new Point2D(((JSONObject) a).getJSONArray("points").getJSONObject(0).getDouble("startX"), ((JSONObject) a).getJSONArray("points").getJSONObject(0).getDouble("startY"));
            int lengthOfPointArray = ((JSONObject) a).getJSONArray("points").length();
            Point2D lastPoint = new Point2D(((JSONObject) a).getJSONArray("points").getJSONObject(lengthOfPointArray - 1).getDouble("endX"), ((JSONObject) a).getJSONArray("points").getJSONObject(lengthOfPointArray - 1).getDouble("endY"));
            if (delegations.containsKey(providingNodeId)) {
                providingAssemblable = delegations.get(providingNodeId);
            } else {
                if (providingComponent instanceof CompositeComponent) {
                    providingAssemblable = new Delegation(providingComponent, firstPoint, ((JSONObject) a).getEnum(Side.class, "providingNodeSide"));
                } else {
                    providingAssemblable = new ComponentConnection(providingComponent, ((JSONObject) a).getEnum(Side.class, "providingNodeSide"), firstPoint, InterfaceType.PROVIDES);
                }
            }
            if (delegations.containsKey(requiringNodeId)) {
                requiringAssemblable = delegations.get(requiringNodeId);
            } else {
                if (requiringComponent instanceof CompositeComponent) {
                    requiringAssemblable = new Delegation(requiringComponent, lastPoint, ((JSONObject) a).getEnum(Side.class, "requiringNodeSide"));
                } else {
                    requiringAssemblable = new ComponentConnection(requiringComponent, ((JSONObject) a).getEnum(Side.class, "requiringNodeSide"), lastPoint, InterfaceType.REQUIRES);
                }
            }
            delegations.put(providingNodeId, providingAssemblable);
            delegations.put(requiringNodeId, requiringAssemblable);
            Assembly assembly = new Assembly();
            assembly.setProvidingComponent(providingComponent);
            assembly.setRequiringComponent(requiringComponent);
            List<Assemblable> assemblables = new ArrayList<>();
            assemblables.add(providingAssemblable);
            ((JSONObject) a).getJSONArray("points").forEach(point -> {
                Assemblable next = new AssemblyLine(
                        assemblables.getLast(),
                        new Point2D(((JSONObject) point).getDouble("endX"), ((JSONObject) point).getDouble("endY"))
                );
                assemblables.getLast().setEnd(next, next.getStartPoint());
                assemblables.add(next);
            });
            assemblables.getLast().setEnd(requiringAssemblable, requiringAssemblable.getStartPoint());
            requiringAssemblable.setStart(assemblables.getLast(), assemblables.getLast().getEndPoint());
            assemblables.add(requiringAssemblable);
            assemblables.forEach(assemblable -> {
                assembly.getChildren().add((Node) assemblable);
            });
            assemblies.add(assembly);
        });
        return assemblies;
    }

    public static Model generateModel(JSONObject json) {
        Map<Integer, Component> components = getComponents(json.getJSONArray("components"));
        Collection<Assembly> assemblies = getAssemblies(json.getJSONArray("assemblies"), components);
        Model model = new Model();
        model.components = components.values().stream().toList();
        model.assemblies = assemblies.stream().toList();
        return model;
    }

}
