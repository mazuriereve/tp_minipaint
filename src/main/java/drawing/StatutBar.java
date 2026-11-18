package drawing;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Barre de statut affichant le nombre de formes dessinées.
 * Elle observe le DrawingPane et se met à jour à chaque ajout ou suppression de forme.
 */
public class StatutBar extends HBox implements Observer {

    private DrawingPane drawingPane;
    private Label label;

    public StatutBar(DrawingPane drawingPane) {
        this.drawingPane = drawingPane;
        label = new Label();
        getChildren().add(label);
        setPadding(new Insets(5));
        getStyleClass().add("statusbar");

        // abonnement auprès du sujet
        drawingPane.addObserver(this);
        update();
    }

    @Override
    public void update() {
        label.setText("Nombre de formes : " + drawingPane.size());
    }

    public String getText() {
        return label.getText();
    }
}