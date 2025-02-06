package gui.controllers;

import antlr.cidlParser.ComponentContext;
import composition.CheckComposition;
import gui.Model;
import gui.components.Atomic;
import gui.components.Component;
import gui.components.Composite;
import gui.components.SelectedComponentOverlay;
import gui.components.assembly.components.Assembly;
import gui.components.assembly.components.ComponentConnection;
import gui.components.assembly.components.Delegates;
import gui.components.assembly.util.AssemblyBuilder;
import gui.components.assembly.util.AssemblyEditor;
import gui.components.util.ComponentMover;
import gui.components.util.ComponentTraverser;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.InterfaceParser;
import utils.TextFileReader;

import gui.views.ComponentCanvas;
import gui.views.MainLayout;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import utils.InterfaceParser;
import utils.TextFileReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ViewController {
    private final Model model;
    private final ComponentCanvas canvas;
    private final MainLayout mainLayout;
    private final ComponentMover componentMover;
    private SelectedComponentOverlay selectedComponentOverlay;
    private AssemblyBuilder assemblyBuilder;
    private AssemblyEditor assemblyEditor;
    private Point2D pointAtCreationOfAssemblyEditor;
    private double canvasDragX, canvasDragY;
    InterfaceParser parser;
    CheckComposition checker;
    Component currentComponent;

    public ViewController(Model model, MainLayout mainLayout, ComponentCanvas canvas) {
        this.model = model;
        this.canvas = canvas;
        this.mainLayout = mainLayout;

        //Instantiate parser and composition checker
        componentMover = new ComponentMover(canvas, model.components);
        parser = new InterfaceParser();
        checker = new CheckComposition(model);

        mainLayout.setOnDiagramLoaded(newModel -> {
            model.components = newModel.components;
            model.assemblies = newModel.assemblies;
            model.components.forEach(c -> {
                if (c.getParentComponent() == null) {
                    canvas.draw(c);
                }
                handleComponentClick(c);
                handleComponentDrag(c);
            });
            model.assemblies.forEach(canvas::draw);
        });

        //Set Events on non-changing components
        setHandleAddAtomicComponent();
        setHandleAddCompositeComponent();
        setHandleLoadInterfaceFromFile();
        setHandleSaveInterfaceToFile();
        // setHandleCheckCompositions();
        setHandleCheckComposite();
        setHandleCanvasInteraction();
    }

    // private void setHandleCheckCompositions() {
    //    mainLayout.checkAssembly.setOnAction(e -> {
    //        System.out.println("Check Clicked");
    //        List<Assembly> invalidAssemblies = checker.checkAssembly();
    //        System.out.println("Invalid Assemblies Found:");
    //        System.out.println(invalidAssemblies);
    //        canvas.updateAssemblyColors(invalidAssemblies);

    //     });
    //   }

    private void setHandleCheckComposite(){
        mainLayout.checkComposite.setOnAction(e -> {
            System.out.println("Composite Check Clicked");
            System.out.println(currentComponent);
            if(checker.checkComposite((Composite)currentComponent)){
                System.out.println("All OK");
                currentComponent.setColor(Color.GREEN);

            }
            else{
                System.out.println("Composition not OK");
                currentComponent.setColor(Color.RED);
            }
            //List<Assembly> invalidAssemblies = checker.checkAssembly();
            //System.out.println("Invalid Assemblies Found:");
            //System.out.println(invalidAssemblies);
            //canvas.updateAssemblyColors(invalidAssemblies);

        });

    }

    private void setHandleSaveInterfaceToFile() {
        mainLayout.saveSpec.setOnAction(e -> {
            System.out.println("Saving file");
            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(null);
            if (selectedFile == null) {
                // TODO Handle no file selected
                System.out.println("No file selected");
            } else {
                String loadedFileName = selectedFile.getName();

                try (FileWriter writer = new FileWriter(loadedFileName)) {
                    String spec = mainLayout.getSpec().toString();
                    writer.write(spec);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setHandleLoadInterfaceFromFile() {
        mainLayout.loadSpec.setOnAction(e -> {
            System.out.println("Loading file");
            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(null);
            if (selectedFile == null) {
                // TODO Handle no file selected
                System.out.println("No file selected");
            } else {
                TextFileReader reader = new TextFileReader();
                List<String> lines = reader.read(selectedFile);

                mainLayout.clearInterfaceTextField();
                mainLayout.clearErrorsTextField();
                mainLayout.setInterfaceTextField(lines);

                //Also set the interface spec of the current selected component FIX: done in a bad way for now.
                String specText = mainLayout.getSpec().getText();
                System.out.println("handling load");
                System.out.println(specText);
                currentComponent.setSpec(specText);
                //FIX: Need to instantiate new parser to not display old errors...
                parser = new InterfaceParser();
                ComponentContext componentContext = parser.parse(specText);
                System.out.println(componentContext);
                currentComponent.setComponentContext(componentContext);
            }
        });
    }

    private void handleComponentClick(Component component) {
        Pane rect = component.getContainer();
        rect.setOnMouseClicked(e -> {
            // Stop the click cascading
            e.consume();

            setCurrentComponent(component);
            initializeEditingFields();

            if (e.getClickCount() > 1 && assemblyBuilder == null) {
                editComponentName(component);
            }

            if (assemblyBuilder != null) {
                handleAssemblyBuildingClick(component, e);
            } else {
                setOverlay(component);
            }
        });
    }

    private void handleAssemblyBuildingClick(Component component, MouseEvent e) {
        Point2D point = ComponentTraverser.getSwitchedCoordinates(component, null).add(new Point2D(e.getX(), e.getY()));
        assemblyBuilder.clickComponent(component, point);
    }

    private void initializeEditingFields() {
        //Clear spec field
        mainLayout.clearInterfaceTextField();
        //Clear error view
        mainLayout.clearErrorsTextField();
        //Set the spec for the current component
        mainLayout.setInterfaceTextField(currentComponent.getSpec());
    }

    private static void editComponentName(Component component) {
        TextField textField = new TextField();
        component.getContainer().getChildren().add(textField);
        //Store the name and remove text field when enter is pressed
        textField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                String inputText = textField.getText();
                System.out.println(inputText);
                //Remove text field
                component.getContainer().getChildren().remove(textField);
                //Set new name
                component.setName(inputText);
            }
        });
    }

    private void setOverlay(Component component) {
        selectedComponentOverlay = SelectedComponentOverlay.fromComponent(component);
        if (assemblyEditor != null) {
            canvas.remove(assemblyEditor);
            assemblyEditor = null;
        }
        selectedComponentOverlay.setOnArrowClicked(side -> {
            assemblyBuilder = new AssemblyBuilder(component, side);
            initializeAssemblyBuilder();
        });
        selectedComponentOverlay.setOnEdgePressed((event, s) -> {
            event.consume();
            componentMover.mouseDownComponentResized(event, s);
        });
        selectedComponentOverlay.setOnEdgeDragged((event, side) -> {
            event.consume();
            componentMover.mouseDraggedComponent(event);
            selectedComponentOverlay.resize();
        });
        selectedComponentOverlay.setOnEdgeReleased((event, side) -> {
            event.consume();
            componentMover.mouseReleasedComponent(event);
        });
        canvas.draw(selectedComponentOverlay);
    }

    private void initializeAssemblyBuilder() {
        assemblyBuilder.setOnEnd(() -> {
            if (!deleteAssemblyIfIncomplete()) {
                Assembly assembly = assemblyBuilder.getAssembly();
                assembly.getChildren().forEach(a -> {
                    if (a instanceof Delegates) {
                        ((Delegates) a).getComponent().getAssemblies().add((Delegates) a);
                        a.setMouseTransparent(false);
                        a.setOnMouseClicked(e -> {
                            Delegates clickedDelegates = (Delegates) a;
                            if (clickedDelegates.getEnd() == null || clickedDelegates.getStart() == null) {
                                assemblyBuilder = new AssemblyBuilder(clickedDelegates);
                                initializeAssemblyBuilder();
                            }
                        });
                    } else if (a instanceof ComponentConnection) {
                        ((ComponentConnection) a).getComponent().getAssemblies().add((ComponentConnection) a);
                    }
                    a.setOnMouseEntered(e -> {
                        if (assemblyEditor == null) {
                            assemblyEditor = new AssemblyEditor(assembly);
                            canvas.draw(assemblyEditor);
                            pointAtCreationOfAssemblyEditor = new Point2D(e.getSceneX(), e.getSceneY());
                        }
                    });
                });
            }
            assemblyBuilder = null;
        });
        canvas.draw(assemblyBuilder.getAssembly());
        model.assemblies.add(assemblyBuilder.getAssembly());
    }

    private boolean deleteAssemblyIfIncomplete() {
        if (!assemblyBuilder.getAssembly().isComplete()) {
            canvas.remove(assemblyBuilder.getAssembly());
            model.assemblies.remove(assemblyBuilder.getAssembly());
            return true;
        }
        assemblyBuilder.getAssembly().setViewOrder(0);
        return false;
    }

    private void setCurrentComponent(Component component) {
        currentComponent = component;
    }


    private void saveAndParseInterfaceSpec() {
        TextArea spec = mainLayout.getSpec();
        spec.setOnKeyTyped(e -> {
            String specText = spec.getText();
            //Set current text to the spec of component
            currentComponent.setSpec(specText);
            //Parse the spec
            ComponentContext componentContext = parser.parse(specText);
            //Set current component context
            currentComponent.setComponentContext(componentContext);


            //Get the parse errors
            List<String> parserErrors = parser.getErrors();
            //Clear the error view FIX does not seem to clear properly?
            mainLayout.clearErrorsTextField();
            //Set new parse errors
            mainLayout.setErrorsTextField(parserErrors);
        });
    }

    private void handleComponentDrag(Component component) {
        Pane rect = component.getContainer();
        rect.setOnMousePressed(e -> {
            e.consume();
            if (assemblyBuilder == null) {
                if (component != currentComponent) {
                    removeOverlay();
                }
                componentMover.setComponent(component);
                componentMover.mouseDownComponent(e);
            }
            if (assemblyEditor != null) {
                canvas.remove(assemblyEditor);
                assemblyEditor = null;
            }
        });
        rect.setOnMouseMoved(e -> {
            e.consume();
            if (assemblyBuilder != null) {
                Point2D point = ComponentTraverser.getSwitchedCoordinates(component, null).add(new Point2D(e.getX(), e.getY()));
                assemblyBuilder.mouseoverComponent(component, point);
            } else {
                componentMover.mouseMovedComponent(component, e);
            }
        });
        rect.setOnMouseDragged(e -> {
            e.consume();
            componentMover.mouseDraggedComponent(e);
            if (selectedComponentOverlay != null) {
                selectedComponentOverlay.reposition();
            }
        });
        rect.setOnMouseReleased(e -> {
            if (assemblyBuilder == null) {
                componentMover.mouseReleasedComponent(e);
            }
        });
    }

    private void removeOverlay() {
        if (selectedComponentOverlay == null) {
            return;
        }
        canvas.remove(selectedComponentOverlay);
        selectedComponentOverlay = null;
    }

    private void setHandleAddAtomicComponent() {
        mainLayout.componentList.atomic.setOnAction(e -> {
            Atomic a = new Atomic(20, 20, 50, 50);
            //Set component specific handlers
            handleComponentDrag(a);
            handleComponentClick(a);
            saveAndParseInterfaceSpec();
            model.add_component(a);
            canvas.draw(a);

        });
    }

    private void setHandleAddCompositeComponent() {
        mainLayout.componentList.composite.setOnAction(e -> {
            Composite c = new Composite(20, 20, 250, 50);
            //Set component specific handlers
            handleComponentDrag(c);
            handleComponentClick(c);
            saveAndParseInterfaceSpec();
            model.add_component(c);
            canvas.draw(c);
        });
    }

    private void setHandleCanvasInteraction() {
        canvas.getPane().setOnMouseMoved(e -> {
            if (assemblyBuilder != null) {
                assemblyBuilder.mouseoverComponent(null, new Point2D(e.getX(), e.getY()));
            }
            if (assemblyEditor != null && pointAtCreationOfAssemblyEditor != null) {
                if (pointAtCreationOfAssemblyEditor.distance(new Point2D(e.getSceneX(), e.getSceneY())) > 5) {
                    canvas.remove(assemblyEditor);
                    assemblyEditor = null;
                }
            }
        });

        canvas.getPane().setOnMousePressed(e -> {
            canvasDragX = e.getX();
            canvasDragY = e.getY();
        });
        /*canvas.getPane().setOnMouseDragged(e -> {
            canvas.getPane().setTranslateX(e.getX() - canvasDragX + canvas.getPane().getTranslateX());
            canvas.getPane().setTranslateY(e.getY() - canvasDragY + canvas.getPane().getTranslateY());
        });
        canvas.getPane().setOnScroll(e -> {
            canvas.getPane().setScaleX(canvas.getPane().getScaleX() + e.getDeltaY() * 0.001);
            canvas.getPane().setScaleY(canvas.getPane().getScaleX() + e.getDeltaY() * 0.001);
        });*/
        canvas.getPane().setOnMousePressed(e -> {
            if (assemblyBuilder != null) {
                handleAssemblyBuildingClick(null, e);
            }


        });
        canvas.getPane().setOnMouseClicked(e -> {
            if (assemblyBuilder == null) {
                canvas.remove(selectedComponentOverlay);
                selectedComponentOverlay = null;
            }
            if (assemblyEditor != null) {
                if (pointAtCreationOfAssemblyEditor != null && pointAtCreationOfAssemblyEditor.distance(new Point2D(e.getSceneX(), e.getSceneY())) < 5) {
                    pointAtCreationOfAssemblyEditor = null;
                }
            }
        });
        canvas.getPane().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                if (assemblyBuilder != null) {
                    model.assemblies.remove(assemblyBuilder.getAssembly());
                    canvas.remove(assemblyBuilder.getAssembly());
                    assemblyBuilder = null;
                }
                if (selectedComponentOverlay != null) {
                    canvas.remove(selectedComponentOverlay);
                    selectedComponentOverlay = null;
                }
                if (assemblyEditor != null) {
                    canvas.remove(assemblyEditor);
                    assemblyEditor = null;
                }
            }
            if (e.getCode() == KeyCode.DELETE) {
                if (assemblyBuilder == null && selectedComponentOverlay != null) {
                    Component componentToDelete = selectedComponentOverlay.getComponent();
                    deleteComponent(componentToDelete);
                }
                if (assemblyEditor != null) {
                    canvas.remove(assemblyEditor.getAssembly());
                    model.assemblies.remove(assemblyEditor.getAssembly());
                    canvas.remove(assemblyEditor);
                    assemblyEditor = null;
                }
            }
        });
    }

    private void deleteComponent(Component componentToDelete) {
        model.components.remove(componentToDelete);
        if (componentToDelete.getParentComponent() == null) {
            canvas.remove(componentToDelete.getContainer());
        } else {
            componentToDelete.getParentComponent().removeChildComponent(componentToDelete);
        }
        Collection<Assembly> assembliesToRemove = model.assemblies.stream().filter(assembly -> (assembly.getProvidingComponent() == componentToDelete || assembly.getRequiringComponent() == componentToDelete)).toList();
        assembliesToRemove.forEach(a -> {
            model.assemblies.remove(a);
            canvas.remove(a);
        });
        if (componentToDelete instanceof Composite) {
            ((Composite) componentToDelete).getChildComponents().forEach(this::deleteComponent);
        }
        canvas.remove(selectedComponentOverlay);
        selectedComponentOverlay = null;
    }
}
