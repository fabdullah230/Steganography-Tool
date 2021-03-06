package tool;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Point;
import javax.swing.JPanel;

public class Draw extends JPanel
{
    private Point prevPoint;
    private static BufferedImage paintLayer;
    private Graphics paintLayerGraphics;
    
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
    
    public Draw(final int x, final int y) {
        this.prevPoint = null;
        this.setBackground(Color.black);
        Draw.paintLayer = new BufferedImage(x, y, 2);
        final JButton b = new JButton("Done");
        b.setBounds(50, 100, 400, 500);
        this.add(b);
        (this.paintLayerGraphics = Draw.paintLayer.getGraphics()).setColor(Color.white);
        this.paintLayerGraphics.fillRect(0, 0, Draw.paintLayer.getWidth(), Draw.paintLayer.getHeight());
        this.setBackground(Color.WHITE);
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                if (Draw.this.prevPoint == null) {
                    Draw.access$1(Draw.this, e.getPoint());
                }
                Draw.this.paintLayerGraphics.setColor(Color.black);
                Draw.this.paintLayerGraphics.drawLine(Draw.this.prevPoint.x, Draw.this.prevPoint.y, e.getX(), e.getY());
                Draw.this.repaint();
                Draw.access$1(Draw.this, e.getPoint());
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                Draw.access$1(Draw.this, null);
            }
        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final int rshift = (int)(Math.random() * 8.0);
                    final int gshift = (int)(Math.random() * 8.0);
                    final int bshift = (int)(Math.random() * 8.0);
                    System.out.println(String.valueOf(rshift) + " " + gshift + " " + bshift);
                    final JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(null);
                    final File f = chooser.getSelectedFile();
                    if (f == null) {
                        JOptionPane.showMessageDialog(null, "Action terminated");
                        System.exit(1);
                    }
                    final BufferedImage sample = ImageIO.read(f);
                    for (int i = 0; i < Draw.paintLayer.getWidth() - 2; ++i) {
                        for (int j = 0; j < Draw.paintLayer.getHeight() - 2; ++j) {
                            final Color c = new Color(Draw.paintLayer.getRGB(i, j));
                            if (c.equals(Color.black)) {
                                final Color x = new Color(sample.getRGB(i, j));
                                int r = Draw.binaryToInteger(String.valueOf(Draw.xBit(x.getRed(), 8).substring(0, 5)) + Draw.xBit(rshift, 3));
                                if (r > 255) {
                                    r = 255;
                                }
                                int g = Draw.binaryToInteger(String.valueOf(Draw.xBit(x.getGreen(), 8).substring(0, 5)) + Draw.xBit(gshift, 3));
                                if (g > 255) {
                                    g = 255;
                                }
                                int b = Draw.binaryToInteger(String.valueOf(Draw.xBit(x.getBlue(), 8).substring(0, 5)) + Draw.xBit(bshift, 3));
                                if (b > 255) {
                                    b = 255;
                                }
                                final Color y = new Color(r, g, b);
                                sample.setRGB(i, j, y.getRGB());
                            }
                        }
                    }
                    ImageIO.write(sample, "png", f);
                    JOptionPane.showMessageDialog(null, "Encryption Successful\nYour Hash Key is " + rshift + gshift + bshift, "Encryption Complete", -1);
                    System.exit(1);
                }
                catch (IOException | NullPointerException e2) {
                    JOptionPane.showMessageDialog(null, "Unsupported file format", "Error", 0);
                    System.exit(1);
                }
                catch (ArrayIndexOutOfBoundsException e3) {
                    JOptionPane.showMessageDialog(null, "Selected image is smaller than the message size", "Error", 0);
                    System.exit(1);
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(Draw.paintLayer, 0, 0, this);
    }
    
    static /* synthetic */ void access$1(final Draw draw, final Point prevPoint) {
        draw.prevPoint = prevPoint;
    }
}