import java.awt.Graphics2D;

public class Node extends Part {
    public Node(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height, -1, -1);
    }

    public Node(Node n) {
        super(n);
    }

    public Node clone() {
        return new Node(this);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(state ? Main.onState_Led : Main.nodeColor);
        g.fillOval(x, y, width, height);
    }

    @Override
    public boolean[] getBooleanOutputs(boolean[] in) {
        state = false;
        for (Wire w : inputs) {
            if (w.state) {
                state = true;
                break;
            }
        }

        boolean[] out = new boolean[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            out[i] = state;
        }
        return out;
    }

    @Override
    public int[] getOutputNode(Wire w){
        return new int[] {(int) x + width/2, (int) y + height/2};
    }

    @Override
    public int[] getInputNode(Wire w){
        return new int[] {(int) x + width/2, (int) y + height/2};
    }

}
