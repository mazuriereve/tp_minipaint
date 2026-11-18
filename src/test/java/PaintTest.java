import drawing.PaintApplication;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import drawing.DrawingPane;
import drawing.Observer;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class PaintTest extends ApplicationTest {

    PaintApplication app;

    @Override
    public void start(Stage stage) {
        try {
            app = new PaintApplication();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_draw_circle_programmatically() {
        interact(() -> {
            app.getDrawingPane().addShape(new Ellipse(20, 20, 30, 30));
        });
        Iterator it = app.getDrawingPane().iterator();
        assertTrue(it.next() instanceof Ellipse);
        assertFalse(it.hasNext());
    }

    @Test
    public void should_draw_circle() {
        // given:
        clickOn("Circle");
        moveBy(60,60);

        // when:
        drag().dropBy(30,30);
        //press(MouseButton.PRIMARY); moveBy(30,30); release(MouseButton.PRIMARY);

        // then:
        Iterator it = app.getDrawingPane().iterator();
        assertTrue(it.next() instanceof Ellipse);
        assertFalse(it.hasNext());
    }

    @Test
    public void should_draw_rectangle() {
        // given:
        clickOn("Rectangle");
        moveBy(0,60);

        // when:
        drag().dropBy(70,40);

        // then:
        Iterator it = app.getDrawingPane().iterator();
        assertTrue(it.next() instanceof Rectangle);
        assertFalse(it.hasNext());
    }

    @Test
    public void should_clear() {
        // given:
        clickOn("Rectangle");
        moveBy(30,60).drag().dropBy(70,40);
        clickOn("Circle");
        moveBy(-30,160).drag().dropBy(70,40);

        // when:
        clickOn("Clear");

        // then:
        assertFalse(app.getDrawingPane().iterator().hasNext());
    }

    @Test
    public void should_draw_triangle_programmatically() {
        interact(() -> {
            app.getDrawingPane().addShape(new Polygon(10, 0, 0, 20, 20, 20));
        });
        Iterator it = app.getDrawingPane().iterator();
        assertTrue(it.next() instanceof Polygon);
        assertFalse(it.hasNext());
    }

    @Test
    public void should_draw_triangle() {
        // given:
        clickOn("Triangle");
        moveBy(0, 60);

        // when:
        drag().dropBy(70, 40);

        // then:
        Iterator it = app.getDrawingPane().iterator();
        Object shape = it.next();
        assertTrue(shape instanceof Polygon);
        assertFalse(it.hasNext());

        Polygon triangle = (Polygon) shape;
        assertTrue(triangle.getStyleClass().contains("triangle"));
        assertIsoscelesTriangle(triangle, 70, 40);
    }

    @Test
    public void should_draw_triangle_when_dragging_towards_top_left() {
        // given: l'origine est à droite et en dessous de la destination
        clickOn("Triangle");
        moveBy(100, 150);

        // when:
        drag().dropBy(-70, -40);

        // then: le résultat est le même que dans l'autre sens
        Iterator it = app.getDrawingPane().iterator();
        Object shape = it.next();
        assertTrue(shape instanceof Polygon);
        assertFalse(it.hasNext());
        assertIsoscelesTriangle((Polygon) shape, 70, 40);
    }

    @Test
    public void should_clear_triangles() {
        // given:
        clickOn("Triangle");
        moveBy(30, 60).drag().dropBy(70, 40);

        // when:
        clickOn("Clear");

        // then:
        assertFalse(app.getDrawingPane().iterator().hasNext());
    }

    @Test
    public void should_not_modify_shapes_through_iterator() {
        interact(() -> {
            app.getDrawingPane().addShape(new Ellipse(20, 20, 30, 30));
        });
        Iterator<Shape> it = app.getDrawingPane().iterator();
        it.next();
        try {
            it.remove();
            fail("L'itérateur ne devrait pas permettre de supprimer des formes");
        } catch (UnsupportedOperationException expected) {
            // ok : la collection interne est protégée
        }
    }

    /**
     * Vérifie qu'un polygone est un triangle isocèle de la largeur et de la hauteur données :
     * sommet au milieu en haut, base en bas.
     */
    private void assertIsoscelesTriangle(Polygon triangle, double width, double height) {
        List<Double> p = triangle.getPoints();
        assertEquals(6, p.size());
        double delta = 1.0;
        // sommet au-dessus de la base, centré
        assertTrue(p.get(1) < p.get(3));
        assertEquals((p.get(2) + p.get(4)) / 2, p.get(0), delta);
        // base horizontale
        assertEquals(p.get(3), p.get(5), delta);
        // dimensions
        assertEquals(width, p.get(4) - p.get(2), delta);
        assertEquals(height, p.get(3) - p.get(1), delta);
    }

    @Test
    public void status_bar_should_show_zero_at_start() {
        assertEquals("Nombre de formes : 0", app.getStatutBar().getText());
    }

    @Test
    public void status_bar_should_count_drawn_shapes() {
        // when:
        clickOn("Rectangle");
        moveBy(30, 60).drag().dropBy(70, 40);

        // then:
        assertEquals("Nombre de formes : 1", app.getStatutBar().getText());

        // when:
        clickOn("Circle");
        moveBy(-30, 160).drag().dropBy(70, 40);

        // then:
        assertEquals("Nombre de formes : 2", app.getStatutBar().getText());
    }

    @Test
    public void status_bar_should_be_updated_when_a_shape_is_removed() {
        Shape ellipse = new Ellipse(20, 20, 30, 30);
        interact(() -> app.getDrawingPane().addShape(ellipse));
        assertEquals("Nombre de formes : 1", app.getStatutBar().getText());

        interact(() -> app.getDrawingPane().removeShape(ellipse));
        assertEquals("Nombre de formes : 0", app.getStatutBar().getText());
    }

    @Test
    public void status_bar_should_be_reset_by_clear() {
        // given:
        clickOn("Triangle");
        moveBy(30, 60).drag().dropBy(70, 40);
        assertEquals("Nombre de formes : 1", app.getStatutBar().getText());

        // when:
        clickOn("Clear");

        // then:
        assertEquals("Nombre de formes : 0", app.getStatutBar().getText());
    }

    @Test
    public void observer_should_be_notified_until_it_is_removed() {
        int[] calls = {0};
        Observer observer = () -> calls[0]++;
        DrawingPane pane = app.getDrawingPane();

        pane.addObserver(observer);
        interact(() -> pane.addShape(new Ellipse(20, 20, 30, 30)));
        assertEquals(1, calls[0]);

        pane.removeObserver(observer);
        interact(() -> pane.addShape(new Ellipse(20, 20, 30, 30)));
        assertEquals(1, calls[0]);
    }

}