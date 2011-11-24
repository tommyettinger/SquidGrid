package squidgrid;

import java.awt.image.BufferedImage;

/**This interface guarantees access to certain common elements for data blocks
 * associated with a monospaced display. This interface is concerned with graphical
 * data only, text data displayed should use TextDisplayBlock.
 *
 * @author Eben
 */
public interface DisplayBlock {

    public BufferedImage getNextImage();

    public BufferedImage getImage();
}
