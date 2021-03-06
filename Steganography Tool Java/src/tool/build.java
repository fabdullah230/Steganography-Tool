package tool;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.Icon;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class build
{
    public static void main(final String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        final String[] options = { "Encrypt", "Decrypt" };
        final int x = JOptionPane.showOptionDialog(null, "What would you like to do?", "Steganography Tool", -1, 1, null, options, options[0]);
        System.out.println(x);
        if (x == -1) {
        	JOptionPane.showMessageDialog(null, "Action terminated");
            System.exit(1);
        }
        else if (x == 0) {
            final JFrame frame = new JFrame();
            final String[] resolution = { "300x300", "500x500", "700x700" };
            final int y = JOptionPane.showOptionDialog(null, "Select dimensions for the encrypted message board", "Encryption", -1, 1, null, resolution, resolution[0]);
            System.out.println(y);
            int a = 0;
            if (y == -1) {
            	JOptionPane.showMessageDialog(null, "Action terminated");
                System.exit(1);
            }
            else if (y == 0) {
                a = 300;
            }
            else if (y == 1) {
                a = 500;
            }
            else {
                a = 700;
            }
            frame.setDefaultCloseOperation(3);
            frame.add(new Draw(a, a));
            frame.setTitle("Draw your message");
            frame.setSize(a, a);
            frame.setVisible(true);
        }
        else {
            final Decrypt decrypt = new Decrypt();
        }
    }
}
