import java.awt.Graphics2D;

public class Output extends Part {
    public Output(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height, -1, 0);
    }

    public Output(Output l) {
        super(l);
    }

    public Output clone() {
        return new Output(this);
    }

    @Override
    public void onDoubleClick() {
        changeName();
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

        return new boolean[] {};
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(state ? Main.onState_Led : Main.offState_Led);

        g.fillOval(x, y, width, height);
        g.setColor(Main.nodeColor);
        g.drawOval(x, y, width, height);
        
        drawText(g);
    }

    @Override
    public int[] getInputNode(Wire w){
        return new int[] {(int) x + width/2, (int) y + height/2};
    }
    
}
