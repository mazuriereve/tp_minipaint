package drawing;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lewandowski on 20/12/2020.
 * Sujet du patron Observer : prévient ses observateurs quand une forme est ajoutée ou supprimée.
 */
public class DrawingPane extends Pane implements Iterable<Shape> {

    private MouseMoveHandler mouseMoveHandler;

    private ArrayList<Shape> shapes;

    private List<Observer> observers;

    public DrawingPane() {
        clipChildren();
        shapes = new ArrayList<>();
        observers = new ArrayList<>();
        mouseMoveHandler = new MouseMoveHandler(this);
    }


    /**
     * Clips the children of this {@link Region} to its current size.
     * This requires attaching a change listener to the region’s layout bounds,
     * as JavaFX does not currently provide any built-in way to clip children.
     */
    void clipChildren() {
        final Rectangle outputClip = new Rectangle();
        this.setClip(outputClip);

        this.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        this.getChildren().add(shape);
        notifyObservers();
    }

    public void removeShape(Shape shape) {
        if (shapes.remove(shape)) {
            this.getChildren().remove(shape);
            notifyObservers();
        }
    }

    /**
     * Itère sur les formes dessinées. L'itérateur est en lecture seule :
     * pour ajouter ou retirer une forme, il faut passer par addShape / removeShape.
     */
    @Override
    public Iterator<Shape> iterator() {
        return Collections.unmodifiableList(shapes).iterator();
    }

    /**
     * Nombre de formes dessinées.
     */
    public int size() {
        return shapes.size();
    }

    public void clear() {
        this.getChildren().removeAll(shapes);
        shapes.clear();
        notifyObservers();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        // copie de la liste : un observateur peut se désabonner pendant la notification
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update();
        }
    }
}