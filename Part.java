import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Part {
    public int x, y;
    public int width, height;
    public String name;
    
    public ArrayList<Wire> inputs = new ArrayList<Wire>();
    public ArrayList<Wire> outputs = new ArrayList<Wire>();

    public int maxInputs;
    public int maxOutputs;

    public boolean state;

    public Part(String name, int x, int y, int width, int height, int maxInputs, int maxOutputs) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxInputs = maxInputs;
        this.maxOutputs = maxOutputs;
    }

    public Part(Part p){
        this.name = p.name;
        this.x = p.x;
        this.y = p.y;
        this.width = p.width;
        this.height = p.height;
        this.maxInputs = p.maxInputs;
        this.maxOutputs = p.maxOutputs;
    }

    public abstract Part clone();

    public void update() {
        boolean[] in = new boolean[inputs.size()];
        for (Wire w : inputs) {
            in[inputs.indexOf(w)] = w.state;
        }

        boolean[] out = getBooleanOutputs(in);

        for (int i = 0; i < outputs.size(); i++) {
            outputs.get(i).state = out[i];
        }
    }

    public abstract boolean[] getBooleanOutputs(boolean[] in);

    public abstract void draw(Graphics2D g);

    public void onClick() { }

    public void onDoubleClick() { }

    public void changeName() {
        String newName = javax.swing.JOptionPane.showInputDialog("Change name", name);
        if (newName != null && !newName.trim().isEmpty() && newName.length() <= 10) {
            name = newName;
        }
    }

    public int[] getInputNode(Wire w) { return new int[] {0, 0}; }

    public int[] getOutputNode(Wire w) { return new int[] {0, 0}; }

    public boolean canAddInput() {
        return (inputs.size() < maxInputs || maxInputs == -1);
    }

    public boolean canAddOutput() {
        return (outputs.size() < maxOutputs || maxOutputs == -1 || maxOutputs == 1);
    }

    public boolean inside(int x, int y){
        return (x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height);
    }

    public void drawText(Graphics2D g) {
        g.setColor(Main.nodeColor);

        FontMetrics fm = g.getFontMetrics();
        int x = this.x + (this.width - fm.stringWidth(name)) / 2;
        int y = this.y + (this.height - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(name, x, y);
    }
}
