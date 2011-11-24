package squidgrid;

import java.awt.image.BufferedImage;

/**Contains the data within a cell.
 *
 * @author Eben
 */
public class MultiCharDataBlock implements DisplayBlock {

    private BufferedImage data[];//stored as an array so that multiple tiles can be in each block
    private int index = 0;//currently displayed image

    public MultiCharDataBlock(BufferedImage image){
        this(new BufferedImage[]{image});
    }

    public MultiCharDataBlock(BufferedImage image[]){
        data = image;
    }

    public BufferedImage getImage() {
        return data[index];
    }

    public BufferedImage getNextImage() {
        if (data.length > 1) {//check if there is more than one image
            index++;
            if (index >= data.length) {
                index = 0;
            }
        }
        return getImage();
    }
}
