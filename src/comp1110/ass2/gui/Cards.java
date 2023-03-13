package comp1110.ass2.gui;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class to define the Card, and keep the track of situation of the cards
 * @author Rita Zhou
 */
public class Cards {

    // the name of the card
    private final String text;
    // the width of the card
    private final int width;
    // the length of the card
    private final int length;
    // the x-ray that the card want to place
    private double x;
    // the y-ray that the card want to place
    private double y;
    // if the card is rotated
    private final double rotate;
    // image view of the card
    private final ImageView imageView;

    /**
     * define the card
     * @param text card's name
     * @param width width of the card
     * @param length length of the card
     * @param x x-ray of the card
     * @param y y-ray of the card
     * @param rotate if the card is rotated
     * @param imageView image view of the card
     * @author Rita Zhou
     */
    public Cards(String text, int width, int length, int x, int y, double rotate, ImageView imageView) {
        this.text = text;
        this.width = width;
        this.length = length;
        this.x = x;
        this.y = y;
        this.rotate = rotate;
        this.imageView = imageView;
    }

    /**
     * set the x-ray of the card
     * @param x new x-ray
     * @author Rita Zhou
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * set the y-ray of the card
     * @param y new y-ray
     * @author Rita Zhou
     */
    public void setY(double y) {
        this.y = y;
    }


    /**
     * get the x-ray of the card
     * @return x-ray
     * @author Rita Zhou
     */
    public double getX() {
        return x;
    }

    /**
     * get the y-ray of the card
     * @return y-ray
     * @author Rita Zhou
     */
    public double getY() {
        return y;
    }

    /**
     * get image of the card
     * @return image view
     * @author Rita Zhou
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * set up the image view of the card
     * @author Rita Zhou
     */
    public void draw(){
        Image image = new Image("file:assets/" + text + ".png");
        imageView.setImage(image);
        imageView.setFitHeight(length);
        imageView.setFitWidth(width);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setRotate(rotate);
    }
}
