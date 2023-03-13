package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.*;

import java.awt.*;


import static comp1110.ass2.Arboretum.*;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;
    private static final int GRID_SIZE = 50;
    private static final int GRID_DIMENSION = 10;
    private static final int WINDOW_XOFFSET = 10;
    private static final int WINDOW_YOFFSET = 30;
    private static final int TEXTBOX_WIDTH = 120;

    private final Group root = new Group();
    private final Group controls = new Group();

    private TextField turnIDTextField;
    private TextField aArboretumTextField;
    private TextField bArboretumTextField;
    private TextField aDiscardTextField;
    private TextField bDiscardTextField;
    private TextField deckTextField;
    private TextField aHandTextField;
    private TextField bHandTextField;

    /**
     * Draw a placement in the window, removing any previously drawn placements
     * @author Rita Zhou
     * @param gameState TASK 6
     */
    void displayState(String[][] gameState) {
        // clear the root
        root.getChildren().clear();
        root.getChildren().add(controls);

        // find the validity of the state
        if (isStateValid(gameState)) {
            // show up current turns and the position of two player
            Text currentTurn = writeText("Current Turn: ", 450, 345);
            Text turn = writeText(gameState[0][0], 575, 345);
            Text playerA = writeText("Player A", 490, 680);
            Text playerB = writeText("Player B", 490, 40);
            root.getChildren().addAll(turn, currentTurn, playerA, playerB);

            // show up A's discard
            Text discardA = writeText("A Discard", 100, 580);
            discardA.setRotate(90);
            Rectangle discardCardA = drawRectangle(70, 140, 40, 510);
            root.getChildren().addAll(discardA, discardCardA);
            // find the card
            if (gameState[0][2].length() > 1) {
                char firstA = gameState[0][2].charAt(gameState[0][2].length() - 2);
                char secondA = gameState[0][2].charAt(gameState[0][2].length() - 1);
                String findDiscardA = firstA + String.valueOf(secondA);

                Text discardAName = writeText(findDiscardA, 65, 580);
                root.getChildren().add(discardAName);
            }

            // show up B's discard
            Text discardB = writeText("B Discard", 100, 100);
            discardB.setRotate(90);
            Rectangle discardCardB = drawRectangle(70, 140, 40, 40);
            root.getChildren().addAll(discardB, discardCardB);
            // find the card
            if (gameState[0][4].length() > 1) {
                char firstB = gameState[0][4].charAt(gameState[0][4].length() - 2);
                char secondB = gameState[0][4].charAt(gameState[0][4].length() - 1);
                String findDiscardB = firstB + String.valueOf(secondB);

                Text discardBName = writeText(findDiscardB, 65, 110);
                root.getChildren().add(discardBName);
            }

            // show up deck
            Text deck = writeText("DECK", 100, 325);
            deck.setRotate(90);
            Rectangle deckCard = drawGreyRectangle(70, 140, 40, 250);
            root.getChildren().addAll(deck, deckCard);
            // find the card
            if (gameState[1][0].length() > 1) {
                char firstD = gameState[1][0].charAt(gameState[1][0].length() - 2);
                char secondD = gameState[1][0].charAt(gameState[1][0].length() - 1);
                String findDiscardD = firstD + String.valueOf(secondD);

                Text discardBName = writeText(findDiscardD, 65, 330);
                root.getChildren().add(discardBName);
            }

            // show up A's card on hand
            Text onHandA = writeText("A card on hand", 850, 580);
            onHandA.setRotate(270);
            root.getChildren().add(onHandA);
            // print out all cards
            if (gameState[1][1].length() > 1) {
                for (int i = 0; i < (gameState[1][1].length() - 1) / 2; i++) {
                    char letterA = gameState[1][1].charAt(i * 2 + 1);
                    char numA = gameState[1][1].charAt(i * 2 + 2);
                    String findAOnHand = letterA + String.valueOf(numA);

                    Text nameAOnHand = writeText(findAOnHand, 990, 680 - (i * 40));
                    Rectangle drawAOnHand = drawGreyRectangle(90, 30, 950, 700 - ((i + 1) * 40));
                    root.getChildren().addAll(drawAOnHand, nameAOnHand);
                }
            }

            // show up B's card on hand
            Text onHandB = writeText("B card on hand", 850, 100);
            onHandB.setRotate(270);
            root.getChildren().add(onHandB);
            // print out all cards
            if (gameState[1][2].length() > 1) {
                for (int i = 0; i < (gameState[1][2].length() - 1) / 2; i++) {
                    char letterB = gameState[1][2].charAt(i * 2 + 1);
                    char numB = gameState[1][2].charAt(i * 2 + 2);
                    String findBOnHand = letterB + String.valueOf(numB);

                    Text nameBOnHand = writeText(findBOnHand, 990, 20 + i * 40);
                    Rectangle drawAOnHand = drawGreyRectangle(90, 30, 950, (i + 1) * 40 - 40);
                    root.getChildren().addAll(drawAOnHand, nameBOnHand);
                }
            }

            // draw A's arboretum
            Text arboretumA = writeText("A Arboretum", 470, 650);
            root.getChildren().add(arboretumA);
            // print out all cards
            if (gameState[0][1].length() > 1) {
                for (int i = 1; i <= (gameState[0][1].length() - 1); i = i + 8) {
                    char cardLetterA = gameState[0][1].charAt(i);
                    char cardNumA = gameState[0][1].charAt(i + 1);
                    String findArboretumNameA = cardLetterA + String.valueOf(cardNumA);

                    int y_ray1 = gameState[0][1].charAt(i + 3) - 48;
                    int y_ray2 = gameState[0][1].charAt(i + 4) - 48;
                    int findY = y_ray1 * 10 + y_ray2;

                    int x_ray1 = gameState[0][1].charAt(i + 6) - 48;
                    int x_ray2 = gameState[0][1].charAt(i + 7) - 48;
                    int findX = x_ray1 * 10 + x_ray2;

                    switch (gameState[0][1].charAt(i + 2)) {
                        case ('C'):
                            switch (gameState[0][1].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, 475);
                                    Text name1 = writeText(findArboretumNameA, 505, 500);
                                    root.getChildren().addAll(card1, name1);
                                    continue;
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), 475);
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), 500);
                                    root.getChildren().addAll(card2, name2);
                                    continue;
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), 475);
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), 500);
                                    root.getChildren().addAll(card3, name3);
                                    continue;
                                }
                            }
                        case ('S'):
                            switch (gameState[0][1].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, (475 + (findY * 55)));
                                    Text name1 = writeText(findArboretumNameA, 505, (500 + (findY * 55)));
                                    root.getChildren().addAll(card1, name1);
                                    continue;
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), (475 + (findY * 55)));
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), (500 + (findY * 55)));
                                    root.getChildren().addAll(card2, name2);
                                    continue;
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), (475 + (findY * 55)));
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), (500 + (findY * 55)));
                                    root.getChildren().addAll(card3, name3);
                                    continue;
                                }
                            }
                        case ('N'):
                            switch (gameState[0][1].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, (475 - (findY * 55)));
                                    Text name1 = writeText(findArboretumNameA, 505, (500 - (findY * 55)));
                                    root.getChildren().addAll(card1, name1);
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), (475 - (findY * 55)));
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), (500 - (findY * 55)));
                                    root.getChildren().addAll(card2, name2);
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), (475 - (findY * 55)));
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), (500 - (findY * 55)));
                                    root.getChildren().addAll(card3, name3);
                                }
                            }
                    }
                }
            }

            // draw B arboretum
            Text arboretumB = writeText("B Arboretum", 470, 70);
            root.getChildren().add(arboretumB);
            // print out all cards
            if (gameState[0][3].length() > 1) {
                for (int i = 1; i <= (gameState[0][3].length() - 1); i = i + 8) {
                    char cardLetterA = gameState[0][3].charAt(i);
                    char cardNumA = gameState[0][3].charAt(i + 1);
                    String findArboretumNameA = cardLetterA + String.valueOf(cardNumA);

                    int y_ray1 = gameState[0][3].charAt(i + 3) - 48;
                    int y_ray2 = gameState[0][3].charAt(i + 4) - 48;
                    int findY = y_ray1 * 10 + y_ray2;

                    int x_ray1 = gameState[0][3].charAt(i + 6) - 48;
                    int x_ray2 = gameState[0][3].charAt(i + 7) - 48;
                    int findX = x_ray1 * 10 + x_ray2;

                    switch (gameState[0][3].charAt(i + 2)) {
                        case ('C'):
                            switch (gameState[0][3].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, 185);
                                    Text name1 = writeText(findArboretumNameA, 505, 210);
                                    root.getChildren().addAll(card1, name1);
                                    continue;
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), 185);
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), 210);
                                    root.getChildren().addAll(card2, name2);
                                    continue;
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), 185);
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), 210);
                                    root.getChildren().addAll(card3, name3);
                                    continue;
                                }
                            }
                        case ('S'):
                            switch (gameState[0][3].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, (185 + (findY * 55)));
                                    Text name1 = writeText(findArboretumNameA, 505, (210 + (findY * 55)));
                                    root.getChildren().addAll(card1, name1);
                                    continue;
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), (185 + (findY * 55)));
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), (210 + (findY * 55)));
                                    root.getChildren().addAll(card2, name2);
                                    continue;
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), (185 + (findY * 55)));
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), (210 + (findY * 55)));
                                    root.getChildren().addAll(card3, name3);
                                    continue;
                                }
                            }
                        case ('N'):
                            switch (gameState[0][3].charAt(i + 5)) {
                                case ('C') -> {
                                    Rectangle card1 = drawRectangle(30, 50, 500, (185 - (findY * 55)));
                                    Text name1 = writeText(findArboretumNameA, 505, (210 - (findY * 55)));
                                    root.getChildren().addAll(card1, name1);
                                }
                                case ('W') -> {
                                    Rectangle card2 = drawRectangle(30, 50, (500 - (findX * 40)), (185 - (findY * 55)));
                                    Text name2 = writeText(findArboretumNameA, (505 - (findX * 40)), (210 - (findY * 55)));
                                    root.getChildren().addAll(card2, name2);
                                }
                                case ('E') -> {
                                    Rectangle card3 = drawRectangle(30, 50, (500 + (findX * 40)), (185 - (findY * 55)));
                                    Text name3 = writeText(findArboretumNameA, (505 + (findX * 40)), (210 - (findY * 55)));
                                    root.getChildren().addAll(card3, name3);
                                }
                            }
                    }
                }
            }
        }
        else{
            // raise error message
            // from http://www.java2s.com/example/java/javafx/show-javafx-error-message.html
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DevLaunch Dialog");
            alert.setHeaderText("An error has been encountered");
            alert.setContentText("Invalid Input");

            alert.showAndWait();
        }
    }

    /**
     * A Helper function to help me draw a rectangle with white background and black board
     * @param width the width of rectangle
     * @param length the length of rectangle
     * @param x the x-ray for rectangle placed
     * @param y the y-ray for rectangle placed
     * @return a rectangle
     * @author Rita Zhou
     */
    public static Rectangle drawRectangle(int width, int length, int x, int y){
        Rectangle rectangle = new Rectangle(width, length);
        rectangle.setStyle("-fx-fill: white;" +
                "    -fx-stroke: black;" +
                "    -fx-stroke-width: 2;" +
                "    -fx-stroke-dash-array: 12 2 4 2;" +
                "    -fx-stroke-dash-offset: 6;" +
                "    -fx-stroke-line-cap: square;");
        rectangle.setX(x);
        rectangle.setY(y);
        return rectangle;
    }

    /**
     * A Helper function to help me draw a rectangle in grey
     * @param width the width of rectangle
     * @param length the length of rectangle
     * @param x the x-ray for rectangle placed
     * @param y the y-ray for rectangle placed
     * @return a rectangle
     * @author Rita Zhou
     */
    public static Rectangle drawGreyRectangle(int width, int length, int x, int y){
        Rectangle rectangle = new Rectangle(width, length);
        rectangle.setStyle("-fx-fill: grey;" +
                "    -fx-stroke: grey;" +
                "    -fx-stroke-width: 2;" +
                "    -fx-stroke-dash-array: 12 2 4 2;" +
                "    -fx-stroke-dash-offset: 6;" +
                "    -fx-stroke-line-cap: square;");
        rectangle.setX(x);
        rectangle.setY(y);
        return rectangle;
    }

    /**
     * A helping function to write text
     * @param text A String contain text that we want to write
     * @param x the x-ray for text placed
     * @param y the y-ray for text placed
     * @return a text
     * @author Rita Zhou
     */
    public static Text writeText(String text, int x, int y){
        Text name = new Text(text);
        name.setFont(new Font(20));
        name.setX(x);
        name.setY(y);
        return name;
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Player Turn ID");
        turnIDTextField = new TextField();
        turnIDTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label aArboretum = new Label("Player A Arboretum:");
        aArboretumTextField = new TextField();
        aArboretumTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label aDiscard = new Label("Player A Discard:");
        aDiscardTextField = new TextField();
        aDiscardTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label bArboretum = new Label("Player B Arboretum:");
        bArboretumTextField = new TextField();
        bArboretumTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label bDiscard = new Label("Player B Discard:");
        bDiscardTextField = new TextField();
        bDiscardTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label deck = new Label("Deck:");
        deckTextField = new TextField();
        deckTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label aHand = new Label("Player A Hand:");
        aHandTextField = new TextField();
        aHandTextField.setPrefWidth(TEXTBOX_WIDTH);
        Label bHand = new Label("Player B Hand:");
        bHandTextField = new TextField();
        bHandTextField = new TextField();
        bHandTextField.setPrefWidth(TEXTBOX_WIDTH);

        Button displayState = new Button("Display State");
        displayState.setOnAction(e -> {
            String[] sharedState = {turnIDTextField.getText(), aArboretumTextField.getText(),
                    aDiscardTextField.getText(), bArboretumTextField.getText(), bDiscardTextField.getText()};
            String[] hiddenState = {deckTextField.getText(), aHandTextField.getText(), bHandTextField.getText()};
            displayState(new String[][]{sharedState, hiddenState});
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(boardLabel, turnIDTextField, aArboretum, aArboretumTextField, aDiscard,
                aDiscardTextField, bArboretum, bArboretumTextField, bDiscard, bDiscardTextField, deck, deckTextField,
                aHand, aHandTextField, bHand, bHandTextField, displayState);
        vbox.setSpacing(10);
        vbox.setLayoutX(10.4 * (GRID_SIZE) + (2 * WINDOW_XOFFSET) + (GRID_DIMENSION * GRID_SIZE) + (0.5 * GRID_SIZE));
        vbox.setLayoutY(WINDOW_YOFFSET);

        controls.getChildren().add(vbox);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Arboretum Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}