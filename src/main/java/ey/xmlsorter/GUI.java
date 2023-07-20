package ey.xmlsorter;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUI extends JFrame{

    private JButton openButton;
    private JButton saveButton;
    private File inputFile;

    public GUI() {
        createView();

        setTitle("XML Sorter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(300, 140);
    }

    private void createView() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JLabel selectedFileLabel = new JLabel();
        panel.add(selectedFileLabel);

        JLabel savedFileName = new JLabel();
        panel.add(savedFileName);

        openButton = new JButton("Open XML File");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    inputFile = fileChooser.getSelectedFile();
                    selectedFileLabel.setText("Selected file: " + inputFile.getName());
                }
            }
        });
        panel.add(openButton);

        saveButton = new JButton("Process and Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sortedRawFileName = null;
                try {
                    sortedRawFileName = XMLDOMSorter.handleFile(inputFile);
                } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    WhiteSpaceFormatter.handleWhiteSpaceFormatting(sortedRawFileName);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                RawFileDeleter.handleDeletion(sortedRawFileName);
                Path sourcePath = Paths.get("sorted_file.xml");

                JFileChooser directoryChooser = new JFileChooser();
                directoryChooser.setDialogTitle("Specify a directory to save the sorted XML");
                directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int userSelection = directoryChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File directoryToSave = directoryChooser.getSelectedFile();

                    Path targetPath = Paths.get(directoryToSave.getAbsolutePath(), "sorted.xml");

                    try {
                        Files.move(sourcePath, targetPath);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    savedFileName.setText("Sorted and saved file: sorted.xml");
                }
            }
        });
        panel.add(saveButton);
    }
}
