import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Panel extends JPanel implements MouseListener {
    public static ArrayList<Part> parts = new ArrayList<Part>();
    public static ArrayList<Wire> wires = new ArrayList<Wire>();

    public Part dragWire = null;

    public Sidebar sidebar = new Sidebar();

    static int mouseX = 0;
    static int mouseY = 0;

    int dragIdx = -1;
    int dragOffsetX = 0;
    int dragOffsetY = 0;

    public Panel() {
        addMouseListener(this);
        addMouseWheelListener(sidebar); 
        addMouseListener(sidebar);
        addMouseMotionListener(sidebar);

        this.setBackground(Main.background);

        sidebar.add(new Gate("AND", 0, 0, 50, 50, Main.AND_TT));
        sidebar.add(new Gate("NOR", 0, 0, 50, 50, Main.NOR_TT));
        sidebar.add(new Gate("NOT", 0, 0, 50, 50, Main.NOT_TT));
        sidebar.add(new Gate("NAND", 0, 0, 50, 50, Main.NAND_TT));
        sidebar.add(new Led("Q", 0, 0, 50, 50));
        sidebar.add(new Switch("X", 0, 0, 30, 30, false));
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

        // for (Wire w : wires) {
        //     w.update();
        // }

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

        String text2 = "drag part = " + dragIdx;
        g.setColor(Color.BLACK);
        g.drawString(text2, 10, 75);
    }

    public void handleRightButtonClick(int x, int y) {
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
                if (p.inside(x, y) && p.canAddInput() && !(p == dragWire)) {
                    Wire w = new Wire(dragWire, p);
                    wires.add(w);
                    dragWire.outputs.add(w);
                    p.inputs.add(w);
                    break;
                }
            }

            dragWire = null;
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
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            handleRightButtonClick(x, y);
        } else {
            if (dragWire != null) {
                Part p = new Node("", x, y, 10, 10);
                parts.add(p);

                Wire w = new Wire(dragWire, p);
                wires.add(w);
                dragWire.outputs.add(w);
                p.inputs.add(w);

                dragWire = p;
            }

            for (Part p : parts) {
                if (x > p.x && x < p.x + p.width && y > p.y && y < p.y + p.height) {
                    p.onClick();
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
