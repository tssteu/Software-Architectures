import javax.swing.*;
import java.awt.*;

public class FireflyGUI extends JFrame {
    private final Torus torus;
    public FireflyGUI(Torus torus) {
        this.torus = torus;
        setTitle("Firefly Synchronization");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        Firefly[][] grid = torus.getGrid();
        int cellSize = getWidth() / grid.length;

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                Firefly firefly = grid[i][j];
                g.setColor(firefly.isOn() ? Color.GREEN : Color.WHITE);
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }
}