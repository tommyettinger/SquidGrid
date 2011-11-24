package squidgrid;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**This class is a JPanel that will display a text string as a monospaced font
 * regardless of the font's actual spacing. This is accomplished by displaying
 * the text as a graphic.
 *
 * @author Eben
 */
public class MonospacedPanel extends JPanel implements MonospacedDisplay {

    //the array of the contents of the screen
    private MultiCharDataBlock contents[][];
    private int rows, columns, reduction = 0;
    private Dimension cellDimension, panelDimension;
    private BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_4BYTE_ABGR);

    ;
    private Font font;

    /**Builds a new panel with the desired traits.
     * 
     * @param width Desired width in pixels. May be adjusted to allow cells to all be same size
     * @param height Desired height in pixels. May be adjusted to allow cells to all be same size
     * @param rows Number of cells horizontally.
     * @param columns Number of cells vertically.
     * @param font Base
     */
    public MonospacedPanel(int width, int height, int rows, int columns, Font font) {
        super();
        initDisplay(width, height, rows, columns, font);
    }

    public MonospacedPanel(int rows, int columns, Font font) {
        super();
        initDisplayByCell(rows, columns, font);
    }

    private int findOptimalSize(int x, int check) {
        while (x % check != 0) {
            x++;
        }
        return x;
    }

    private void redrawImage() {
        Graphics2D g2 = image.createGraphics();
        for (int i = 0; i < columns; i++) {
            for (int k = 0; k < rows; k++) {
                g2.drawImage(contents[k][i].getNextImage(), k * cellDimension.width, i * cellDimension.height, null);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public Dimension getCellDimension() {
        return cellDimension;
    }

    public void setCellDimension(Dimension cellDimension) {
        this.cellDimension = cellDimension;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setText(char[][] chars) {
        for (int i = 0; i < columns; i++) {
            for (int k = 0; k < rows; k++) {
                setBlock(k, i, chars[k][i]);
            }
        }
    }

    public void setBlock(int x, int y, char c) {
        setBlock(x,y,c,Color.BLACK,Color.WHITE);
    }

    public void setBlock(int x, int y, char c, Color fore, Color back) {
        Graphics2D g2 = image.createGraphics();
        contents[x][y] = new MultiCharDataBlock(buildImage(c, fore, back));
        g2.drawImage(contents[x][y].getNextImage(), x * cellDimension.width, y * cellDimension.height, null);
    }

    public void refresh() {
        redrawImage();
    }

    public void initDisplay(int width, int height, int rows, int columns, Font font) {
        this.rows = rows;
        this.columns = columns;
        int w = findOptimalSize(width, rows);
        int h = findOptimalSize(height, columns);
        cellDimension = new Dimension(w / rows + 1, h / columns);
        this.font = font;
        setUpFont();
        squeezeCell();
        w = rows * cellDimension.width;
        h = columns * cellDimension.height;
        panelDimension = new Dimension(w, h);
        setSize(panelDimension);
        setMinimumSize(panelDimension);
        setPreferredSize(panelDimension);
        contents = new MultiCharDataBlock[rows][columns];
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    public void initDisplayByCell(int rows, int columns, Font font) {
        this.font = font;
        this.rows = rows;
        this.columns = columns;
        int w = getFontMetrics(font).charWidth('W') + 1;
        int h = getFontMetrics(font).getMaxAscent() + getFontMetrics(font).getDescent();
        cellDimension = new Dimension(w, h);
        reduction = 0;

        BufferedImage test;
        int topSpace = h, bottomSpace = 0;
        ArrayList<Integer> topCheck = new ArrayList<Integer>(),
                bottomCheck = new ArrayList<Integer>();
        for (int i = 0; i < 26; i++) {
            test = buildImage((char) ('A' + i), Color.BLACK, Color.WHITE);
            int color;
            topTest:
            for (int k = 0; k < test.getHeight(); k++) {
                for (int j = 0; j < test.getWidth(); j++) {
                    color = test.getRGB(j, k);
                    if (color != Color.WHITE.getRGB()) {
                        topCheck.add(k);
                        break topTest;
                    }
                }
            }
            test = buildImage((char) ('a' + i), Color.BLACK, Color.WHITE);
            bottomTest:
            for (int k = h - 1; k >= 0; k--) {
                for (int j = 0; j < test.getWidth(); j++) {
                    color = test.getRGB(j, k);
                    if (color != Color.WHITE.getRGB()) {
                        bottomCheck.add(h);
                        break bottomTest;
                    }
                }
            }
        }
        for (Integer t : topCheck) {
            topSpace = Math.min(topSpace, t);
        }
        for (Integer t : bottomCheck) {
            bottomSpace = Math.max(bottomSpace, t);
        }
        h = bottomSpace - topSpace;
        reduction = topSpace - 1;
        h += 2;//gives a one pixel buffer on either side of the image
        cellDimension = new Dimension(w, h);

        w = rows * cellDimension.width;
        h = columns * cellDimension.height;
        panelDimension = new Dimension(w, h);

        setSize(panelDimension);
        setMinimumSize(panelDimension);
        setPreferredSize(panelDimension);
        contents = new MultiCharDataBlock[rows][columns];
        image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.ORANGE);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void squeezeCell() {
        cellDimension.width = getFontMetrics(font).charWidth('W');
        cellDimension.height = getFontMetrics(font).getAscent() + getFontMetrics(font).getDescent();
    }

    private Font setUpFont() {
        int size = 0;
        do {
            size++;
            font = new Font(font.getFontName(), font.getStyle(), size);
        } while ((cellDimension.width > (getFontMetrics(font).charWidth('W'))) && (cellDimension.height > (getFontMetrics(font).getAscent() + getFontMetrics(font).getDescent())));
        return font;
    }

    public Component getComponent() {
        return this;
    }

    /**Builds the image for this data set.
     */
    private BufferedImage buildImage(char c, Color foreground, Color background) {
        BufferedImage i = new BufferedImage(cellDimension.width, cellDimension.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = i.createGraphics();
        g.setColor(background);
        g.fillRect(0, 0, cellDimension.width, cellDimension.height);
        g.setColor(foreground);
        g.setFont(font);

        g.drawString("" + c, (cellDimension.width - g.getFontMetrics().charWidth(c)) / 2,
                g.getFontMetrics().getAscent() - reduction);
        return i;
    }
}
