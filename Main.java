import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;

public class Main {
    public static final TruthTable NAND_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {false, true, true, true}
    );

    public static final TruthTable AND_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {true, false, false, false}
    );

    public static final TruthTable NOT_TT = new TruthTable(
        new boolean[][] {
            {true},
            {false},
        },
        new boolean[] {false, true}
    );

    public static final TruthTable NOR_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {false, false, false, true}
    );

    public static final Color background =    new Color(200, 200, 200);
    public static final Color onState_Gate =  new Color(20, 200, 20);
    public static final Color offState_Gate = new Color(200, 20, 20);
    public static final Color onState_Led =   new Color(250, 250, 0);
    public static final Color offState_Led =  new Color(20, 20, 40);
    public static final Color onState_Wire =  new Color(0, 255, 0);
    public static final Color offState_Wire = new Color(10, 10, 10);
    public static final Color switch_On =     new Color(20, 200, 20);
    public static final Color switch_Off =    new Color(200, 20, 20);
    public static final Color nodeColor =     new Color(0, 0, 0);
    public static final Color sidebarColor =  new Color(150, 150, 150);

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Panel panel = new Panel();
            panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
