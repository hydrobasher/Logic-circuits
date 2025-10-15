import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    static final TruthTable NAND_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {false, true, true, true}
    );

    static final TruthTable AND_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {true, false, false, false}
    );

    static final TruthTable NOT_TT = new TruthTable(
        new boolean[][] {
            {true},
            {false},
        },
        new boolean[] {false, true}
    );

    static final TruthTable NOR_TT = new TruthTable(
        new boolean[][] {
            {true, true},
            {true, false},
            {false, true},
            {false, false}
        },
        new boolean[] {false, false, false, true}
    );

    static final TruthTable[] HALF_ADDER_TT = new TruthTable[] {
        new TruthTable(
            new boolean[][] {
                {true, true},
                {true, false},
                {false, true},
                {false, false}
            },
            new boolean[] {false, true, true, false} // Sum
        ),
        new TruthTable(
            new boolean[][] {
                {true, true},
                {true, false},
                {false, true},
                {false, false}
            },
            new boolean[] {true, false, false, false} // Carry
        )
    };

    static final Color background =    new Color(30, 30, 30);
    static final Color onState_Gate =  new Color(250, 250, 0);
    static final Color offState_Gate = new Color(200, 20, 20);
    static final Color onState_Led =   new Color(250, 250, 0);
    static final Color offState_Led =  new Color(50, 50, 70);
    static final Color onState_Wire =  new Color(0, 255, 0);
    static final Color offState_Wire = new Color(10, 10, 10);
    static final Color switch_On =     new Color(20, 200, 20);
    static final Color switch_Off =    new Color(200, 20, 20);
    static final Color nodeColor =     new Color(0, 0, 0);
    static final Color sidebarColor =  new Color(150, 150, 150);
    static final Color btnColor =      new Color(100, 100, 100);

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    static Panel panel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            panel = new Panel();
            panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Panel.sidebar.save();
                    frame.dispose();
                }
            });
            

            frame.setVisible(true);
        });
    }
}

