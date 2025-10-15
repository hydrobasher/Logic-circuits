import java.awt.Graphics2D;

public class Button extends Part{
    public Button(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height, 0, 0);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Main.btnColor);
        g.fillRect(x, y, width, height);
        
        drawText(g);
    }

    @Override
    public void onClick() {
        // System.out.println("clicked");
    }
}
