import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;

import java.io.FileReader;

public class Panel extends JPanel implements MouseListener {
    static ArrayList<Part> parts = new ArrayList<Part>();
    static ArrayList<Wire> wires = new ArrayList<Wire>();

    static Sidebar sidebar = new Sidebar();

    static int mouseX = 0;
    static int mouseY = 0;

    int dragIdx = -1;
    int dragOffsetX = 0;
    int dragOffsetY = 0;

    Part dragWire = null;

    int lastClick;

    JButton saveCircuit = new JButton("SAVE");
    JButton clear = new JButton("CLEAR");
    JButton loadFile = new JButton("LOAD FROM FILE");
    JButton saveFile = new JButton("SAVE TO FILE");

    public Panel() {
        setLayout(null);

        addMouseListener(this);
        addMouseWheelListener(sidebar); 
        addMouseListener(sidebar);
        addMouseMotionListener(sidebar);

        this.setBackground(Main.background);

        saveCircuit.setBounds(sidebar.xOffset - 160, 10, 140, 30);
        saveCircuit.addActionListener(this::saveCircuit);
        add(saveCircuit);

        clear.setBounds(sidebar.xOffset - 160, 50, 140, 30);
        clear.addActionListener(this::clear);
        add(clear);

        loadFile.setBounds(sidebar.xOffset - 160, 90, 140, 30);
        loadFile.addActionListener(this::loadFromFile);
        add(loadFile);

        saveFile.setBounds(sidebar.xOffset - 160, 130, 140, 30);
        saveFile.addActionListener(sidebar::save);
        add(saveFile);

        sidebar.add(new Input("X", 0, 0, 30, 30, false));
        sidebar.add(new Output("Q", 0, 0, 50, 50));

        sidebar.add(new Circuit("AND", 0, 0, 50, 50, new TruthTable[] {Main.AND_TT}));
        sidebar.add(new Circuit("NOR", 0, 0, 50, 50, new TruthTable[] {Main.NOR_TT}));
        sidebar.add(new Circuit("NOT", 0, 0, 50, 50, new TruthTable[] {Main.NOT_TT}));
        sidebar.add(new Circuit("NAND", 0, 0, 50, 50, new TruthTable[] {Main.NAND_TT}));

        lastClick = (int) System.currentTimeMillis();
    }

    public void loadFromFile(java.awt.event.ActionEvent e){
        try (FileReader reader = new FileReader("circuits.json")) {
            Gson gson = new Gson();
            ArrayList<Circuit> toLoad = gson.fromJson(reader, new TypeToken<ArrayList<Circuit>>(){}.getType());

            sidebar.clear();

            for (Part p : toLoad) {
                sidebar.add(p);
            }
        } catch (Exception ex) {
            System.out.println("Failed to load from file");
            ex.printStackTrace();
        }
    }

    public void clear(java.awt.event.ActionEvent e){
        dragIdx = -1;
        dragWire = null;

        parts = new ArrayList<Part>();
        wires = new ArrayList<Wire>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        update();
        draw(g2d);

        repaint();
    }

    public void update() {
        mouseX = MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x;
        mouseY = MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y;

        for (Part p : parts) {
            p.update();
        }

        if (dragIdx != -1) {
            Part p = parts.get(dragIdx);
            p.x = mouseX - dragOffsetX;
            p.y = mouseY - dragOffsetY;
        }

        sidebar.update();
    }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setStroke(new BasicStroke(5));
        for (Wire w : wires) {
            w.draw(g);
        }
        g.setStroke(new BasicStroke(2));

        for (Part p : parts) {
            p.draw(g);
        }

        sidebar.draw(g);

        if (dragWire != null) {
            g.setColor(Main.offState_Wire);
            g.setStroke(new BasicStroke(5));
            g.drawLine(dragWire.getOutputNode(null)[0], dragWire.getOutputNode(null)[1], mouseX, mouseY);
            g.setStroke(new BasicStroke(2));
        }

        String txt = "drag part = " + dragIdx;
        g.setColor(Color.BLACK);
        g.drawString(txt, 10, 75);
    }

    public void saveCircuit(java.awt.event.ActionEvent e) {
        dragWire = null;
        dragIdx = -1;

        ArrayList<Input> inputs = new ArrayList<>();
        ArrayList<Output> outputs = new ArrayList<>();

        for (Part p : parts) {
            if (p instanceof Input) {
                inputs.add((Input) p);
            } else if (p instanceof Output) {
                outputs.add((Output) p);
            }
        }

        if (inputs.isEmpty() || outputs.isEmpty() || inputs.size() > 10 || outputs.size() > 10) {
            JOptionPane.showMessageDialog(this, "Circuit must have at least one input and one output", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        inputs.sort(Comparator.comparing((Input s) -> s.y).thenComparing(s -> s.x));
        outputs.sort(Comparator.comparing((Output l) -> l.y).thenComparing(l -> l.x));

        TruthTable[] tables = new TruthTable[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) 
            tables[i] = new TruthTable(inputs.size());
            
        int count = (int) Math.pow(2, inputs.size());
        for (int i = 0; i < count; i++){

            for (int j = 0; j < inputs.size(); j++){
                inputs.get(j).state = tables[0].inputs[i][j];
            }

            calculateTruthTables(inputs, outputs, i, tables);
        }

        for (TruthTable tt : tables)
            System.out.println(tt.toString() + "\n");

        String name = javax.swing.JOptionPane.showInputDialog("Enter name", "Part " + Integer.toString(sidebar.parts.size() + 1));
        
        int stringWidth = Math.max(getFontMetrics(getFont()).stringWidth(name) + 14, 40);
        int preferedWidth = Integer.parseInt(javax.swing.JOptionPane.showInputDialog("Enter width", Integer.toString(stringWidth)));
        int w = Math.max(stringWidth, preferedWidth);
        int h =  25 * Math.max(inputs.size(), outputs.size());
        Circuit c = new Circuit(name, 0, 0, w, h, tables);
        sidebar.add(c);
    }

    public void calculateTruthTables(ArrayList<Input> inputs, ArrayList<Output> outputs, int idx, TruthTable[] tables) {
        boolean flag = true;
        while (flag){
            flag = false;
            for (Part p : parts){
                if (p.update())
                    flag = true;
            }
        }

        for (int i = 0; i < outputs.size(); i++) {
            tables[i].outputs[idx] = outputs.get(i).state;
        }
    }

    public void handleLeftButtonClick(int x, int y) {
        if (dragWire == null) {
            for (int i = 0; i < parts.size(); i++) {
                Part p = parts.get(i);
                if (p.inside(x, y) && p.canAddOutput()) { 
                    dragWire = p;
                    break;
                }
            }
        } else {
            for (int i = 0; i < parts.size(); i++) {
                Part p = parts.get(i);
                if (p.inside(x, y)) {
                    if (p.canAddInput() && !(p == dragWire)) {
                        Wire w = new Wire(dragWire, p);
                        wires.add(w);
                        dragWire.outputs.add(w);
                        p.inputs.add(w);
                    }
                    dragWire = null;
                    return;
                }
            }

            Part p = new Node("", x, y, 10, 10);
            parts.add(p);

            Wire w = new Wire(dragWire, p);
            wires.add(w);
            dragWire.outputs.add(w);
            p.inputs.add(w);

            dragWire = p;
        }
    }

    public void deletePart(int idx) {
        ArrayList<Wire> toRemove = new ArrayList<Wire>();

        for (Wire w : wires) {
            if (w.start == parts.get(idx) || w.end == parts.get(idx)) {
                toRemove.add(w);
            }
        }

        for (Wire w : toRemove) {
            w.destroy();
            wires.remove(w);
        }

        parts.remove(idx);

        ArrayList<Part> toRemoveParts = new ArrayList<Part>();
        for (Part p : parts) {
            if (p instanceof Node && p.inputs.size() == 0 && p.outputs.size() == 0) {
                toRemoveParts.add(p);
            }
        }

        for (Part p : toRemoveParts) {
            parts.remove(p);
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            handleLeftButtonClick(x, y);
        } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            dragWire = null;
            dragIdx = -1;

            for (Part p : parts) {
                if (x > p.x && x < p.x + p.width && y > p.y && y < p.y + p.height) {
                    p.onClick();

                    if (lastClick + 300 > (int) System.currentTimeMillis()) {
                        p.onDoubleClick();
                    } else {
                        lastClick = (int) System.currentTimeMillis();
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && dragIdx == -1) {
            for (int i = 0; i < parts.size(); i++) {
                Part p = parts.get(i);
                if (x > p.x && x < p.x + p.width && y > p.y && y < p.y + p.height) {
                    dragIdx = i;
                    dragOffsetX = x - p.x;
                    dragOffsetY = y - p.y;
                    break;
                }
            }
        } 
    }
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && dragIdx != -1) {
            if (x < 0 || x > Main.WIDTH - sidebar.WIDTH || y < 0 || y > Main.HEIGHT) {
                deletePart(dragIdx);
            }

            dragIdx = -1;
        }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
    
}
