package squidpony.squidgrid;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**This interface guarantees some basic functionality for swing components displaying
 * arbitrary text as monospaced. This is typically achieved through displaying as
 * graphics and therefor text editing by the end user is not possible.
 *
 * The coordinate system is (x,y) with the upper left block being (0,0).
 *
 * @author Eben
 */
public interface SGDisplay{

    /**Initializes the component with the supplied values. The pixels sizes will be adjusted slightly
     * to allow all of the characters to take up the same amount of space. Once this has been called,
     * often by the constructor, the component is ready to add to the gui and be displayed.
     *
     * @param width The desired width in pixels.
     * @param height The desired height in pixels.
     * @param rows The specific number of characters to display horizontally
     * @param columns The specific number of characters to display vertically
     * @param font The base font
     */
    public void initDisplay(int width, int height, int rows, int columns, Font font);

    /**Initializes the componenet with the supplied values. The height and width will be adjusted to
     * accomodate the font.
     *
     * @param rows Number of characters horizontally
     * @param columns Number of characters vertically
     * @param font The font to use
     */
    public void initDisplayByCell(int rows, int columns, Font font);


    /**Sets the contents of the component to reflect the two dimensional character
     * array. Will ignore any portion of the array that is outside the bounds of
     * the component itself. The incoming array must be at least as large in either
     * dimension as the number of rows and columns.
     *
     * The default colors of black foreground and white background will be used.
     *
     * @param chars
     */
    public void setText(char chars[][]);

    /**Sets one specific block to the given character. This is far more effecient if
     * only a few spots change. <code>refresh()</code> should be called after all changes
     * are made.
     *
     * @param x The x coordinate to set
     * @param y The y coordinate to set
     * @param c The character to be displayed
     */
    public void setBlock(int x, int y, char c);

    /**Sets one specific block to the given character with the given foreground and background
     * colors.
     *
     * @param x The x coordinate to set
     * @param y The y coordinate to set
     * @param c The character to be displayed
     * @param fore The foreground color
     * @param back The background color
     */
    public void setBlock(int x, int y, char c, Color fore, Color back);

    /**Lets the component know that it should update it's display. This will take
     * advantage of the component's default buffering scheme.
     */
    public void refresh();

    public Dimension getCellDimension();

    public void setCellDimension(Dimension cellDimension);

    public int getColumns();

    public void setColumns(int columns);

    public int getRows();

    public void setRows(int rows);

    /**
     *
     * @return The Swing Component used to display.
     */
    public Component getComponent();
}
