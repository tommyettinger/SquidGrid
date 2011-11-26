package squidpony.squidgrid.testing;

import squidpony.squidgrid.*;
import java.awt.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Eben
 */
public class TestDriver {

    SGDisplay display;
    JFrame frame;
    ControlPanel control;
    int c = 0, x = 50, y = 30;

    public TestDriver() {
        display = new SGPanel(x, y, new Font("Arial", Font.PLAIN, 16));
        frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(display.getComponent(), BorderLayout.CENTER);

        control = new ControlPanel();

        frame.getContentPane().add(control, BorderLayout.NORTH);

        changeDisplay();
        frame.pack();
        frame.setLocationRelativeTo(null);
        display.refresh();
        frame.setVisible(true);
        frame.repaint();
        while (!frame.equals(null)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (control.getUpdateReady()) {
//                display.initDisplay(600, 500, control.getXSize(), control.getYSize(), new Font("Ariel", Font.BOLD, 2));
                display.initDisplayByCell(control.getXSize(), control.getYSize(), control.getFontFace());
                changeDisplay();
                frame.pack();
                frame.repaint();
            }
        }
    }

    private void changeDisplay() {
        Random rng = new Random();
        c++;
        for (int i = 0; i < display.getColumns(); i++) {
            for (int k = 0; k < display.getRows(); k++) {
//                display.setBlock(k, i, (char) ('A' + c % 26));
                if (rng.nextBoolean()) {
//                    display.setBlock(k, i, (char) ('A' + rng.nextInt(26)));
                    display.setBlock(k, i, (char) ('A' + (i + k) % 26));
                } else {
//                    display.setBlock(k, i, (char) ('a' + rng.nextInt(26)));
                    display.setBlock(k, i, (char) ('a' + (i + k) % 26));
                }
            }
        }
//        display.refresh();

    }

    public static void main(String args[]) {
        new TestDriver();
    }
}
