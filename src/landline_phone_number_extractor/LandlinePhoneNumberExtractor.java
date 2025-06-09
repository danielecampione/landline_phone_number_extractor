package landline_phone_number_extractor;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;

public class LandlinePhoneNumberExtractor extends Application {

    private StyledTextArea inputTextArea;
    private NeonTextArea outputTextArea;
    private RadioButton ignoreRadioButton;
    private RadioButton onlyRadioButton;
    private final Duration animationDuration = Duration.millis(200);
    private final double scaleFactor = 1.1;
    private Stage primaryStage; // Memorizza lo Stage principale

    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    	primaryStage.setTitle("Landline Phone Number Extractor - Filtro Numeri Fissi");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.getStyleClass().add("root-pane");

        Label inputLabel = new Label("Testo da analizzare:");
        Label outputLabel = new Label("Risultato:");
        inputLabel.getStyleClass().add("label");
        outputLabel.getStyleClass().add("label");

        inputTextArea = new StyledTextArea();
        inputTextArea.setPromptText("Incolla qui il testo contenente i numeri di telefono...");
        inputTextArea.setOpacity(0);

        outputTextArea = new NeonTextArea();
        outputTextArea.setPromptText("I numeri filtrati appariranno qui...");
        outputTextArea.setEditable(false);
        outputTextArea.setOpacity(0);
        
        VBox.setVgrow(inputTextArea, Priority.ALWAYS);
        VBox.setVgrow(outputTextArea, Priority.ALWAYS);

        VBox inputVBox = new VBox(5, inputLabel, inputTextArea);
        VBox outputVBox = new VBox(5, outputLabel, outputTextArea);
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(inputVBox, outputVBox);
        splitPane.setDividerPositions(0.5);
        SplitPane.setResizableWithParent(inputVBox, true);
        SplitPane.setResizableWithParent(outputVBox, true);
        root.setCenter(splitPane);

        VBox controlsBox = new VBox(15);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(15, 0, 10, 0));

        Label choiceLabel = new Label("Scegli la modalità di filtro:");
        choiceLabel.getStyleClass().add("choice-label");

        ignoreRadioButton = new RadioButton("Ignora numeri con prefisso 884 o 772");
        onlyRadioButton = new RadioButton("Dammi solo numeri fissi con prefisso 884 o 772");
        ignoreRadioButton.setSelected(true);

        ToggleGroup choiceGroup = new ToggleGroup();
        ignoreRadioButton.setToggleGroup(choiceGroup);
        onlyRadioButton.setToggleGroup(choiceGroup);

        HBox radioBox = new HBox(20, ignoreRadioButton, onlyRadioButton);
        radioBox.setAlignment(Pos.CENTER);

        StyledButton processButton = new StyledButton("Elabora");
        processButton.setOnAction(e -> processInput());

        controlsBox.getChildren().addAll(choiceLabel, radioBox, processButton);
        root.setBottom(controlsBox);
        
        Scene scene = new Scene(root, 800, 600);
        // Assicurati che il file styles.css sia nella stessa cartella dei file .class
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        animateTextAreas();
        
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            startCloseAnimation();
        });
        
        // Applica l'effetto di hover ai radio button
        applyHoverEffect(ignoreRadioButton);
        applyHoverEffect(onlyRadioButton);
    }
    
    private void animateTextAreas() {
        FadeTransition fadeInInput = new FadeTransition(Duration.seconds(1), inputTextArea);
        fadeInInput.setFromValue(0);
        fadeInInput.setToValue(1);

        FadeTransition fadeInOutput = new FadeTransition(Duration.seconds(1), outputTextArea);
        fadeInOutput.setFromValue(0);
        fadeInOutput.setToValue(1);
        
        ParallelTransition parallelTransition = new ParallelTransition(fadeInInput, fadeInOutput);
        parallelTransition.play();
    }
    
    // Metodo per l'effetto di ingrandimento
    private void applyHoverEffect(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = (ScaleTransition) node.getProperties().get("scaleTransition");
            if (st != null) st.stop();
            st = new ScaleTransition(animationDuration, node);
            st.setToX(scaleFactor);
            st.setToY(scaleFactor);
            st.play();
            node.getProperties().put("scaleTransition", st);
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = (ScaleTransition) node.getProperties().get("scaleTransition");
            if (st != null) st.stop();
            st = new ScaleTransition(animationDuration, node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            node.getProperties().put("scaleTransition", st);
        });
    }

    private void startCloseAnimation() {
        // Animazione di rotazione
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1.5),
                primaryStage.getScene().getRoot());
        rotateTransition.setByAngle(360);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);

        // Animazione di allontanamento (riduzione della scala)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), primaryStage.getScene().getRoot());
        scaleTransition.setToX(0.1);
        scaleTransition.setToY(0.1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);

        // Animazione di dissolvenza verso il bianco
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), primaryStage.getScene().getRoot());
        fadeTransition.setToValue(0.0);
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);

        // Animazione del colore di sfondo verso il bianco
        Timeline backgroundColorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(primaryStage.getScene().getRoot().styleProperty(),
                                "-fx-background-color: white;")),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(primaryStage.getScene().getRoot().styleProperty(),
                        "-fx-background-color: white;")));

        // Esegui tutte le animazioni in parallelo
        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, scaleTransition,
                fadeTransition, backgroundColorTimeline);
        parallelTransition.setOnFinished(event -> {
            primaryStage.close();
        });
        parallelTransition.play();
    }
    
    private void processInput() {
        String inputText = inputTextArea.getText();
        boolean onlyMode = onlyRadioButton.isSelected();
        
        String result = PhoneNumberLogic.processText(inputText, onlyMode);
        
        outputTextArea.setText(result);
        
        // Attiva il focus per mostrare l'effetto "neon" definito nel CSS
        outputTextArea.requestFocus(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
