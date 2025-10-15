import java.awt.Graphics2D;
import java.util.ArrayList;

public class Circuit extends Part {
    public TruthTable[] tables;

    public ArrayList<Part> parts = new ArrayList<Part>();
    public ArrayList<Wire> wires = new ArrayList<Wire>();

    public boolean[] states;

    public Circuit(String name, int x, int y, int width, int height, TruthTable[] tables) {
        super(name, x, y, width, height, tables[0].numInputs, tables.length);
        this.tables = tables;
        states = new boolean[tables.length];
    }

    public Circuit(Circuit c) {
        super(c);
        this.tables = c.tables;
        this.states = c.states.clone();
    }

    public Circuit clone() {
        return new Circuit(this);
    }

    @Override
    public boolean[] getBooleanOutputs(boolean[] in) {
        if (inputs.size() < maxInputs) {
            for (int i = 0; i < states.length; i++) {
                states[i] = false;
            }
        } else {
            for (int i = 0; i < tables.length; i++) {
                states[i] = tables[i].calculate(in);
            }
        }

        if (outputs.size() == 1)
            state = states[0];

        boolean[] out = new boolean[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            out[i] = states[i];
        }
        return out;
    }

    @Override
    public void draw(Graphics2D g){
        g.setColor(Main.nodeColor);

        for (int i = 0; i < maxInputs; i++) {
            int tempY = (int) y + (i+1)*height/(maxInputs+1);

            g.fillOval(x-5, tempY-5, 10, 10);
        }

        for (int i = 0; i < maxOutputs; i++) {
            int tempY = (int) y + (i+1)*height/(maxOutputs+1);

            g.fillOval(x+width-5, tempY-5, 10, 10);
        }

        g.setColor(state ? Main.onState_Gate : Main.offState_Gate);
        g.fillRect(x, y, width, height);

        drawText(g);
    }

    @Override
    public int[] getOutputNode(Wire w){
        int idx = maxOutputs == 1? 0 : outputs.indexOf(w);
        if (idx == -1) idx = outputs.size();

        return new int[] {(int) x + width, (int) y + (idx+1)*height/(maxOutputs+1)};
    }

    @Override
    public int[] getInputNode(Wire w){
        int idx = maxInputs == 1? 0 : inputs.indexOf(w);

        return new int[] {(int) x, (int) y + (idx+1)*height/(maxInputs+1)};
    }
}
