import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCompressor extends JFrame {

    private JFileChooser fileChooser;
    private JButton selectFilesButton;
    private JButton startButton;
    private JButton cancelButton;
    private JProgressBar overallProgressBar;
    private JTextArea statusTextArea;
    private JCheckBox pdfToDocxCheckBox;
    private JCheckBox resizeImageCheckBox;
    private List<File> selectedFiles = new ArrayList<>();
    private ExecutorService executorService;

    public ImageCompressor() {
        setTitle("File Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize components
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        selectFilesButton = new JButton("Select Files");
        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        overallProgressBar = new JProgressBar();
        statusTextArea = new JTextArea();
        pdfToDocxCheckBox = new JCheckBox("PDF to DOCX");
        resizeImageCheckBox = new JCheckBox("Resize Image");

        // Style components
        overallProgressBar.setStringPainted(true);
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);

        // Create and customize panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(selectFilesButton);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        optionsPanel.add(pdfToDocxCheckBox);
        optionsPanel.add(resizeImageCheckBox);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        progressPanel.add(overallProgressBar, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.WEST);
        add(progressPanel, BorderLayout.CENTER);

        // Add action listeners
        selectFilesButton.addActionListener(new SelectFilesAction());
        startButton.addActionListener(new StartConversionAction());
        cancelButton.addActionListener(new CancelAction());

        // Initialize ExecutorService
        executorService = Executors.newFixedThreadPool(4); // Adjust the number of threads as needed
    }

    private class SelectFilesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnValue = fileChooser.showOpenDialog(ImageCompressor.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                selectedFiles.clear(); // Clear previous selections
                for (File file : files) {
                    selectedFiles.add(file);
                }
                statusTextArea.append("Selected files:\n");
                for (File file : selectedFiles) {
                    statusTextArea.append(" - " + file.getName() + "\n");
                }
            }
        }
    }

    private class StartConversionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedFiles.isEmpty()) {
                JOptionPane.showMessageDialog(ImageCompressor.this, "No files selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            overallProgressBar.setMaximum(selectedFiles.size());
            overallProgressBar.setValue(0);

            for (File file : selectedFiles) {
                startConversion(file);
            }
        }

        private void startConversion(File file) {
            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Determine the type of conversion selected
                    String conversionType = pdfToDocxCheckBox.isSelected() ? "PDF to DOCX" : resizeImageCheckBox.isSelected() ? "Resize Image" : "Unknown";

                    // Specify the output directory as the Downloads folder
                    File outputDir = new File(System.getProperty("user.home"), "Downloads");
                    if (!outputDir.exists()) {
                        outputDir.mkdir(); // Create the directory if it doesn't exist
                    }

                    // Create the output file in the Downloads directory
                    File outputFile = new File(outputDir, "converted_" + file.getName());

                    // Simulate the conversion process
                    for (int i = 0; i < 100; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        Thread.sleep(50); // Simulate time-consuming task
                        publish(file.getName() + ": " + conversionType + " - " + i + "% complete");
                        setProgress(i + 1);
                    }

                    // Copy the file to the output file as a simulation of conversion
                    try {
                        Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        publish("Saved converted file: " + outputFile.getName());
                    } catch (IOException ex) {
                        publish("Failed to save file: " + outputFile.getName());
                    }

                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    for (String chunk : chunks) {
                        statusTextArea.append(chunk + "\n");
                    }
                }

                @Override
                protected void done() {
                    overallProgressBar.setValue(overallProgressBar.getValue() + 1);
                    if (overallProgressBar.getValue() == overallProgressBar.getMaximum()) {
                        JOptionPane.showMessageDialog(ImageCompressor.this, "All conversions completed!", "Complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };

            executorService.submit(worker);
        }
    }

    private class CancelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            executorService.shutdownNow();
            statusTextArea.append("Conversion process cancelled.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageCompressor app = new ImageCompressor();
            app.setVisible(true);
        });
    }
}
