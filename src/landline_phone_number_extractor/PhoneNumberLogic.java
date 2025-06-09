package landline_phone_number_extractor;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PhoneNumberLogic {

    /**
     * Esegue l'elaborazione del testo di input per estrarre e filtrare i numeri.
     * La logica è identica a quella dell'applicazione Swing originale.
     * @param inputText Il testo proveniente dalla TextArea di input.
     * @param onlyMode true per la modalità "Dammi solo...", false per la modalità "Ignora...".
     * @return Una stringa con il risultato dell'elaborazione, da mostrare nella TextArea di output.
     */
    public static String processText(String inputText, boolean onlyMode) {
        if (inputText == null) {
            return "";
        }
        
        String[] lines = inputText.split("\\r?\\n");
        StringBuilder output = new StringBuilder();

        for (String line : lines) {
            String cleaned = line.replaceAll("[^\\d+\\-\\/\\\\\\s]", "").trim();

            if (cleaned.isEmpty() || cleaned.equals("+39") || cleaned.equals("39")) {
                output.append("\n");
                continue;
            }

            String[] tokens = cleaned.split("[\\s\\-\\/\\\\]+");

            if (onlyMode) {
                StringBuilder validTokens = new StringBuilder();
                for (String token : tokens) {
                    if (qualifies(token, true)) {
                        if (validTokens.length() > 0) {
                            validTokens.append(" ");
                        }
                        validTokens.append(token);
                    }
                }
                if (validTokens.length() > 0) {
                    output.append(validTokens.toString()).append("\n");
                } else {
                    output.append("\n");
                }
            } else {
                boolean allQualify = true;
                for (String token : tokens) {
                    if (!token.trim().isEmpty() && !qualifies(token, false)) {
                        allQualify = false;
                        break;
                    }
                }
                if (allQualify) {
                    output.append(line).append("\n");
                } else {
                    output.append("\n");
                }
            }
        }
        return output.toString();
    }

    /**
     * Verifica se un token (numero) soddisfa il criterio richiesto.
     * La logica è identica a quella dell'applicazione Swing originale.
     */
    private static boolean qualifies(String token, boolean onlyMode) {
        token = token.trim();
        if (token.startsWith("+39")) {
            token = token.substring(3);
        } else if (token.startsWith("39")) {
            token = token.substring(2);
        }
        if (token.startsWith("02")) {
            token = token.substring(2);
            if (token.isEmpty()) {
                return false;
            }
            if (onlyMode) {
                return token.startsWith("884") || token.startsWith("772");
            } else {
                return !(token.startsWith("884") || token.startsWith("772"));
            }
        } else {
            if (token.length() <= 6) {
                // In modalità only, gli interni sono validi solo se soddisfano i prefissi, cosa che non possono fare.
                // In modalità ignore, gli interni vengono mantenuti perché non iniziano con i prefissi da ignorare.
                return !onlyMode;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Attiva un'animazione di "flash" per l'effetto neon.
     * @param shadow l'effetto da animare.
     * @param onFinish un'azione da eseguire alla fine dell'animazione.
     */
    public static void triggerNeonAnimation(InnerShadow shadow, Runnable onFinish) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(shadow.colorProperty(), Color.DEEPSKYBLUE)),
            new KeyFrame(Duration.millis(100), new KeyValue(shadow.radiusProperty(), 20.0)),
            new KeyFrame(Duration.millis(800), new KeyValue(shadow.colorProperty(), Color.TRANSPARENT)),
            new KeyFrame(Duration.millis(800), new KeyValue(shadow.radiusProperty(), 10.0))
        );
        timeline.setOnFinished(e -> onFinish.run());
        timeline.play();
    }
}
