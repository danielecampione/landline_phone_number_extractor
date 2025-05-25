package landline_phone_number_extractor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandlinePhoneNumberExtractor extends JFrame {

    private static final long serialVersionUID = -2354799664365373469L;
    
    private JTextArea leftTextArea;
    private JTextArea rightTextArea;
    private JButton processButton;
    
    // Due radio buttons mutuamente esclusivi:
    private JRadioButton ignoreRadioButton;
    private JRadioButton onlyRadioButton;

    public LandlinePhoneNumberExtractor() {
        super("Landline Phone Number Extractor - Filtro Numeri Fissi");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        leftTextArea = new JTextArea();
        rightTextArea = new JTextArea();
        rightTextArea.setEditable(false);

        JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
        JScrollPane rightScrollPane = new JScrollPane(rightTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        splitPane.setResizeWeight(0.5);

        // Creazione dei radio buttons e del relativo ButtonGroup
        ignoreRadioButton = new JRadioButton("Ignora numeri con prefisso 884 o 772");
        onlyRadioButton   = new JRadioButton("Dammi solo numeri fissi con prefisso 884 o 772");
        // Modalità di default: "Ignora"
        ignoreRadioButton.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(ignoreRadioButton);
        group.add(onlyRadioButton);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(ignoreRadioButton);
        topPanel.add(onlyRadioButton);

        processButton = new JButton("Elabora");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNumbers();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(processButton, BorderLayout.SOUTH);
    }

    /**
     * Verifica se un token (numero) soddisfa il criterio richiesto.
     *
     * La normalizzazione prevede:
     * • Rimozione del prefisso internazionale "+39" o "39" se presente.
     * • Se il token inizia con "02", viene considerato un fisso milanese: 
     *   si rimuove l'area ("02") e, in modalità only, il token è valido
     *   se dopo la rimozione inizia per "884" o "772", mentre in modalità ignore
     *   il token è valido se **non** inizia per "884" o "772". Se dopo "02"
     *   il token è vuoto, lo consideriamo non valido.
     * • Se il token non inizia con "02" e la sua lunghezza è al massimo 6 cifre
     *   lo trattiamo come interno: in modalità only è valido, in modalità ignore
     *   lo filtriamo.
     * • Altrimenti, il token (presumibilmente di cellulare o altro) viene escluso.
     *
     * @param token il token da analizzare.
     * @param onlyMode true per la modalità "Dammi solo numeri fissi con prefisso 884 o 772"
     *                 false per la modalità "Ignora numeri con prefisso 884 o 772".
     * @return true se il token soddisfa i criteri richiesti per la modalità.
     */
    private boolean qualifies(String token, boolean onlyMode) {
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
            // Se il token è breve (massimo 6 cifre) lo trattiamo come interno.
            if (token.length() <= 6) {
                return onlyMode; // In modalità only, lo includiamo; altrimenti lo escludiamo.
            } else {
                // Token lungo e senza area -> non fisso.
                return false;
            }
        }
    }

    private void processNumbers() {
        String input = leftTextArea.getText();
        String[] lines = input.split("\\r?\\n");
        StringBuilder output = new StringBuilder();

        // Determiniamo la modalità corrente:
        // onlyMode == true se è selezionato "Dammi solo numeri fissi con prefisso 884 o 772"
        boolean onlyMode = onlyRadioButton.isSelected();

        for (String line : lines) {
            // Pulizia: manteniamo cifre, il segno +, separatori (-, /, \) e spazi.
            String cleaned = line.replaceAll("[^\\d+\\-\\/\\\\\\s]", "").trim();

            // Se la riga è vuota o contiene solo "+39" o "39", restituiamo una riga vuota.
            if (cleaned.isEmpty() || cleaned.equals("+39") || cleaned.equals("39")) {
                output.append("\n");
                continue;
            }

            // Suddividiamo la riga in token utilizzando come delimitatori spazio, trattino, slash o backslash.
            String[] tokens = cleaned.split("[\\s\\-\\/\\\\]+");

            if (onlyMode) {
                // Modalità "Dammi solo numeri fissi con prefisso 884 o 772":
                // Elaboriamo i singoli token e creiamo una nuova riga composta SOLO dai token validi.
                StringBuilder validTokens = new StringBuilder();
                for (String token : tokens) {
                    if (qualifies(token, true)) {
                        if (validTokens.length() > 0) {
                            validTokens.append(" ");
                        }
                        validTokens.append(token);
                    }
                }
                // Se almeno un token risulta valido, restituiamo la riga composta da questi token;
                // altrimenti, la riga viene filtrata.
                if (validTokens.length() > 0) {
                    output.append(validTokens.toString()).append("\n");
                } else {
                    output.append("\n");
                }
            } else {
                // Modalità "Ignora numeri con prefisso 884 o 772":
                // La riga viene restituita SOLO se tutti i token soddisfano il criterio;
                // altrimenti la filtriamo (restituiamo una riga vuota).
                boolean allQualify = true;
                for (String token : tokens) {
                    if (!qualifies(token, false)) {
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
        rightTextArea.setText(output.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LandlinePhoneNumberExtractor().setVisible(true);
            }
        });
    }
}