import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;

public class Sidebar extends MouseAdapter {
    int WIDTH = 200;

    int xOffset = Main.WIDTH - WIDTH;
    int scrollHeight = 0;
    int sidebarHeight = 0;

    ArrayList<Part> parts = new ArrayList<Part>();

    Part dragPart = null;
    int dragOffsetX = 0;
    int dragOffsetY = 0;

    public Sidebar() { }

    public void update(){ 
        if (dragPart != null) {
            dragPart.x = Panel.mouseX - dragOffsetX;
            dragPart.y = Panel.mouseY - dragOffsetY;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Main.sidebarColor);
        g.fillRect(xOffset, 0, WIDTH, Main.HEIGHT);

        for (Part p : parts) {
            if (p.y + p.height > 0 && p.y < Main.HEIGHT) {
                p.draw(g);
            }
        }

        if (dragPart != null) {
            dragPart.draw(g);
        }

        String text = "scroll height = " + scrollHeight;
        g.setColor(Color.BLACK);
        g.drawString(text, 10, 50);
    }

    public void add(Part p) {
        sidebarHeight += p.height + 10;
        p.x = xOffset + (WIDTH - p.width) / 2;
        p.y = sidebarHeight - p.height + scrollHeight;
        parts.add(p);
    }

    public void delete(Part p) {
        sidebarHeight -= p.height + 10;
        int idx = parts.indexOf(p);

        for (int i = 0; i < parts.size(); i++) {
            if (i > idx) {
                parts.get(i).y -= p.height + 10;
            }
        }

        parts.remove(p);
    }

    public void save(java.awt.event.ActionEvent e) {
        ArrayList<Circuit> toSave = new ArrayList<Circuit>();

        for (Part p : parts) {
            if (p instanceof Circuit) 
                toSave.add((Circuit) p);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("circuits.json")) {
            gson.toJson(toSave, writer);
        } catch (Exception _) {
            System.out.println("Save to file failed");
        }
    }

    public void clear() {
        dragPart = null;
        scrollHeight = 0;

        ArrayList<Part> toRemove = new ArrayList<Part>();
        for (Part p : parts) {
            if (p instanceof Circuit) {
                sidebarHeight -= p.height + 10;
                toRemove.add(p);
            }
        }
        parts.removeAll(toRemove);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();

        int add = rotation * -10;

        if (scrollHeight + add > 0) add = -scrollHeight;
        if (scrollHeight + add < Main.HEIGHT - sidebarHeight - 10) add = Math.min(0, Main.HEIGHT - sidebarHeight - 10 - scrollHeight);

        for (Part p : parts) {
            p.y += add;
        }

        scrollHeight += add;
    }

    public void handleRightButtonClick(Part p) {
        if (p instanceof Input || p instanceof Output) return;

        JPopupMenu popup = new JPopupMenu();

        JMenuItem option1 = new JMenuItem("Delete");
        option1.addActionListener(_ -> delete(p));
        popup.add(option1);

        JMenuItem option2 = new JMenuItem("Rename");
        option2.addActionListener(_ -> p.changeName());
        popup.add(option2);
        
        popup.show(Main.panel, p.x, p.y);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        dragPart = null;

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3 && e.getX() > xOffset) {
            int x = e.getX();
            int y = e.getY();

            for (Part p : parts) {
                if (p.inside(x, y)) {
                    handleRightButtonClick(p);
                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && x > xOffset && dragPart == null) {
            for (Part p : parts) {
                if (x > p.x && x < p.x + p.width && y > p.y && y < p.y + p.height) {
                    dragPart = p.clone();
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

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1 && dragPart != null) {
            if (x < xOffset) {
                dragPart.x = x - dragOffsetX;
                dragPart.y = y - dragOffsetY;
                Panel.parts.add(dragPart);
            }

            dragPart = null;
        }
    }
}
