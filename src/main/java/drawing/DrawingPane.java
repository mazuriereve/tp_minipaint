package drawing;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by lewandowski on 20/12/2020.
 */
public class DrawingPane extends Pane implements Iterable<Shape> {

    private MouseMoveHandler mouseMoveHandler;

    private ArrayList<Shape> shapes;

    public DrawingPane() {
        clipChildren();
        shapes = new ArrayList<>();
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
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
        this.getChildren().remove(shape);
    }

    /**
     * Itère sur les formes dessinées. L'itérateur est en lecture seule :
     * pour ajouter ou retirer une forme, il faut passer par addShape / removeShape.
     */
    @Override
    public Iterator<Shape> iterator() {
        return Collections.unmodifiableList(shapes).iterator();
    }

    public void clear() {
        this.getChildren().removeAll(shapes);
        shapes.clear();
    }
}