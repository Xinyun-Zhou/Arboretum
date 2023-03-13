package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import static comp1110.ass2.Arboretum.*;
import static comp1110.ass2.gui.HelpToDrawGUI.*;
import static comp1110.ass2.gui.InitialState.drawDeck;


public class Game extends Application {
    /* board layout */
    private static final int BOARD_WIDTH = 1200;
    private static final int BOARD_HEIGHT = 700;

    private final BorderPane root = new BorderPane();
    private final Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

    double mouseX = 0;
    double mouseY = 0;
    boolean gameContinue = true;
    boolean newTurn = true;
    int rollTimes = 0;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Arboretum");

        // show up the initial state
        initialState(2, stage);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Setting the menu
     *
     * @param stage use the stage to set the menubar
     * @author Rita Zhou
     */
    private void getMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu restart = new Menu("New Game");
        Menu help = new Menu("Help");

        menuBar.prefWidthProperty().bind(stage.widthProperty());
        root.setTop(menuBar);

        MenuItem playerOf1 = new MenuItem("1 player");
        playerOf1.setOnAction(actionEvent -> {
            root.getChildren().clear();
            initialState(1, stage);
        });
        MenuItem playerOf2 = new MenuItem("2 player");
        playerOf2.setOnAction(actionEvent -> {
            root.getChildren().clear();
            initialState(2, stage);
        });
        restart.getItems().addAll(playerOf1, playerOf2);

        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        rulesAlert.setTitle("Rules");
        rulesAlert.setHeaderText("How to play the Arboretum");
        rulesAlert.setContentText(getText("Rules"));

        Alert rulesNextAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesNextAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        rulesNextAlert.setTitle("Rules");
        rulesNextAlert.setHeaderText("How to score the Arboretum");
        rulesNextAlert.setContentText(getText("Score"));

        Alert controlAlert = new Alert(Alert.AlertType.INFORMATION);
        controlAlert.setTitle("Controls");
        controlAlert.setHeaderText("Controls");
        controlAlert.setContentText(getText("Controls"));

        MenuItem rules = new MenuItem("How to play");
        rules.setOnAction(actionEvent -> {
            rulesAlert.showAndWait();
            rulesNextAlert.showAndWait();
        });
        MenuItem controls = new MenuItem("Controls");
        controls.setOnAction(actionEvent -> controlAlert.showAndWait());
        help.getItems().addAll(controls, rules);

        menuBar.getMenus().addAll(restart, help);
    }

    /**
     * show the initial state in GUI. Everytime the initial state has the different cards on hand
     *
     * @param playerNum the number of the player
     * @param stage     keep track the stage
     * @author Rita Zhou
     */
    public void initialState(int playerNum, Stage stage) {
        String[] sharedState = {"A", "A", "A", "B", "B"};
        String[] hiddenState = {"a1a2a3a4a5a6a7a8b1b2b3b4b5b6b7b8c1c2c3c4c5c6c7c8d1d2d3d4d5d6d7d8j1j2j3j4j5j6j7j8m1m2m3m4m5m6m7m8", "A", "B"};
        String[][] state = {sharedState, hiddenState};
        String[][] initialState = drawDeck(state);
        displayArboretumState(initialState != null ? initialState : new String[0][], playerNum, stage);
        root.getChildren().addAll();
    }

    /**
     * draw the arboretum game in the GUI
     *
     * @param gameState current state of the game
     * @param playerNum the number of player
     * @param stage     keep track the stage
     * @author Rita Zhou
     */
    public void displayArboretumState(String[][] gameState, int playerNum, Stage stage) {
        // clear the root
        root.getChildren().clear();
        getMenuBar(stage);

        if (gameState[0][1].length() == 1 && gameState[0][2].length() == 1 && gameState[0][3].length() == 1 && gameState[0][4].length() == 1)
            rollTimes = 0;

        // find the validity of the state
        if (isStateValid(gameState)) {
            // show up current turns and the position of two player
            Text currentTurn = writeText("Current Turn: ", BOARD_WIDTH / 2 - 75, 50);
            Text turn = writeText(gameState[0][0], BOARD_WIDTH / 2 + 50, 50);
            Text playerA = writeText("Player A", 0, BOARD_HEIGHT / 2);
            playerA.setRotate(90);
            Text playerB = writeText("Player B", BOARD_WIDTH - 50, BOARD_HEIGHT / 2);
            playerB.setRotate(270);
            root.getChildren().addAll(turn, currentTurn, playerA, playerB);

            // show the winner when the game end
            if (newTurn && gameState[1][0].length() == 0) {
                ArrayList<Integer> scoreA = new ArrayList<>();
                ArrayList<Integer> scoreB = new ArrayList<>();
                int totalA = 0;
                int totalB = 0;
                ArrayList<Character> speciesAContains = new ArrayList<>();
                ArrayList<Character> speciesBContains = new ArrayList<>();

                // a message box to show the winner
                Alert endGameAlert = new Alert(Alert.AlertType.INFORMATION);
                endGameAlert.setTitle("Game Over");
                endGameAlert.setHeaderText("Game Over");

                // find the species for each arboretum
                for (int i = 1; i < gameState[0][1].length(); i = i + 8) {
                    if (!speciesAContains.contains(gameState[0][1].charAt(i))) {
                        speciesAContains.add(gameState[0][1].charAt(i));
                    }
                }
                for (int i = 1; i < gameState[0][3].length(); i = i + 8) {
                    if (!speciesBContains.contains(gameState[0][3].charAt(i))) {
                        speciesBContains.add(gameState[0][3].charAt(i));
                    }
                }

                // calculate the score of player A and player B
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'a'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'a'));
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'b'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'b'));
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'c'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'c'));
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'd'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'd'));
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'j'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'j'));
                scoreA.add(getHighestViablePathScore(gameState, 'A', 'm'));
                scoreB.add(getHighestViablePathScore(gameState, 'B', 'm'));

                // calculate the total score
                for (int num : scoreA) {
                    if (num > -1) {
                        totalA += num;
                    }
                }
                for (int num : scoreB) {
                    if (num > -1) {
                        totalB += num;
                    }
                }

                // find the winner
                if (totalA > totalB) {
                    gameContinue = false;
                    endGameAlert.setContentText("Player A WIN the Game!");
                    endGameAlert.showAndWait();
                }

                if (totalA < totalB) {
                    gameContinue = false;
                    endGameAlert.setContentText("Player B WIN the Game!");
                    endGameAlert.showAndWait();
                }

                if (totalA == totalB) {
                    if (speciesAContains.size() > speciesBContains.size()) {
                        gameContinue = false;
                        endGameAlert.setContentText("Player A WIN the Game!");
                        endGameAlert.showAndWait();
                    } else if (speciesAContains.size() < speciesBContains.size()) {
                        gameContinue = false;
                        endGameAlert.setContentText("Player B WIN the Game!");
                        endGameAlert.showAndWait();
                    } else {
                        gameContinue = false;
                        endGameAlert.setContentText("You're TIE now, Plant Tree!");
                        endGameAlert.showAndWait();
                    }
                }
            }

            // play with AI
            if (playerNum == 1 && gameState[0][0].equals("B")) {
                for (int i = 0; i < 2; i++) {
                    // draw from deck
                    String cardDraw = chooseDrawLocation(gameState);
                    if (cardDraw != null && cardDraw.equals("D")) {
                        String randomCard = drawFromDeck(gameState[1][0]);
                        gameState[1][0] = removeCard(gameState[1][0], randomCard, 0);
                        gameState[1][2] += randomCard;
                    } else if (gameState[0][2].substring(gameState[0][2].length() - 2).equals(cardDraw)) {
                        gameState[0][2] = gameState[0][2].substring(0, gameState[0][2].length() - 2);
                        gameState[1][2] += cardDraw;
                    } else if (gameState[0][4].substring(gameState[0][4].length() - 2).equals(cardDraw)) {
                        gameState[0][4] = gameState[0][4].substring(0, gameState[0][4].length() - 2);
                        gameState[1][2] += cardDraw;
                    }
                }

                // find next move
                String[] throwCardAI = generateMove(gameState);
                gameState[0][3] += throwCardAI[0];
                gameState[0][4] += throwCardAI[1];

                int len = gameState[1][2].length();
                for (int i = 1; i < len; i = i + 2) {
                    if (gameState[1][2].charAt(i) == throwCardAI[0].charAt(0) && gameState[1][2].charAt(i + 1) == throwCardAI[0].charAt(1)) {
                        gameState[1][2] = removeCard(gameState[1][2], throwCardAI[0].substring(0, 2), 1);
                        len = len - 2;
                        i = i - 2;
                    } else if (gameState[1][2].charAt(i) == throwCardAI[1].charAt(0) && gameState[1][2].charAt(i + 1) == throwCardAI[1].charAt(1)) {
                        gameState[1][2] = removeCard(gameState[1][2], throwCardAI[1], 1);
                        len = len - 2;
                        i = i - 2;
                    }
                }
                gameState[0][0] = "A";
                newTurn = true;
                displayArboretumState(gameState, playerNum, stage);
            }

            // show up A's discard
            Text discardA = writeText("A Discard", 100, BOARD_HEIGHT - 110);
            root.getChildren().addAll(discardA);
            // find the card
            if (gameState[0][2].length() > 1) {
                char firstA = gameState[0][2].charAt(gameState[0][2].length() - 2);
                char secondA = gameState[0][2].charAt(gameState[0][2].length() - 1);
                String findDiscardA = firstA + String.valueOf(secondA);
                Button discardAButton = setButton(findDiscardA, 100, 200, 150, BOARD_HEIGHT - 50, 90);
                if (gameContinue && rollTimes < 2 && ((gameState[0][0].equals("A") && gameState[1][1].length() < 19) || (gameState[0][0].equals("B") && gameState[1][2].length() < 19))) {
                    // set action: add card to on hand
                    discardAButton.setOnAction((ActionEvent e) -> {
                        String card = gameState[0][2].substring(gameState[0][2].length() - 2);
                        gameState[0][2] = removeCard(gameState[0][2], card, 1);
                        if (gameState[0][0].equals("A")) {
                            gameState[1][1] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                        if (gameState[0][0].equals("B")) {
                            gameState[1][2] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                    });
                }
                root.getChildren().add(discardAButton);
            }

            // show up B's discard
            Text discardB = writeText("B Discard", BOARD_WIDTH - 200, BOARD_HEIGHT - 110);
            root.getChildren().addAll(discardB);
            // find the card
            if (gameState[0][4].length() > 1) {
                char firstB = gameState[0][4].charAt(gameState[0][4].length() - 2);
                char secondB = gameState[0][4].charAt(gameState[0][4].length() - 1);
                String findDiscardB = firstB + String.valueOf(secondB);
                Button discardBButton = setButton(findDiscardB, 100, 200, BOARD_WIDTH - 150, BOARD_HEIGHT - 50, 270);
                if (gameContinue && rollTimes < 2 && ((gameState[0][0].equals("A") && gameState[1][1].length() < 19) || (gameState[0][0].equals("B") && gameState[1][2].length() < 19))) {
                    // set action: add card to on hand
                    discardBButton.setOnAction((ActionEvent e) -> {
                        String card = gameState[0][4].substring(gameState[0][4].length() - 2);
                        gameState[0][4] = removeCard(gameState[0][4], card, 1);
                        if (gameState[0][0].equals("A")) {
                            gameState[1][1] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                        if (gameState[0][0].equals("B")) {
                            gameState[1][2] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                    });
                }
                root.getChildren().add(discardBButton);
            }

            // show up deck
            Text deck = writeText("DECK", BOARD_WIDTH / 2 + 25, BOARD_HEIGHT - 110);
            if (gameState[1][0].length() > 0) {
                Button deckButton = setButton("Back", 100, 200, BOARD_WIDTH / 2 + 50, BOARD_HEIGHT - 50, 90);
                if (gameContinue && rollTimes < 2 && ((gameState[0][0].equals("A") && gameState[1][1].length() < 19) || (gameState[0][0].equals("B") && gameState[1][2].length() < 19))) {
                    // set action: add card to on hand
                    deckButton.setOnAction((ActionEvent e) -> {
                        String card = drawFromDeck(gameState[1][0]);
                        gameState[1][0] = removeCard(gameState[1][0], card, 0);
                        if (gameState[0][0].equals("A")) {
                            gameState[1][1] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                        if (gameState[0][0].equals("B")) {
                            gameState[1][2] += card;
                            rollTimes++;
                            newTurn = false;
                            displayArboretumState(gameState, playerNum, stage);
                        }
                    });
                }
                root.getChildren().addAll(deck, deckButton);
            }

            // show up A's card on hand
            Text onHandA = writeText("A card on hand", BOARD_WIDTH / 10, 50);
            root.getChildren().add(onHandA);
            // print out all cards
            if (gameState[1][1].length() > 1 && gameState[0][0].equals("A")) {
                for (int i = 0; i < (gameState[1][1].length() - 1) / 2; i++) {
                    char letterA = gameState[1][1].charAt(i * 2 + 1);
                    char numA = gameState[1][1].charAt(i * 2 + 2);
                    String findAOnHand = letterA + String.valueOf(numA);
                    ImageView imageView = new ImageView();
                    int x = i * 40;
                    int y = 60;
                    Cards drawAOnHand = new Cards(findAOnHand, 40, 80, x, y, 0, imageView);
                    Set<String> validPlace = getAllValidPlacements(gameState, findAOnHand);

                    imageView.setOnMousePressed(mouseEvent -> {
                        // right-click the card then the card will add to the discard A
                        if (gameState[1][1].length() == 17) {
                            if (mouseEvent.isSecondaryButtonDown()) {
                                gameState[1][1] = removeCard(gameState[1][1], findAOnHand, 1);
                                gameState[0][2] += findAOnHand;
                                gameState[0][0] = "B";
                                rollTimes = 0;
                                newTurn = true;
                                displayArboretumState(gameState, playerNum, stage);
                            }
                        } else {
                            // show up all valid place in the pane
                            for (String place : validPlace) {
                                Cards[] cards = drawArboretum(place, 0, BOARD_WIDTH / 3, true);
                                root.getChildren().addAll(cards[0].getImageView());
                            }
                            mouseX = mouseEvent.getX() - x;
                            mouseY = mouseEvent.getY() - y;
                        }
                    });

                    // drag the card
                    imageView.setOnMouseDragged(mouseEvent -> {
                        drawAOnHand.setX(mouseEvent.getSceneX() - mouseX);
                        drawAOnHand.setY(mouseEvent.getSceneY() - mouseY);
                        drawAOnHand.draw();
                    });

                    // put the card to the arboretum A
                    imageView.setOnMouseReleased(mouseEvent -> {
                        if (gameState[1][1].length() == 19) {
                            HashMap<String, Cards> cardsInArboretum = new HashMap<>();
                            for (String place : validPlace) {
                                Cards[] cards = drawArboretum(place, 0, BOARD_HEIGHT / 3, false);
                                cardsInArboretum.put(place, cards[0]);
                            }
                            for (Map.Entry<String, Cards> entry : cardsInArboretum.entrySet()) {
                                // put card in the arboretum
                                if ((entry.getValue().getX() + 200) > mouseEvent.getX() && (entry.getValue().getX() + 160) < mouseEvent.getX() && (entry.getValue().getY() + 60) > mouseEvent.getY() && entry.getValue().getY() < mouseEvent.getY()) {
                                    gameState[0][1] += entry.getKey();
                                    gameState[1][1] = removeCard(gameState[1][1], entry.getKey().substring(0, 2), 1);
                                    displayArboretumState(gameState, playerNum, stage);
                                }
                                // otherwise, put the card back
                                else {
                                    displayArboretumState(gameState, playerNum, stage);
                                }
                            }
                        }
                        // otherwise, put the card back
                        else {
                            displayArboretumState(gameState, playerNum, stage);
                        }
                    });
                    root.getChildren().addAll(imageView);
                    drawAOnHand.draw();
                }
            }

            // not A's turn, don't show A's card
            if (gameState[1][1].length() > 1 && gameState[0][0].equals("B")) {
                for (int i = 0; i < 7; i++) {
                    ImageView imageView = new ImageView();
                    Cards drawAOnHand = new Cards("Back", 40, 80, i * 40, 60, 0, imageView);
                    root.getChildren().addAll(imageView);
                    drawAOnHand.draw();
                }
            }

            // show up B's card on hand
            Text onHandB = writeText("B card on hand", BOARD_WIDTH - (BOARD_WIDTH / 6), 50);
            root.getChildren().add(onHandB);
            // print out all cards
            if (gameState[1][2].length() > 1 && gameState[0][0].equals("B")) {
                for (int i = 0; i < (gameState[1][2].length() - 1) / 2; i++) {
                    char letterB = gameState[1][2].charAt(i * 2 + 1);
                    char numB = gameState[1][2].charAt(i * 2 + 2);
                    String findBOnHand = letterB + String.valueOf(numB);
                    ImageView imageView = new ImageView();
                    int x = (BOARD_WIDTH - ((i + 1) * 40));
                    int y = 60;
                    Cards drawAOnHand = new Cards(findBOnHand, 40, 80, x, y, 0, imageView);
                    Set<String> validPlace = getAllValidPlacements(gameState, findBOnHand);

                    imageView.setOnMousePressed(mouseEvent -> {
                        // right-click to put the card into the discard B
                        if (gameState[1][2].length() == 17) {
                            if (mouseEvent.isSecondaryButtonDown()) {
                                gameState[1][2] = removeCard(gameState[1][2], findBOnHand, 1);
                                gameState[0][4] += findBOnHand;
                                gameState[0][0] = "A";
                                rollTimes = 0;
                                newTurn = true;
                                displayArboretumState(gameState, playerNum, stage);
                            }
                        } else {
                            // show up all valid place in the pane
                            for (String place : validPlace) {
                                Cards[] cards = drawArboretum(place, 0, BOARD_WIDTH / 3 + BOARD_WIDTH / 3, true);
                                root.getChildren().addAll(cards[0].getImageView());
                            }
                            mouseX = mouseEvent.getX() - x;
                            mouseY = mouseEvent.getY() - y;
                        }
                    });

                    // drag the card
                    imageView.setOnMouseDragged(mouseEvent -> {
                        drawAOnHand.setX(mouseEvent.getSceneX() - mouseX);
                        drawAOnHand.setY(mouseEvent.getSceneY() - mouseY);
                        drawAOnHand.draw();
                    });

                    // add the card to the arboretum
                    imageView.setOnMouseReleased(mouseEvent -> {
                        if (gameState[1][2].length() == 19) {
                            HashMap<String, Cards> cardsInArboretum = new HashMap<>();
                            for (String place : validPlace) {
                                Cards[] cards = drawArboretum(place, 0, BOARD_WIDTH / 3 + BOARD_WIDTH / 3, false);
                                cardsInArboretum.put(place, cards[0]);
                            }
                            for (Map.Entry<String, Cards> entry : cardsInArboretum.entrySet()) {
                                // add the card to the arboretum
                                if (entry.getValue().getX() + 40 > mouseEvent.getSceneX() && entry.getValue().getX() < mouseEvent.getSceneX() && entry.getValue().getY() + 60 > mouseEvent.getSceneY() && entry.getValue().getY() < mouseEvent.getSceneY()) {
                                    gameState[0][3] += entry.getKey();
                                    gameState[1][2] = removeCard(gameState[1][2], entry.getKey().substring(0, 2), 1);
                                    displayArboretumState(gameState, playerNum, stage);
                                }
                                // otherwise, put the card back
                                else {
                                    displayArboretumState(gameState, playerNum, stage);

                                }
                            }
                        }
                        // otherwise, put the card back
                        else {
                            displayArboretumState(gameState, playerNum, stage);
                        }
                    });

                    root.getChildren().addAll(imageView);
                    drawAOnHand.draw();
                }
            }

            // not B's turn, don't show B's card on hand
            if (gameState[1][2].length() > 1 && gameState[0][0].equals("A")) {
                for (int i = 0; i < 7; i++) {
                    ImageView imageView = new ImageView();
                    Cards drawAOnHand = new Cards("Back", 40, 80, (BOARD_WIDTH - ((i + 1) * 40)), 60, 0, imageView);
                    root.getChildren().addAll(imageView);
                    drawAOnHand.draw();
                }
            }

            // draw A's arboretum
            Text arboretumA = writeText("A Arboretum", 0, BOARD_HEIGHT / 2);
            arboretumA.setRotate(90);
            root.getChildren().add(arboretumA);
            // print out all cards
            if (gameState[0][1].length() > 1) {
                Cards[] cards = drawArboretum(gameState[0][1], 1, BOARD_WIDTH / 3, false);
                for (Cards card : cards) {
                    root.getChildren().addAll(card.getImageView());
                }
            }

            // draw B arboretum
            Text arboretumB = writeText("B Arboretum", 1100, BOARD_HEIGHT / 2);
            arboretumB.setRotate(270);
            root.getChildren().add(arboretumB);
            // print out all cards
            if (gameState[0][3].length() > 1) {
                Cards[] cards = drawArboretum(gameState[0][3], 1, BOARD_WIDTH / 3 + BOARD_WIDTH / 3, false);
                for (Cards card : cards) {
                    root.getChildren().addAll(card.getImageView());
                }
            }
        } else {
            // raise error message
            // from http://www.java2s.com/example/java/javafx/show-javafx-error-message.html
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("An error has been encountered");
            alert.setContentText("Raise an error message to debug");

            alert.showAndWait();
        }
    }

}


