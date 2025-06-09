package landline_phone_number_extractor;

import javafx.scene.control.TextArea;

public class StyledTextArea extends TextArea {

    public StyledTextArea() {
        initStyle();
    }

    public StyledTextArea(String text) {
        super(text);
        initStyle();
    }

    private void initStyle() {
        getStyleClass().add("styled-text-area");
    }
}
