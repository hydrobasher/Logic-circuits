import java.awt.Graphics2D;

public class Switch extends Part {
    public Switch(String name, int x, int y, int width, int height, boolean state) {
        super(name, x, y, width, height, 0, -1);
    }

    public Switch(Switch s) {
        super(s);
    }

    public Switch clone() {
        return new Switch(this);
    }

    @Override
    public void onDoubleClick() {
        changeName();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(state ? Main.switch_On : Main.switch_Off);

        g.fillOval(x, y, width, height);
        g.setColor(Main.nodeColor);
        g.drawOval(x, y, width, height);
        
        drawText(g);
    }

    @Override
    public boolean[] getBooleanOutputs(boolean[] in) {
        boolean[] out = new boolean[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            out[i] = state;
        }
        return out;
    }

    @Override
    public void onClick() {
        state = !state;
    }

    @Override
    public int[] getOutputNode(Wire w){
        return new int[] {(int) x + width/2, (int) y + height/2};
    }
    
}
