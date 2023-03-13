package comp1110.ass2.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * There are some helping functions to help me draw the GUI
 * @author Rita Zhou
 */
public class HelpToDrawGUI {

    /**
     * create the list of the Cards to show cards by the input String
     * @param arboretum the string would like to put in Card
     * @param startWith index number
     * @param x x-ray
     * @param newCard if it's a new card, draw the plus image, otherwise, draw the image of the card
     * @return a list of the cards
     * @author Rita Zhou
     */
    public static Cards[] drawArboretum(String arboretum, int startWith, int x, boolean newCard){
        Cards[] cards = new Cards[(arboretum.length()-startWith)/8];
        for (int i = startWith; i < arboretum.length(); i = i + 8) {
            String findArboretumName;
            if (newCard) {
                findArboretumName = "Plus";
            }
            else {
                char cardLetter = arboretum.charAt(i);
                char cardNum = arboretum.charAt(i + 1);
                findArboretumName = cardLetter + String.valueOf(cardNum);
            }

            int y_ray1 = arboretum.charAt(i + 3) - 48;
            int y_ray2 = arboretum.charAt(i + 4) - 48;
            int findY = y_ray1 * 10 + y_ray2;

            int x_ray1 = arboretum.charAt(i + 6) - 48;
            int x_ray2 = arboretum.charAt(i + 7) - 48;
            int findX = x_ray1 * 10 + x_ray2;

            ImageView imageView = new ImageView();
            switch (arboretum.charAt(i + 2)) {
                case ('C'):
                    switch (arboretum.charAt(i + 5)) {
                        case ('C') -> {
                            Cards card1 = new Cards(findArboretumName, 40, 80, x, 325, 0, imageView);
                            card1.draw();
                            cards[(i-startWith)/8] = card1;
                            continue;
                        }
                        case ('W') -> {
                            Cards card2 = new Cards(findArboretumName, 40, 80, x - (findX * 42), 325, 0, imageView);
                            card2.draw();
                            cards[(i-startWith)/8] = card2;
                            continue;
                        }
                        case ('E') -> {
                            Cards card3 = new Cards(findArboretumName, 40, 80, x + (findX * 42), 325 , 0, imageView);
                            card3.draw();
                            cards[(i-startWith)/8] = card3;
                            continue;
                        }
                    }
                case ('S'):
                    switch (arboretum.charAt(i + 5)) {
                        case ('C') -> {
                            Cards card1 = new Cards(findArboretumName, 40, 80, x, 325 + (findY * 82), 0, imageView);
                            card1.draw();
                            cards[(i-startWith)/8] = card1;
                            continue;
                        }
                        case ('W') -> {
                            Cards card2 = new Cards(findArboretumName, 40, 80, x - (findX * 42), 325 + (findY * 82), 0, imageView);
                            card2.draw();
                            cards[(i-startWith)/8] = card2;
                            continue;
                        }
                        case ('E') -> {
                            Cards card3 = new Cards(findArboretumName, 40, 80, x + (findX * 42), 325 + (findY * 82), 0, imageView);
                            card3.draw();
                            cards[(i-startWith)/8] = card3;
                            continue;
                        }
                    }
                case ('N'):
                    switch (arboretum.charAt(i + 5)) {
                        case ('C') -> {
                            Cards card1 = new Cards(findArboretumName, 40, 80, x, 325 - (findY * 82), 0, imageView);
                            card1.draw();
                            cards[(i-startWith)/8] = card1;
                        }
                        case ('W') -> {
                            Cards card2 = new Cards(findArboretumName, 40, 80, x - (findX * 42), 325 - (findY * 82), 0, imageView);
                            card2.draw();
                            cards[(i-startWith)/8] = card2;
                        }
                        case ('E') -> {
                            Cards card3 = new Cards(findArboretumName, 40, 80, x + (findX * 42), 325 - (findY * 82), 0, imageView);
                            card3.draw();
                            cards[(i-startWith)/8] = card3;
                        }
                    }
            }
        }
        return cards;
    }

    /**
     * A helping function to write text
     * @param text A String contain text that we want to write
     * @param x the x-ray for text placed
     * @param y the y-ray for text placed
     * @return a text
     * @author Rita Zhou
     */
    public static Text writeText(String text, int x, int y) {
        Text name = new Text(text);
        name.setFont(new Font(20));
        name.setX(x);
        name.setY(y);
        return name;
    }


    /**
     * A helping function to set up the button
     * @param text the image of would like to show of the button
     * @param width the width of image
     * @param length the length of image
     * @param x the x-ray would like to place in the GUI
     * @param y the y-ray would like to place in the GUI
     * @param rotate rotation number
     * @return the button
     * @author Rita Zhou
     */
    public static Button setButton(String text, int width, int length, int x, int y, double rotate){
        ImageView imageView = new ImageView();
        Image image = new Image("file:assets/" + text+ ".png");
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(length);
        Button button = new Button();
        button.setGraphic(imageView);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setRotate(rotate);
        return button;
    }

    /**
     * A helping function to help me read the text in a file
     * @param file name of the file
     * @return the text written in the file
     * @author Rita Zhou
     */
    protected static String getText(String file){
        StringBuilder result = new StringBuilder();
        try {
            Scanner in = new Scanner(new FileInputStream("assets/"+file+".txt"));
            while (in.hasNext()){
                result.append(in.nextLine()).append("\n");
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
