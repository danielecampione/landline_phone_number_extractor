package landline_phone_number_extractor;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NeonTextArea extends StyledTextArea {

    public NeonTextArea() {
        super();
        initNeonEffect();
    }

    public NeonTextArea(String text) {
        super(text);
        initNeonEffect();
    }

    private void initNeonEffect() {
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.TRANSPARENT);
        innerShadow.setRadius(10);
        innerShadow.setChoke(0.1);
        setEffect(innerShadow);

        focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                startNeonAnimation(innerShadow);
            } else {
                removeNeonEffect(innerShadow);
            }
        });
    }
    
    private void startNeonAnimation(InnerShadow innerShadow) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(innerShadow.colorProperty(), Color.TRANSPARENT)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(innerShadow.colorProperty(), Color.DEEPSKYBLUE))
        );
        timeline.play();
    }

    private void removeNeonEffect(InnerShadow innerShadow) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(innerShadow.colorProperty(), Color.DEEPSKYBLUE)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(innerShadow.colorProperty(), Color.TRANSPARENT))
        );
        timeline.play();
    }
}
