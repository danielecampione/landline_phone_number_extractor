package landline_phone_number_extractor;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class StyledButton extends Button {

    public StyledButton(String text) {
        super(text);
        initStyle();
    }

    private void initStyle() {
        getStyleClass().add("styled-button");
        setOnMouseEntered(createMouseEnterHandler());
        setOnMouseExited(createMouseExitHandler());
        setOnMousePressed(createMousePressHandler());
        setOnMouseReleased(createMouseReleaseHandler());
    }

    private EventHandler<MouseEvent> createMouseEnterHandler() {
        return e -> {
            // Animazione di scala e rotazione al passaggio del mouse
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), this);
            scale.setToX(1.1);
            scale.setToY(1.1);
            
            RotateTransition rotate = new RotateTransition(Duration.millis(200), this);
            rotate.setToAngle(5);

            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        };
    }

    private EventHandler<MouseEvent> createMouseExitHandler() {
        return e -> {
            // Ritorno allo stato originale
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), this);
            scale.setToX(1.0);
            scale.setToY(1.0);

            RotateTransition rotate = new RotateTransition(Duration.millis(200), this);
            rotate.setToAngle(0);
            
            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        };
    }

    private EventHandler<MouseEvent> createMousePressHandler() {
        return e -> {
            // Animazione alla pressione
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), this);
            scale.setToX(0.9);
            scale.setToY(0.9);

            RotateTransition rotate = new RotateTransition(Duration.millis(100), this);
            rotate.setToAngle(-5); // Leggera rotazione opposta

            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        };
    }

    private EventHandler<MouseEvent> createMouseReleaseHandler() {
        return e -> {
            // Ritorno allo stato di hover al rilascio
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), this);
            scale.setToX(1.1);
            scale.setToY(1.1);

            RotateTransition rotate = new RotateTransition(Duration.millis(100), this);
            rotate.setToAngle(5);

            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        };
    }
}
