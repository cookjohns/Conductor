//package leap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Canvas represents a drawing surface that allows the user to draw on it
 * freehand, with the leap.
 */
public class Canvas extends JPanel {
    // image where the user's drawing is stored
    private Image drawingBuffer;
    Color color;
    BasicStroke stroke;

    /**
     * Make a canvas.
     * 
     * @param width
     *            width in pixels
     * @param height
     *            height in pixels
     */
    public Canvas(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        //color = Color.BLUE;
        //stroke = new BasicStroke(8);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
            System.out.println("here");
        }
        g.drawImage(drawingBuffer, 0, 0, null);
    }

    public Image getImage() {
        return this.drawingBuffer;
    }

    public void setImage(Image newDrawingBuffer) {
        this.drawingBuffer = newDrawingBuffer;
    }

    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
    }

    /*
     * Make the drawing buffer entirely white.
     */
    private void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // IMPORTANT! every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }

    /*
     * Draw a line between two points (x1, y1) and (x2, y2), specified in pixels
     * relative to the upper-left corner of the drawing buffer.
     */
    void drawLineSegment(int x1, int y1, int x2, int y2, Color color, int stroke) {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        g.setColor(color);
        g.setStroke(new BasicStroke(stroke));
        g.drawLine(x1, y1, x2, y2);
        this.repaint();
    }

    public void changeColorAndSize(Color newColor, BasicStroke basicStroke) {
    	color = newColor;
        stroke = basicStroke;
    }
}

