import java.awt.Graphics2D;

public class Wire {
    public Part start, end;
    public int startIndex, endIndex;
    public boolean state;

    public Wire(Part start, Part end) {
        this.start = start;
        this.end = end;
        this.state = false;
    }

    public void update() {
        // state = start.outputs[startIndex];
        // end.inputs[endIndex] = state;
    }

    public void destroy() {
    }

    public void draw(Graphics2D g) {
        g.setColor(state? Main.onState_Wire : Main.offState_Wire);

        int[] pos1 = start.getOutputNode(this);
        int[] pos2 = end.getInputNode(this);
        g.drawLine(pos1[0], pos1[1], pos2[0], pos2[1]);
    }
}
