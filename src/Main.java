import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 填空题   fill in the blanks           fill_blanks --<p>
 * 多选题   multiple choice questions    multiple_choice √<p>
 * 单选题   single choice question       SingleChoice √<p>
 * 判断题   true or false                TrueOrFalse √<p>
 * 文字题   Text Problem                 text_problem<p>
 * 应用题   word problem                 word_problem<p>
 * 作文题   writing question             writing question<p>
 * 画图题   drawing question             drawing question<p>
 *  =<p>
 * 音频     audio<p>
 * 纯文本   plain text<p>
 *  =<p>
 * */

public class Main {
    private static final String RECENT_FILES_FILE = "QTM.ini";
    private static final int MAX_RECENT_FILES = 5;
    private static List<String> recentFiles = new ArrayList<>();

    public static void main(String[] args) {
        FlatLightLaf.setup();
        loadRecentFiles();

        // Create main frame
        JFrame frame = new JFrame("QTM 测试系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("欢迎使用 QTM 测试系统", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Recent files panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBorder(BorderFactory.createTitledBorder("最近打开的文件"));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String file : recentFiles) {
            listModel.addElement(file);
        }

        JList<String> recentList = new JList<>(listModel);
        recentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = recentList.getSelectedValue();
                if (selected != null) {
                    openFile(selected, frame);
                }
            }
        });

        recentPanel.add(new JScrollPane(recentList), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JButton openButton = new JButton("打开文件");
        openButton.addActionListener(e -> openFileDialog(frame));

        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openButton);
        buttonPanel.add(exitButton);

        // Add components to main panel
        panel.add(recentPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void openFileDialog(JFrame parent) {
        FileDialog dialog = new FileDialog(parent, "选择QTM文件", FileDialog.LOAD);
        dialog.setFile("*.qtm;*.QTM;*.toml;*.json");
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            String filePath = dialog.getDirectory() + dialog.getFile();
            openFile(filePath, parent);
        }
    }

    private static void openFile(String filePath, JFrame parent) {
        try {
            // Add to recent files
            addRecentFile(filePath);
            parent.dispose();

            // Open the file
            new Reader(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "文件读取错误请重试: " + e.getMessage());
        }
    }

    private static void loadRecentFiles() {
        if (Files.exists(Paths.get(RECENT_FILES_FILE))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(RECENT_FILES_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (new File(line).exists()) { // Only keep files that still exist
                        recentFiles.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("无法读取最近文件列表: " + e.getMessage());
            }
        }
    }

    private static void saveRecentFiles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECENT_FILES_FILE))) {
            for (String file : recentFiles) {
                writer.write(file);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("无法保存最近文件列表: " + e.getMessage());
        }
    }

    private static void addRecentFile(String filePath) {
        // Remove if already exists
        recentFiles.remove(filePath);

        // Add to beginning
        recentFiles.add(0, filePath);

        // Trim to max size
        if (recentFiles.size() > MAX_RECENT_FILES) {
            recentFiles = recentFiles.subList(0, MAX_RECENT_FILES);
        }

        saveRecentFiles();
    }
}