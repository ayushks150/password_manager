import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class PasswordManager {
    private SecretKey secretKey;
    private File passwordFile;

    public PasswordManager() {
        try {
            // Generate or load encryption key
            loadOrGenerateKey();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing password manager: " + e.getMessage());
            System.exit(1);
        }

        // Create GUI
        JFrame frame = new JFrame("M-Pass Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51));
        panel.add(sidebar, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("M-Pass");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(200, 50));
        sidebar.add(titleLabel);

        JButton addButton = new JButton("Add Password");
        addButton.setFont(new Font("Arial", Font.PLAIN, 12));
        addButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(addButton);

        JButton retrieveButton = new JButton("Retrieve Password");
        retrieveButton.setFont(new Font("Arial", Font.PLAIN, 12));
        retrieveButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(retrieveButton);

        JButton generateButton = new JButton("Generate Password");
        generateButton.setFont(new Font("Arial", Font.PLAIN, 12));
        generateButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(generateButton);

        JButton clearButton = new JButton("Clear Passwords");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(clearButton);

        JButton helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.PLAIN, 12));
        helpButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(helpButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.PLAIN, 12));
        quitButton.setPreferredSize(new Dimension(200, 50));
        sidebar.add(quitButton);

        frame.setVisible(true);

        // Set up button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPassword();
            }
        });

        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrievePassword();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPasswords();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void loadOrGenerateKey() throws IOException, NoSuchAlgorithmException {
        File keyFile = new File(".mpass_key");
        if (keyFile.exists()) {
            byte[] keyBytes = new byte[(int) keyFile.length()];
            try (FileInputStream fis = new FileInputStream(keyFile)) {
                fis.read(keyBytes);
            }
            secretKey = new SecretKeySpec(keyBytes, "AES");
        } else {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256, new SecureRandom());
            secretKey = keyGen.generateKey();
            try (FileOutputStream fos = new FileOutputStream(keyFile)) {
                fos.write(secretKey.getEncoded());
            }
        }
        passwordFile = new File(".m_pass");
    }

    private String encrypt(String strToEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    private String decrypt(String strToDecrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    private void addPassword() {
        JFrame addFrame = new JFrame("Add Password");
        addFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        addFrame.add(panel);
        placeAddComponents(panel);

        addFrame.setVisible(true);
    }

    private void placeAddComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel websiteLabel = new JLabel("Website:");
        websiteLabel.setBounds(10, 20, 80, 25);
        panel.add(websiteLabel);

        JTextField websiteText = new JTextField(20);
        websiteText.setBounds(100, 20, 165, 25);
        panel.add(websiteText);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 50, 80, 25);
        panel.add(usernameLabel);

        JTextField usernameText = new JTextField(20);
        usernameText.setBounds(100, 50, 165, 25);
        panel.add(usernameText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 80, 80, 25);
        panel.add(passwordLabel);

        JTextField passwordText = new JTextField(20);
        passwordText.setBounds(100, 80, 165, 25);
        panel.add(passwordText);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(10, 120, 80, 25);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String website = websiteText.getText();
                    String username = usernameText.getText();
                    String password = passwordText.getText();

                    String encryptedPassword = encrypt(password);

                    try (FileWriter fw = new FileWriter(passwordFile, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.println(website + ":" + username + ":" + encryptedPassword);
                    }

                    JOptionPane.showMessageDialog(null, "Password saved successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void retrievePassword() {
        JFrame retrieveFrame = new JFrame("Retrieve Password");
        retrieveFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        retrieveFrame.add(panel);
        placeRetrieveComponents(panel);

        retrieveFrame.setVisible(true);
    }

    private void placeRetrieveComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel websiteLabel = new JLabel("Website:");
        websiteLabel.setBounds(10, 20, 80, 25);
        panel.add(websiteLabel);

        JTextField websiteText = new JTextField(20);
        websiteText.setBounds(100, 20, 165, 25);
        panel.add(websiteText);

        JButton retrieveButton = new JButton("Retrieve");
        retrieveButton.setBounds(10, 50, 150, 25);
        panel.add(retrieveButton);

        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(10, 80, 300, 25);
        panel.add(resultLabel);

        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String website = websiteText.getText();

                    BufferedReader br = new BufferedReader(new FileReader(passwordFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts[0].equals(website)) {
                            String username = parts[1];
                            String decryptedPassword = decrypt(parts[2]);
                            resultLabel.setText("Username: " + username + ", Password: " + decryptedPassword);
                            break;
                        }
                    }
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void generatePassword() {
        String password = new String();
        for (int i = 0; i < 12; i++) {
            password += (char) ((Math.random() * 26) + 97);
        }
        JOptionPane.showMessageDialog(null, "Generated Password: " + password);
    }

    private void clearPasswords() {
        if (passwordFile.delete()) {
            JOptionPane.showMessageDialog(null, "All passwords have been cleared.");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to clear passwords.");
        }
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(null, "This is a simple password manager. Use the buttons to add, retrieve, generate, and clear passwords.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PasswordManager();
        });
    }
}
