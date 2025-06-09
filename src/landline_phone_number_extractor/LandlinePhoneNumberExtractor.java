package landline_phone_number_extractor;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//Importa TranslateTransition
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox; // Importa CheckBox
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
    private StyledButton processButton;
    private CheckBox effettiSpecialiCheckBox; // Dichiarazione della CheckBox
    private Label outputLabel;
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
        outputLabel = new Label("Risultato:");
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

        // Crea la CheckBox per gli effetti speciali
        effettiSpecialiCheckBox = new CheckBox("Attiva effetti speciali");
        effettiSpecialiCheckBox.setSelected(false); // Di default, la checkbox è disattivata
        
        processButton = new StyledButton("Elabora");
        processButton.setOnAction(e -> processInput());

        controlsBox.getChildren().addAll(choiceLabel, radioBox, effettiSpecialiCheckBox, processButton);
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
        applyHoverEffect(effettiSpecialiCheckBox);
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
        
        // Se la checkbox è selezionata, esegui l'animazione sul pulsante E SULLA LABEL
        if (effettiSpecialiCheckBox.isSelected()) {
        	// 1. Rotazione del pulsante "Elabora"
            RotateTransition rotateButton = new RotateTransition(Duration.seconds(2), processButton);
            rotateButton.setByAngle(720);
            rotateButton.setInterpolator(Interpolator.EASE_BOTH);
            rotateButton.play();

            // 2. Animazione della label di destra (outputLabel)
            final Duration animationDurationLabel = Duration.seconds(2);
            final Font initialFont = Font.getDefault();
            final Color initialColor = Color.BLACK;

            Timeline labelTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(outputLabel.textFillProperty(), initialColor),
                            new KeyValue(outputLabel.scaleXProperty(), 0), // Inizia da scala 0
                            new KeyValue(outputLabel.scaleYProperty(), 0),
                            new KeyValue(outputLabel.translateXProperty(), 100), // Inizia da 100 a destra
                            new KeyValue(outputLabel.fontProperty(),
                                    Font.font(initialFont.getFamily(), FontWeight.BOLD, initialFont.getSize())),
                            new KeyValue(outputLabel.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(outputLabel.opacityProperty(), 1),
                            new KeyValue(outputLabel.textFillProperty(), Color.DARKGRAY),
                            new KeyValue(outputLabel.scaleXProperty(), 1.2),
                            new KeyValue(outputLabel.scaleYProperty(), 1.2),
                            new KeyValue(outputLabel.translateXProperty(), 0)), // Si muove a 0
                    new KeyFrame(Duration.seconds(1.0), new KeyValue(outputLabel.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(1.5), new KeyValue(outputLabel.opacityProperty(), 0)),
                    new KeyFrame(animationDurationLabel, new KeyValue(outputLabel.textFillProperty(), initialColor),
                            new KeyValue(outputLabel.fontProperty(), initialFont),
                            new KeyValue(outputLabel.scaleXProperty(), 1),
                            new KeyValue(outputLabel.scaleYProperty(), 1),
                            new KeyValue(outputLabel.translateXProperty(), 0),
                            new KeyValue(outputLabel.opacityProperty(), 1)));
            labelTimeline.play();

            // Animazione combinata per outputTextArea: Fade, Scala e Traslazione
            FadeTransition fadeInOutput = new FadeTransition(Duration.seconds(1), outputTextArea);
            fadeInOutput.setFromValue(0);
            fadeInOutput.setToValue(1);

            ScaleTransition scaleOutput = new ScaleTransition(Duration.seconds(1), outputTextArea);
            scaleOutput.setFromX(0.8); // Inizia da una scala ridotta
            scaleOutput.setFromY(0.8);
            scaleOutput.setToX(1);
            scaleOutput.setToY(1);

            TranslateTransition translateOutput = new TranslateTransition(Duration.seconds(1), outputTextArea);
            translateOutput.setFromX(50); // Inizia da 50 pixel a destra
            translateOutput.setToX(0);

            ParallelTransition parallelTransition = new ParallelTransition(fadeInOutput, scaleOutput, translateOutput);
            parallelTransition.play();
        }
        
        String result = PhoneNumberLogic.processText(inputText, onlyMode);
        
        outputTextArea.setText(result);
        
        // Attiva il focus per mostrare l'effetto "neon" definito nel CSS
        outputTextArea.requestFocus(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
