    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;

    public class Main {
        private static JFrame frame;

        public static void main(String[] args) {
            frame = new JFrame("M-Pass Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setMinimumSize(new Dimension(800, 600));
            frame.setMaximumSize(new Dimension(1200, 900));

            JPanel panel = new JPanel();
            panel.setBackground(new Color(204, 230, 255));
            frame.add(panel);
            placeComponents(panel);

            frame.setVisible(true);
        }

        private static void placeComponents(JPanel panel) {
            panel.setLayout(null);

            JLabel titleLabel = new JLabel("Welcome to M-Pass");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBounds(100, 20, 400, 30);
            panel.add(titleLabel);

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            usernameLabel.setBounds(100, 100, 100, 25);
            panel.add(usernameLabel);

            JTextField usernameText = new JTextField(20);
            usernameText.setBounds(200, 100, 160, 25);
            panel.add(usernameText);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            passwordLabel.setBounds(100, 150, 100, 25);
            panel.add(passwordLabel);

            JPasswordField passwordText = new JPasswordField(20);
            passwordText.setBounds(200, 150, 160, 25);
            panel.add(passwordText);

            JButton loginButton = new JButton("Login");
            loginButton.setFont(new Font("Arial", Font.BOLD, 14));
            loginButton.setBounds(150, 200, 100, 25);
            loginButton.setBackground(new Color(76, 175, 80));
            loginButton.setForeground(Color.WHITE);
            panel.add(loginButton);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameText.getText();
                    String password = new String(passwordText.getPassword());

                    if (username.equals("admin") && password.equals("admin")) {
                        frame.dispose(); // Dispose of the login frame
                        try {
                            new PasswordManager(); // Open the PasswordManager window
                        } catch (IOException ioException) {
                            // Handle IOException if it occurs during PasswordManager initialization
                            ioException.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password. Your unauthorized access attempt has been recorded.");
                    }
                }
            });
        }
    }
