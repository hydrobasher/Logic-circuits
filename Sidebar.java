import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

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
            // System.out.println("drawing drag part x = " + dragPart.x + " y = " + dragPart.y);
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

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        dragPart = null;
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (x > xOffset && dragPart == null) {
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

        if (dragPart != null) {
            if (x < xOffset) {
                dragPart.x = x - dragOffsetX;
                dragPart.y = y - dragOffsetY;
                Panel.parts.add(dragPart);
            }

            dragPart = null;
        }
    }
}
