import java.awt.Graphics2D;

public class Gate extends Part {
    public TruthTable table;

    public Gate(String name, int x, int y, int width, int height, TruthTable table) {
        super(name, x, y, width, height, table.numInputs, 1);
        this.table = table;
    }

    public Gate(Gate g) {
        super(g);
        this.table = g.table;
    }

    public Gate clone() {
        return new Gate(this);
    }

    @Override
    public boolean[] getBooleanOutputs(boolean[] in) {
        if (inputs.size() < maxInputs) {
            state = false;
        } else {
            state = table.calculate(in);
        }

        boolean[] out = new boolean[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            out[i] = state;
        }
        return out;
    }

    public void draw(Graphics2D g){
        g.setColor(Main.nodeColor);

        for (int i = 0; i < maxInputs; i++) {
            int tempY = (int) y + (i+1)*height/(maxInputs+1);

            g.fillOval(x-5, tempY-5, 10, 10);
        }

        g.fillOval(x+width-5, (int) y+height/2-5, 10, 10);

        g.setColor(state ? Main.onState_Gate : Main.offState_Gate);
        g.fillRect(x, y, width, height);

        drawText(g);
    }

    @Override
    public int[] getOutputNode(Wire w){
        return new int[] {(int) x + width, (int) y + height/2};
    }

    @Override
    public int[] getInputNode(Wire w){
        if (w == null) return new int[] {(int) x, (int) y + (inputs.size())*height/(maxInputs+1)};

        int idx = maxInputs == 1? 0 : inputs.indexOf(w);

        return new int[] {(int) x, (int) y + (idx+1)*height/(maxInputs+1)};
    }
}
