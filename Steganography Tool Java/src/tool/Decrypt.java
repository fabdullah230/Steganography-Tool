package tool;

import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Decrypt
{
    public static String xBit(final int n, final int x) {
        String s = Integer.toBinaryString(n);
        if (s.length() > x) {
            return "000";
        }
        while (s.length() < x) {
            s = String.valueOf(0) + s;
        }
        return s;
    }
    
    public static boolean hasNeighbor(final BufferedImage sample, int i, int j, final int r, final int g, final int b) {
        if (i == 0) {
            ++i;
        }
        if (j == 0) {
            ++j;
        }
        if (i == sample.getWidth()) {
            --i;
        }
        if (j == sample.getHeight()) {
            --j;
        }
        return validate(sample, i, j, r, g, b) && (validate(sample, i + 1, j + 1, r, g, b) || validate(sample, i + 1, j - 1, r, g, b) || validate(sample, i - 1, j + 1, r, g, b) || validate(sample, i - 1, j - 1, r, g, b) || validate(sample, i - 1, j, r, g, b) || validate(sample, i, j - 1, r, g, b) || validate(sample, i + 1, j, r, g, b) || validate(sample, i, j + 1, r, g, b));
    }
    
    public static boolean validate(final BufferedImage sample, final int i, final int j, final int r, final int g, final int b) {
        final Color c = new Color(sample.getRGB(i, j));
        return binaryToInteger(xBit(c.getRed(), 8).substring(5, 8)) == r && binaryToInteger(xBit(c.getGreen(), 8).substring(5, 8)) == g && binaryToInteger(xBit(c.getBlue(), 8).substring(5, 8)) == b;
    }
    
    public static Integer binaryToInteger(final String binary) {
        final char[] numbers = binary.toCharArray();
        Integer result = 0;
        int count = 0;
        for (int i = numbers.length - 1; i >= 0; --i) {
            if (numbers[i] == '1') {
                result += (int)Math.pow(2.0, count);
            }
            ++count;
        }
        return result;
    }
    
    public Decrypt() {
        final JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        final File f = chooser.getSelectedFile();
        if (f == null) {
            JOptionPane.showMessageDialog(null, "Action terminated");
            System.exit(1);
        }
        final String n = JOptionPane.showInputDialog(null, "Enter Hash key");
        if (n == null) {
            JOptionPane.showMessageDialog(null, "Action terminated");
            System.exit(1);
        }
        final int r = Character.getNumericValue(n.charAt(0));
        final int g = Character.getNumericValue(n.charAt(1));
        final int b = Character.getNumericValue(n.charAt(2));
        System.out.println(String.valueOf(r) + " " + g + " " + b);
        try {
            final BufferedImage sample = ImageIO.read(f);
            final BufferedImage newImage = new BufferedImage(sample.getWidth(), sample.getHeight(), 1);
            for (int i = 0; i < sample.getWidth() - 2; ++i) {
                for (int j = 0; j < sample.getHeight() - 2; ++j) {
                    final Color c = new Color(sample.getRGB(i, j));
                    if (hasNeighbor(sample, i, j, r, g, b)) {
                        newImage.setRGB(i, j, Color.white.getRGB());
                    }
                }
            }
            final ImageIcon image = new ImageIcon();
            image.setImage(newImage);
            final JFrame frame = new JFrame();
            final JLabel label = new JLabel();
            label.setIcon(image);
            frame.add(label);
            frame.setTitle("Encrypted Message");
            frame.setDefaultCloseOperation(3);
            frame.setSize(newImage.getWidth(), newImage.getHeight());
            frame.setVisible(true);
        }
        catch (IOException | NullPointerException ex2) {
            
            JOptionPane.showMessageDialog(null, "Unsupported file format", "Error", 0);
            System.exit(1);
        }
    }
}
