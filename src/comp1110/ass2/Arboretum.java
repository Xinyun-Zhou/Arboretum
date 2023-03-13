package comp1110.ass2;

import java.util.*;

import static comp1110.ass2.help.Matrix.findMatrix;
import static comp1110.ass2.help.IsArboretumValid.findAdjacent;
import static comp1110.ass2.help.IsArboretumValid.isArboretumWellFormed;

public class Arboretum {

    /**
     * A hiddenState string array is well-formed if it complies with the following rules:
     *
     * hiddenState[0] - Deck
     *                      A number of 2-character card substrings sorted alphanumerically as defined below
     *                      For example: "a3a8b5b6c2c7d1d3d7d8m1"
     *
     * hiddenState[1] - Player A's hand
     *                      0th character is 'A'
     *                      Followed by 7, 8 or 9 2-character card substrings sorted alphanumerically.
     *                      For example a full hand String might look like: "Ab3b4c1j1m2m5m8"
     *
     * hiddenState[2] - Player B's hand
     *                      0th character is 'B'
     *                      Followed by 7, 8 or 9 2-character _card_ substrings
     *                      For example: "Ba6b7b8c8d2j2j8"
     *
     * Where:
     * "card substring" - A 2-character string that represents a single card.
     *                     0th character is 'a', 'b', 'c', 'd', 'j', or 'm' representing the card species.
     *                     1st character is a sequential digit from '1' to '8', representing the value of the card.
     *                     For example: "d7"
     *
     * "alphanumerical(ly)" - This means that cards are sorted first alphabetically and then numerically.
     *                     For example, "m2" appears before "m5" because 2 < 5
     *                     Whilst "b4" appears before "c1" because alphabetical ordering takes precedence over
     *                     numerical ordering.
     *
     * Exceptions:
     *      - If the deck is empty, hiddenState[0] will be the empty string ""
     *      - If a player's hand is empty, then the corresponding hiddenState[i] will contain only the player's ID.
     *      For example: if player A's hand is empty then hiddenState[1] will be "A" with no other characters.
     *
     * @param hiddenState the hidden state array.
     * @return true if the hiddenState array is well-formed, false if it is not well-formed.
     * @author Rita Zhou
     * TASK 3
     */
    public static boolean isHiddenStateWellFormed(String[] hiddenState) {
        // find three element
        if (hiddenState.length == 3){
            // find all element is not null and not empty
            if (hiddenState[0] != null && hiddenState[1] != null && hiddenState[2] != null && hiddenState[1].length() > 0 && hiddenState[2].length() > 0) {
                // find A on hand and B on hand are in a same order
                if (hiddenState[1].charAt(0) == 'A' && hiddenState[2].charAt(0) == 'B') {
                    return isGoodDeck(hiddenState[0]) && isWellFormed(hiddenState[1]) && isWellFormed(hiddenState[2]);
                }
            }
        }
        return false;
    }

    /**
     * Function to check if the deck is a good deck
     * @param deck a String to represent deck
     * @return true if this deck is a good format, otherwise return false
     * @author Rita Zhou
     */
    public static boolean isGoodDeck(String deck){
        // how many card in deck is good
        int num = 0;

        // creat list to find order
        ArrayList<String> cards = new ArrayList<>();
        ArrayList<String> sortCards = new ArrayList<>();

        // find the card in deck is in right format, then put the card in the array list
        if (deck != null && deck.length() % 2 == 0) {
            for (int i = 0; i<deck.length()/2; i++) {
                if (deck.charAt(i*2+1) >= '1' && deck.charAt(i*2+1) <= '8') {
                    if (isTree(deck.charAt(i*2))) {
                        // put in array list
                        char letter = deck.charAt(i*2);
                        char number = deck.charAt(i*2+1);
                        String card = letter + String.valueOf(number);
                        cards.add(card);
                        sortCards.add(card);

                        num++;
                    }
                }
            }
        }
        // find the card is in a right order
        Collections.sort(sortCards);

        return deck == null || (num == deck.length() / 2 && cards.equals(sortCards));
    }

    /**
     * A sharedState string array is well-formed if it complies with the following rules:
     *
     * sharedState[0] - a single character ID string, either "A" or "B" representing whose turn it is.
     * sharedState[1] - Player A's arboretum
     *                     0th character is 'A'
     *                     Followed by a number of 8-character _placement_ substrings as defined below that occur in
     *                       the order they were played. Note: these are NOT sorted alphanumerically.
     *                     For example: "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01"
     *
     * sharedState[2] - Player A's discard
     *                    0th character is 'A'
     *                    Followed by a number of 2-character _card_ substrings that occur in the order they were
     *                    played. Note: these are NOT sorted alphanumerically.
     *                    For example: "Aa7c4d6"
     *
     * sharedState[3] - Player B's arboretum
     *                    0th character is 'B'
     *                    Followed by a number of 8-character _placement_ substrings that occur in the order they
     *                    were played. Note: these are NOT sorted alphanumerically.
     *                    For example: "Bj5C00C00j6S01C00j7S02W01j4C00W01m6C00E01m3C00W02j3N01W01"
     *
     * sharedState[4] - Player B's discard
     *                    0th character is 'B'
     *                    Followed by a number of 2-character _card_ substrings that occur in the order they were
     *                    played. Note: these are NOT sorted alphanumerically.
     *                    For example: "Bb2d4c5a1d5"
     *
     * Where: "card substring" and "alphanumerical" ordering are defined above in the javaDoc for
     * isHiddenStateWellFormed and placement substrings are defined as follows:
     *
     * "placement substring" - An 8-character string that represents a card placement in a player's arboretum.
     *                  - 0th and 1st characters are a 2-character card substring
     *                  - 2nd character is 'N' for North, 'S' for South, or 'C' for Centre representing the
     *                    direction of this card relative to the first card played.
     *                  - 3rd and 4th characters are a 2-digit number from "00" to "99" representing the distance
     *                    from the first card played to this card, in the direction of the 2nd character.
     *                  - 5th character is 'E' for East, 'W' for West, or 'C' for Centre representing the
     *                    direction of this card relative to the first card played.
     *                  - 6th and 7th characters are a 2-digit number from "00" to "99" representing the distance
     *                    from the first card played to this card, in the direction of the 3rd character.
     *                  For example: "a1C00C00b3N01C00" says that card "a1" was played first and card "b3" was played
     *                  one square north of the first card (which was "a1").
     *
     * Exceptions:
     * If a player's discard or arboretum are empty, (ie: there are no cards in them), then the corresponding string
     * should contain only the player ID.
     * For example:
     *  - If Player A's arboretum is empty, then sharedState[1] would be "A" with no other characters.
     *  - If Player B's discard is empty, then sharedState[4] would be "B" with no other characters.
     *
     * @param sharedState the shared state array.
     * @return true if the sharedState array is well-formed, false if it is not well-formed.
     * @author Rita Zhou
     * TASK 4
     */
    public static boolean isSharedStateWellFormed(String[] sharedState) {
        // find the length of state is 5, otherwise out of range
        if (sharedState.length == 5) {
            // check if the first element is A or B
            if (sharedState[0].equals("A") || sharedState[0].equals("B")) {
                // check the order of state is correct
                if(sharedState[1].charAt(0) == 'A' && sharedState[2].charAt(0) == 'A' && sharedState[3].charAt(0) == 'B' && sharedState[4].charAt(0) == 'B'){
                    // find the arboretum and the discard are well format
                    return isArboretumWellFormed(sharedState[1]) && isWellFormed(sharedState[2]) && isArboretumWellFormed(sharedState[3]) && isWellFormed(sharedState[4]);
                }
            }
        }
        return false;
    }

    /**
     * This function is to help if the discard or the cards on player's hand is in a good format
     * @param discard the input of state that unsure about the format
     * @return true if the input state is a good formed, otherwise return false
     * @author Rita Zhou
     */
    public static boolean isWellFormed(String discard){
        int num = 0;
        if (discard.length() >= 1 && (discard.charAt(0) == 'A' || discard.charAt(0) == 'B')) {
            if ((discard.length() - 1) % 2 == 0) {
                for (int i = (discard.length() - 1) / 2; i > 0; i--) {
                    if (discard.charAt(i * 2) >= '1' && discard.charAt(i * 2) <= '8') {
                        if (isTree(discard.charAt((i * 2) - 1)))
                            num++;
                    }
                }
            }
        }
        return discard.length() == 1 || num == (discard.length() - 1) / 2;
    }

    /**
     * Input a char to check if this is a type of tree in the game
     * @param name a char to represent the species of the tree
     * @return if this species is in the arboretum
     * @author Rita Zhou
     */
    public static boolean isTree(char name){
        return name == 'a' || name == 'b' || name == 'c' || name == 'd' || name == 'j' || name == 'm';
    }

    /**
     * Given a deck string, draw a random card from the deck.
     * You may assume that the deck string is well-formed.
     *
     * @param deck the deck string.
     * @return a random cardString from the deck. If the deck is empty, return the empty string "".
     * @author Allen Lionel Yen, Rita Zhou
     * TASK 5
     */
    public static String drawFromDeck(String deck) {
        String result = "";
        if (deck != null && deck.length() > 0){
            int random = (int) (Math.random() * (deck.length()/2));
            char name = deck.charAt(random * 2);
            char num = deck.charAt(random * 2 + 1);
            result = name + String.valueOf(num);
        }
        return result;
    }

    /**
     * Determine whether this placement is valid for the current player. The "Turn String" determines who is making
     * this placement.
     *
     * A placement is valid if the following conditions are met:
     *
     * - The card is placed adjacent to a card that is already placed, or is placed in the position "C00C00" if this is
     * the first card placed.
     * - The card does not share a location with another card that has been placed by this player.
     * - The card being placed is currently in the hand of this player.
     * - The hand of this player has exactly 9 cards in it.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @param placement the placement string of the card to be placed
     * @return false if the placement is valid, false if it is not valid.
     * @author Rita Zhou
     * TASK 7
     */
    public static boolean isPlacementValid(String[][] gameState, String placement) {
        // find the card name in placement
        char name = placement.charAt(0);
        char num = placement.charAt(1);
        String card = name + String.valueOf(num);

        // if it is A's turn, put the card into A's arboretum and find if the state is valid
        if (gameState[0][0].charAt(0) == 'A' && gameState[1][1].length() == 19){
            if (gameState[0][1].length() > 0){
                ArrayList<String> onHandA = findCards(gameState[1][1]);
                if (onHandA != null && onHandA.contains(card)){
                    return isArboretumWellFormed(gameState[0][1] + placement);
                }
            }
        }

        // if it is B's turn, put the card into B's arboretum and find if the state is valid
        if (gameState[0][0].charAt(0) == 'B' && gameState[1][2].length() == 19){
            if (gameState[0][3].length() > 1){
                ArrayList<String> onHandA = findCards(gameState[1][2]);
                if (onHandA != null && onHandA.contains(card)){
                    return isArboretumWellFormed(gameState[0][3] + placement);
                }
            }
        }

        return false;
    }

    /**
     * Given a deck string, draw a random card from the deck.
     * You may assume that the deck string is well-formed.
     *
     * @param gameState the game state array
     * @return true if the gameState is valid, false if it is not valid.
     * @author Rita Zhou
     * TASK 8
     */
    public static boolean isStateValid(String[][] gameState) {
        // create list of String to add cards in the following questions
        ArrayList<String> cards = new ArrayList<>();
        ArrayList<String> cardOnDeck = new ArrayList<>();
        ArrayList<String> arboretumA = new ArrayList<>();
        ArrayList<String> arboretumB = new ArrayList<>();

        // check the length of the hidden state and the shared state are correct, otherwise will out of range
        if (gameState[0].length == 5 && gameState[1].length == 3) {

            // find the list of cards for A on hand, B on hand, A's discard and B's discard
            ArrayList<String> onHandA = findCards(gameState[1][1]);
            ArrayList<String> onHandB = findCards(gameState[1][2]);
            ArrayList<String> discardA = findCards(gameState[0][2]);
            ArrayList<String> discardB = findCards(gameState[0][4]);

            // find out all cards in the deck and put them in cards
            if (gameState[1][0].length() >= 2) {
                for (int i = 0; i < gameState[1][0].length(); i = i + 2) {
                    char first = gameState[1][0].charAt(i);
                    char second = gameState[1][0].charAt(i + 1);
                    String findCard = first + String.valueOf(second);
                    // add the card if the card is not duplicate, add null if the card is duplicated
                    if (!cardOnDeck.contains(findCard))
                        cardOnDeck.add(findCard);
                    else
                        cardOnDeck.add(null);
                }
                cards.addAll(cardOnDeck);
            }

            // find all cards for A on hand and put them in cards
            if (onHandA != null) {
                for (String card : onHandA) {
                    if (!cards.contains(card))
                        cards.add(card);
                    else
                        cards.add(null);
                }
            }

            // find all cards for B on hand and put them in cards
            if (onHandB != null) {
                for (String card : onHandB) {
                    if (!cards.contains(card))
                        cards.add(card);
                    else
                        cards.add(null);
                }
            }

            // find all cards for A's discard and put them in cards
            if (discardA != null) {
                for (String card : discardA) {
                    if (!cards.contains(card))
                        cards.add(card);
                    else
                        cards.add(null);
                }
            }

            // find all cards for B's discard and put them in cards
            if (discardB != null) {
                for (String card : discardB) {
                    if (!cards.contains(card))
                        cards.add(card);
                    else
                        cards.add(null);
                }
            }

            // find all cards for A's arboretum
            if (gameState[0][1].length() > 1) {
                for (int i = 1; i < gameState[0][1].length(); i = i + 8) {
                    char first = gameState[0][1].charAt(i);
                    char second = gameState[0][1].charAt(i + 1);
                    String findCard = first + String.valueOf(second);
                    // add card is not duplicated, add null if the card is duplicate
                    if (!arboretumA.contains(findCard))
                        arboretumA.add(findCard);
                    else
                        arboretumA.add(null);
                }
                cards.addAll(arboretumA);
            }

            // find all cards for B's arboretum
            if (gameState[0][3].length() > 1) {
                for (int i = 1; i < gameState[0][3].length(); i = i + 8) {
                    char first = gameState[0][3].charAt(i);
                    char second = gameState[0][3].charAt(i + 1);
                    String findCard = first + String.valueOf(second);
                    // add card is not duplicate, add null if the card is duplicate
                    if (!arboretumB.contains(findCard))
                        arboretumB.add(findCard);
                    else
                        arboretumB.add(null);
                }
                cards.addAll(arboretumB);
            }

            // define if the state is valid or not
            // if there are 48 cards in the state and no duplicated cards
            if (cards.size() == 48 && !cards.contains(null)) {
                // find if the number of card on arboretum A is always greater than arboretum B
                if (arboretumA.size() == arboretumB.size() || arboretumB.size() + 1 == arboretumA.size()) {
                    // initial state is true
                    if (cardOnDeck.size() == 48 && (onHandA == null || onHandA.size() == 0) && (onHandB == null || onHandB.size() == 0)) {
                        return true;
                    }
                    // find the card number on hand when it is A's turn is correct
                    if (onHandA != null && onHandB != null && gameState[0][0].equals("A") && (onHandA.size() == 7 || onHandA.size() == 8 || onHandA.size() == 9) && onHandB.size() == 7) {
                        if (discardA == null) {
                            return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                        }
                        // find the number of card in arboretum is always greater than discard
                        if (arboretumA.size() >= discardA.size()) {
                            if (discardB == null) {
                                return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                            }
                            // find the number of card in arboretum is always greater than discard
                            else if (arboretumB.size() >= discardB.size()) {
                                return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                            }
                        }
                    }

                    // find the card number on hand when it is A's turn is correct
                    if (onHandA != null && onHandB != null && gameState[0][0].equals("B") && (onHandB.size() == 7 || onHandB.size() == 8 || onHandB.size() == 9) && onHandA.size() == 7) {
                        if (discardA == null) {
                            return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                        }
                        // find the number of card in arboretum is always greater than discard
                        if (arboretumA.size() >= discardA.size()) {
                            if (discardB == null) {
                                return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                            }
                            // find the number of card in arboretum is always greater than discard
                            else if (arboretumB.size() >= discardB.size()) {
                                return isSharedStateWellFormed(gameState[0]) && isHiddenStateWellFormed(gameState[1]);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * A helper function to print out all the cards in the state
     * @param inputString input a String of state
     * @return if the inputString is not empty, contains cards in the state and there are no duplicate,
     * then return a ArrayList contains all cards. Otherwise, return null.
     * @author Rita Zhou
     */
    public static ArrayList<String> findCards(String inputString){
        // output
        ArrayList<String> cardsFound = new ArrayList<>();

        // if there is cards in the String
        if (inputString.length() > 1) {
            for (int i = 1; i < inputString.length(); i = i + 2) {
                // find card
                char letter = inputString.charAt(i);
                char number = inputString.charAt(i + 1);
                String cardFind = letter + String.valueOf(number);

                // add the card to the list if it is not duplicated
                if (!cardsFound.contains(cardFind)) {
                    cardsFound.add(cardFind);
                } else            // return null if the card is duplicated
                    return null;
            }
        }
        return cardsFound;
    }

    /**
     * Determine whether the given player has the right to score the given species. Note: This does not check whether
     * a viable path exists. You may gain the right to score a species that you do not have a viable scoring path for.
     * See "Gaining the Right to Score" in `README.md`.
     * You may assume that the given gameState array is valid.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array.
     * @param player the player attempting to score.
     * @param species the species that is being scored.
     * @return true if the given player has the right to score this species, false if they do not have the right.
     * @author Rita Zhou
     * TASK 9
     */
    public static boolean canScore(String[][] gameState, char player, char species) {
        // find player's card on hand
        ArrayList<String> onHandA = findCards(gameState[1][1]);
        ArrayList<String> onHandB = findCards(gameState[1][2]);

        // find possible cards in array list
        ArrayList<String> possibleCards = possibleCards(species);

        // add valid cards in a set
        Set<String> cardCountA = new HashSet<>();
        Set<String> cardCountB = new HashSet<>();

        // find valid cards for both player A and B
        if (onHandA != null && onHandB != null) {
            for (String card : possibleCards) {
                if (onHandA.contains(card)) {
                    cardCountA.add(card);
                }
                if (onHandB.contains(card)) {
                    cardCountB.add(card);
                }
            }
            // count valid cards amount
            int countA = countAmountOnHand(cardCountA);
            int countB = countAmountOnHand(cardCountB);

            // if we want to know if player A is right to score the species
            if (player == 'A') {
                if (onHandA.contains(species + "8") && onHandB.contains(species + "1")){
                    countA = countA - 8;
                }
                if (onHandA.contains(species + "1") && onHandB.contains(species + "8")){
                    countB = countB - 8;
                }
                return countA >= countB;
            }

            // if we want to know if player B is right to score the species
            if (player == 'B') {
                if (onHandA.contains(species + "8") && onHandB.contains(species + "1")){
                    countA = countA - 8;
                }
                if (onHandA.contains(species + "1") && onHandB.contains(species + "8")){
                    countB = countB - 8;
                }
                return countA <= countB;
            }
        }
        return false;
    }

    /**
     * Find all possible cards in the species provided
     * @param species a char of species that want to find
     * @return a String of ArrayList contains all possible cards
     * @author Rita Zhou
     */
    public static ArrayList<String> possibleCards(char species){
        ArrayList<String> possibleCards = new ArrayList<>();

        // Find cards
        String card1 = species + "1";
        String card2 = species + "2";
        String card3 = species + "3";
        String card4 = species + "4";
        String card5 = species + "5";
        String card6 = species + "6";
        String card7 = species + "7";
        String card8 = species + "8";

        // Add to the array list
        possibleCards.add(card1);
        possibleCards.add(card2);
        possibleCards.add(card3);
        possibleCards.add(card4);
        possibleCards.add(card5);
        possibleCards.add(card6);
        possibleCards.add(card7);
        possibleCards.add(card8);

        return possibleCards;
    }

    /**
     * Count the total value of the card
     * @param availableCards input an arraylist of cards to count
     * @return an integer to represent the total amount of cards
     * @author Rita Zhou
     */
    public static int countAmountOnHand(Set<String> availableCards){
        int out = 0;
        if (availableCards != null && availableCards.size() > 0){
            for (String card : availableCards){
                out = out + (card.charAt(1) - 48);
            }
        }
        return out;
    }

    /**
     * Find all the valid placements for the given card for the player whose turn it is.
     * A placement is valid if it satisfies the following conditions:
     * 1. The card is horizontally or vertically adjacent to at least one other placed card.
     * 2. The card does not overlap with an already-placed card.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @param card the card to play
     * @return a set of valid card placement strings for the current player.
     * TASK 10
     * @author Rita Zhou
     */
    public static Set<String> getAllValidPlacements(String[][] gameState, String card) {
        // an arraylist of cards already placed.
        ArrayList<String> cardPlaced = new ArrayList<>();
        // a set for place of card can put.
        Set<String> cardWillPlace = new HashSet<>();

        String arboretumA = gameState[0][1];
        String arboretumB = gameState[0][3];

        // find player A's valid placement
        if (gameState[0][0].charAt(0) == 'A'){
            // the first card
            if (arboretumA.length() == 1){
                cardWillPlace.add("C00C00");
            }
            // the following cards
            for (int i = 0; i < (arboretumA.length() - 1) / 8; i++) {
                String place = findPlace(arboretumA, i);
                cardPlaced.add(place);
                // the card adjacent with the first one
                if (i == 0) {
                    cardWillPlace.add("C00E01");
                    cardWillPlace.add("C00W01");
                    cardWillPlace.add("S01C00");
                    cardWillPlace.add("N01C00");
                }
                // find all adjacent place and remove the duplicate places
                else {
                    cardWillPlace.addAll(findAdjacent(place));
                    cardPlaced.forEach(cardWillPlace::remove);
                }
            }
        }

        // find player B's valid placement
        if (gameState[0][0].charAt(0) == 'B'){
            // the first card
            if (arboretumB.length() == 1){
                cardWillPlace.add("C00C00");
            }
            // the following cards
            for (int i = 0; i < (arboretumB.length() - 1) / 8; i++) {
                String place = findPlace(arboretumB, i);
                cardPlaced.add(place);
                // the card adjacent with the first one
                if (i == 0) {
                    cardWillPlace.add("C00E01");
                    cardWillPlace.add("C00W01");
                    cardWillPlace.add("S01C00");
                    cardWillPlace.add("N01C00");
                }
                // find all adjacent place and remove the duplicate places
                else {
                    cardWillPlace.addAll(findAdjacent(place));
                    cardPlaced.forEach(cardWillPlace::remove);
                }
            }
        }

        // return in a good format
        String[] willPlace = cardWillPlace.toArray(new String[0]);
        for (String place : willPlace){
            cardWillPlace.add(card+place);
            cardWillPlace.remove(place);
        }
        return cardWillPlace;
    }

    /**
     * find the place of the current card
     * @param arboretum the String of the arboretum
     * @param times the index of the String
     * @return the place of the arboretum
     * @author Rita Zhou
     */
    public static String findPlace(String arboretum, int times){
        char yRayLetter = arboretum.charAt(times * 8 + 3);
        char yRayNum1 = arboretum.charAt(times * 8 + 4);
        char yRayNum2 = arboretum.charAt(times * 8 + 5);
        String yRay = yRayLetter + String.valueOf(yRayNum1) + yRayNum2;

        // find x-ray
        char xRayLetter = arboretum.charAt(times * 8 + 6);
        char xRayNum1 = arboretum.charAt(times * 8 + 7);
        char xRayNum2 = arboretum.charAt(times * 8 + 8);
        String xRay = xRayLetter + String.valueOf(xRayNum1) + xRayNum2;

        // find current place
        return yRay + xRay;
    }

    /**
     * Find all viable scoring paths for the given player and the given species if this player has the right to
     * score this species.
     *
     * A "scoring path" is a non-zero number of card substrings in order from starting card to ending card.
     * For example: "a1a3b6c7a8" is a path of length 5 starting at "a1" and ending at "a8".
     *
     * A path is viable if the following conditions are met:
     * 1. The player has the right to score the given species.
     * 2. Each card along the path is orthogonally adjacent to the previous card.
     * 3. Each card has value greater than the previous card.
     * 4. The path begins with the specified species.
     * 5. The path ends with the specified species.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @param player the given player
     * @param species the species the path must start and end with.
     * @return a set of all viable scoring paths if this player has the right to score this species, or null if this
     * player does not have the right to score this species. If the player has no viable scoring paths (but has the
     * right to score this species), return the empty Set.
     * TASK 12
     * @author Rita Zhou
     */
    public static Set<String> getAllViablePaths(String[][] gameState, char player, char species) {
        // find A and B arboretum
        String arboretumA = gameState[0][1];
        String arboretumB = gameState[0][3];
        // output: viablePath
        Set<String> viablePath = new HashSet<>();

        // find the possible cards
        ArrayList<String> possibleCards = possibleCards(species);

        // return null if invalid
        if (!canScore(gameState, player, species)){
            return null;
        }

        // find A's path
        if (player == 'A' && canScore(gameState, player, species)) {
            // create matrix
            String[][] matrix = findMatrix(arboretumA);
            // arraylist of possible path
            ArrayList<String> out = new ArrayList<>();
            // find the card is in the type of species
            for (String card : possibleCards){
                // find the card's place of matrix and the card is in the type of species
                for(int i=0;i<matrix.length;i++){
                    for(int j=0;j<matrix[i].length;j++){
                        if (matrix[i][j] != null && matrix[i][j].equals(card)){
                            // add the first card in the path
                            String path = matrix[i][j];
                            // find out the possible path
                            String used = "";
                            findPath(matrix, i, j, path, species, out, (arboretumA.length()-1)/8, used);
                            // add the path to the viablePath if we can find the path
                            for (String element : out){
                                if (element != null)
                                    viablePath.add(element);
                            }
                        }
                    }
                }
            }
        }

        // find B's arboretum
        if (player == 'B' && canScore(gameState, player, species)) {
            // create matrix
            String[][] matrix = findMatrix(arboretumB);
            // arraylist of possible path
            ArrayList<String> out = new ArrayList<>();
            // find the card is in the type of species
            for (String card : possibleCards){
                // find the card's place of matrix and the card is in the type of species
                for(int i=0;i<matrix.length;i++){
                    for(int j=0;j<matrix[i].length;j++){
                        if (matrix[i][j] != null && matrix[i][j].equals(card)){
                            // add the first card in the path
                            String path = matrix[i][j];
                            // find out the possible path
                            String used = "";
                            findPath(matrix, i, j, path, species, out, (arboretumB.length()-1)/8, used);
                            // add the path to the viablePath if we can find the path
                            for (String element : out){
                                if (element != null)
                                    viablePath.add(element);
                            }
                        }
                    }
                }
            }
        }

        return viablePath;
    }

    /**
     Find the valid Path in the matrix
     * @param matrix the shape of arboretum
     * @param row row of the searched card
     * @param column column of the searched card
     * @param path the path found
     * @param species species want to check
     * @param out ArrayList contains all possible path
     * @param length the length of cards
     * @param used the used cards
     * @author Rita Zhou
     */
    public static void findPath(String[][] matrix, int row, int column, String path, char species, ArrayList<String> out, int length, String used){
        if (used.length() == length * 2){
            return;
        }

        // card's value of searched card
        char num = matrix[row][column].charAt(1);
        // find if there is a card which is adjacent with searched card
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i != j && (i == 0 || j == 0)) {
                    if (i + row < matrix.length && i + row >= 0 && j + column < matrix.length && j + column >= 0 && matrix[row + i][column + j] != null) {
                        used = used + matrix[row+i][column+j];
                        // check if the adjacent card's value is greater than the searched card
                        if (matrix[row + i][column + j].charAt(1) > num) {
                            // if the adjacent card is the species we want, then add the path to the array list
                            if (matrix[row + i][column + j].charAt(0) == species) {
                                out.add(path + matrix[row + i][column + j]);
                            }
                            // keep finding the path
                            findPath(matrix, row + i, column + j, path + matrix[row + i][column + j], species, out, length, used);
                        }
                    }
                }
            }
        }
    }

    /**
     * Find the highest score of the viable paths for the given player and species.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @param player the given player
     * @param species the species to score
     * @return the score of the highest scoring viable path for this player and species.
     * If this player does not have the right to score this species, return -1.
     * If this player has the right to score this species but there is no viable scoring path, return 0.
     * TASK 13
     * @author Rita Zhou
     */
    public static int getHighestViablePathScore(String[][] gameState, char player, char species) {
        // output
        int out = 0;

        // if the player does not have the right to score
        if (!canScore(gameState, player, species))
            return -1;

        // find the viable path for the species
        Set<String> viablePath = getAllViablePaths(gameState, player, species);

        // no viable path then return 0
        if (canScore(gameState, player, species) && (viablePath == null || viablePath.size() == 0))
            return out;

            // if there is a viable path in the arboretum
        else if (viablePath != null){
            // find the path has the highest score
            for (String path : viablePath){
                int count = path.length()/2;
                int times = 0;
                // plus 1 score if the path start with 1
                if (path.charAt(1) == '1') {
                    count = count + 1;
                }
                // plus 2 score if the path is ended with 8
                if (path.charAt(path.length()-1) == '8') {
                    count = count + 2;
                }
                // if there are more than 4 cards in the path with the same species, each card add 1 score
                if (path.length() >= 8) {
                    for (int i = 0; i < path.length(); i = i + 2){
                        if (path.charAt(i) == species){
                            times ++;
                        }
                    }
                }
                if (times == path.length()/2){
                    count = count + times;
                }
                // find out the highest point
                if (count > out){
                    out = count;
                }
            }
        }
        return out;
    }


    /**
     * AI Part 1:
     * Decide whether to draw a card from the deck or a discard pile.
     * Note: This method only returns the choice, it does not update the game state.
     * If you wish to draw a card from the deck, return "D".
     * If you wish to draw a card from a discard pile, return the cardstring of the (top) card you wish to draw.
     * You may count the number of cards in a players' hand to determine whether this is their first or final draw
     * for the turn.
     *
     * Note: You may only draw the top card of a players discard pile. ie: The last card that was added to that pile.
     * Note: There must be cards in the deck (or alternatively discard) to draw from the deck (or discard) respectively.
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @return "D" if you wish to draw from the deck, or the cardstring of the card you wish to draw from a discard
     * pile.
     * TASK 14
     * @author Rita Zhou
     */
    public static String chooseDrawLocation(String[][] gameState) {
        // hashmap to represent each species of trees present times
        HashMap<String, Integer> presentTimes;
        // a set of String to represent the most species represent
        Set<String> biggestNumOfSpecies = new HashSet<>();
        // the possible card get from the discard A or discard B
        ArrayList<String> availableCards = new ArrayList<>();

        // find the most species present in A's arboretum
        if (gameState[0][0].equals("A") && gameState[1][1] != null && gameState[1][1].length() > 1){
            presentTimes = findPresentTimes(gameState[1][1], 2);
            int maxValueInMap=(Collections.max(presentTimes.values()));
            biggestNumOfSpecies.add(keyOfHighestValue(presentTimes, maxValueInMap));
        }

        // find the most species present in B's arboretum
        if (gameState[0][0].equals("B") && gameState[1][2] != null && gameState[1][2].length() > 1){
            presentTimes = findPresentTimes(gameState[1][2], 2);
            int maxValueInMap=(Collections.max(presentTimes.values()));
            biggestNumOfSpecies.add(keyOfHighestValue(presentTimes, maxValueInMap));
        }

        // get a card on the top of player A's discard
        if (gameState[0][2].length() > 1){
            String finalCard = gameState[0][2].substring(gameState[0][2].length()-2);
            availableCards.add(finalCard);
        }

        // get a card on the top of player B's discard
        if (gameState[0][4].length() > 1){
            String finalCard = gameState[0][4].substring(gameState[0][4].length()-2);
            availableCards.add(finalCard);
        }

        // if the card in available card pile is the species that represent the most, then return the card
        if (availableCards.size() > 0){
            for (String card : availableCards){
                for (String species : biggestNumOfSpecies){
                    if (String.valueOf(card.charAt(0)).equals(species))
                        return card;
                }
            }
        }

        // if there is at least one card in the deck, return "D"
        if (gameState[1][0].length() > 0)
            return "D";
        // if there is no card in the deck, then return any card of available card pile
        else{
            if (availableCards.size() > 0){
                int random = (int) (Math.random() * (availableCards.size()));
                return availableCards.get(random);
            }
            else
                return null;
        }
    }

    /**
     * Find each species of the list of cards present times
     * @param cards the list of cards
     * @param adds the index of the next card in the list of cards
     * @return hashmap of key for species and the value of present times
     * @author Rita Zhou
     */
    public static HashMap<String, Integer> findPresentTimes(String cards, int adds){
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 1; i < cards.length(); i=i+adds){
            if (hashMap.containsKey(String.valueOf(cards.charAt(i)))){
                hashMap.put(String.valueOf(cards.charAt(i)), hashMap.get(String.valueOf(cards.charAt(i)))+1);
            }
            else{
                hashMap.put(String.valueOf(cards.charAt(i)), 1);
            }
        }
        return hashMap;
    }

    /**
     * Return the String of key with the exact value in hashmap
     * @param hashMap the hashmap
     * @param compare the value wish to find
     * @return the key of that value in hashmap
     * @author Rita Zhou
     */
    public static String keyOfHighestValue(HashMap<String, Integer> hashMap, int compare){
        // find the species with the highest represent times
        // from https://www.codegrepper.com/code-examples/java/java+get+highest+value+in+a+hashmap
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==compare) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * AI Part 2:
     * Generate a moveString array for the player whose turn it is.
     *
     * A moveString array consists of two parts;
     * moveString[0] is the valid card _placement_ string for the card you wish to place.
     * moveString[1] is the cardstring of the card you wish to discard.
     *
     * For example: If I want to play card "a1" in location "N01E02" and discard card "b4" then my moveString[] would
     * be as follows:
     * moveString[0] = "a1N01E02"
     * moveString[1] = "b4"
     *
     * You may assume that the inputs to this method are valid and/or well-formed.
     *
     * @param gameState the game state array
     * @return a valid move for this player.
     * TASK 15
     * @author Rita Zhou
     */
    public static String[] generateMove(String[][] gameState) {
        // hashmap, key: species, value: the highest score of the path
        HashMap<String, Integer> putCardsAndFindValue = new HashMap<>();
        // current player
        String currentPlayer = gameState[0][0];
        // find the minimum num of the card
        int cardNum = 10;
        // output
        String[] output = new String[2];

        // find the player A
        if (currentPlayer.equals("A")){
            if (gameState[1][1].length() == 19){
                // find the most num of species represent in the arboretum
                HashMap<String, Integer> findPresent = findPresentTimes(gameState[1][1], 2);
                int maxValueInMap=(Collections.max(findPresent.values()));
                String randomCardSpecies = keyOfHighestValue(findPresent, maxValueInMap);
                // a new arboretum
                String newArboretum = gameState[0][1];
                // a new on hand card pile
                String newOnHand;

                // find each species and the highest value of the path for that species and put them in the hashmap
                for (int i = 1; i < gameState[1][1].length(); i=i+2){
                    char species = gameState[1][1].charAt(i);
                    String card = gameState[1][1].substring(i, i+2);
                    Set<String> findPlace = getAllValidPlacements(gameState, card);

                    if (String.valueOf(species).equals(randomCardSpecies)){
                        if (cardNum > gameState[1][1].charAt(i+1)-48)
                            cardNum = gameState[1][1].charAt(i+1)-48;
                    }

                    for (String cardAndPlace : findPlace){
                        Integer compareValue = getHighestViablePathScore(gameState, currentPlayer.charAt(0), species);
                        if (putCardsAndFindValue.containsKey(cardAndPlace)){
                            Integer currentValue = putCardsAndFindValue.get(cardAndPlace);
                            int highestValue = currentValue.compareTo(compareValue) > 0 ? currentValue : compareValue;
                            putCardsAndFindValue.put(cardAndPlace, highestValue);
                        }
                        else{
                            putCardsAndFindValue.put(cardAndPlace, compareValue);
                        }
                    }
                }

                // find the species with the highest path value
                int maxValue=(Collections.max(putCardsAndFindValue.values()));
                if (maxValue > 0){
                    String place = keyOfHighestValue(putCardsAndFindValue, maxValue);
                    output[0] = place;
                    newArboretum += place;
                    newOnHand = removeCard(gameState[1][1], place, 1);
                }
                // if there is no highest value, then put the random card in the arboretum
                else{
                    String cardName = randomCardSpecies + cardNum;
                    Set<String> places = getAllValidPlacements(gameState, cardName);
                    ArrayList<String> setToArray = new ArrayList<>(places);
                    int random = (int) (Math.random() * (setToArray.size()));
                    String place = setToArray.get(random);
                    output[0] = place;
                    newArboretum += place;
                    newOnHand = removeCard(gameState[1][1], place, 1);
                }

                // find each species and its present in the arboretum
                HashMap<String, Integer> presentTimesInArboretum = findPresentTimes(newArboretum, 8);
                int minValue=(Collections.min(presentTimesInArboretum.values()));
                // find the lowest number of species present in the arboretum
                String lowestNumSpeciesInArboretum = keyOfHighestValue(presentTimesInArboretum, minValue);

                Set<String> cardCanDisplay = new HashSet<>();

                // find each species and its present on hand
                HashMap<String, Integer> presentTimesNewOnHand = findPresentTimes(newOnHand, 2);
                int minValueInMap=(Collections.min(presentTimesNewOnHand.values()));
                // find the lowest number of species present on hand
                String lessNumPresentInOnHand = keyOfHighestValue(presentTimesNewOnHand, minValueInMap);

                // if the on hand species is not in the species of arboretum, then the species add the cardCanDisplay
                for (int i = 1; i < newOnHand.length(); i=i+2){
                    if (!presentTimesInArboretum.containsKey(String.valueOf(newOnHand.charAt(i)))){
                        cardCanDisplay.add(String.valueOf(newOnHand.charAt(i))+newOnHand.charAt(i+1));
                    }
                }

                if (cardCanDisplay.size() > 0){
                    // remove the species represent the lowest number appear in the arboretum
                    if (cardCanDisplay.contains(lowestNumSpeciesInArboretum)) {
                        for (int i = 1; i < newOnHand.length(); i = i + 2){
                            if (String.valueOf(newOnHand.charAt(i)).equals(lowestNumSpeciesInArboretum))
                                output[1] = String.valueOf(newOnHand.charAt(i)) + newOnHand.charAt(i+1);
                        }
                    }

                    // remove the species represent the lowest number appear on hand
                    else if (cardCanDisplay.contains(lessNumPresentInOnHand)){
                        for (int i = 1; i < newOnHand.length(); i = i + 2){
                            if (String.valueOf(newOnHand.charAt(i)).equals(lessNumPresentInOnHand))
                                output[1] = String.valueOf(newOnHand.charAt(i)) + newOnHand.charAt(i+1);
                        }
                    }

                    // otherwise, remove any card
                    else {
                        ArrayList<String> setToArray = new ArrayList<>(cardCanDisplay);
                        int random = (int) (Math.random() * (setToArray.size()));
                        String card = setToArray.get(random);
                        output[1] = card;
                    }
                }

                // otherwise, remove any card
                else {
                    int random = (int) (Math.random() * ((newOnHand.length()-1)/2));
                    output[1] = newOnHand.charAt(random * 2+1) + String.valueOf(newOnHand.charAt(random * 2 + 2));
                }
            }
        }


        // find the player B
        if (currentPlayer.equals("B")){
            if (gameState[1][2].length() == 19){
                // find the most num of species represent in the arboretum
                HashMap<String, Integer> findPresent = findPresentTimes(gameState[1][2], 2);
                int maxValueInMap=(Collections.max(findPresent.values()));
                String randomCardSpecies = keyOfHighestValue(findPresent, maxValueInMap);
                // a new arboretum
                String newArboretum = gameState[0][3];
                // a new on hand
                String newOnHand;

                // find each species and the highest value of the path for that species and put them in the hashmap
                for (int i = 1; i < gameState[1][2].length(); i=i+2){
                    char species = gameState[1][2].charAt(i);
                    String card = gameState[1][2].substring(i, i+2);
                    Set<String> findPlace = getAllValidPlacements(gameState, card);

                    if (String.valueOf(species).equals(randomCardSpecies)){
                        if (cardNum > gameState[1][2].charAt(i+1)-48)
                            cardNum = gameState[1][2].charAt(i+1)-48;
                    }

                    for (String cardAndPlace : findPlace){
                        Integer compareValue = getHighestViablePathScore(gameState, currentPlayer.charAt(0), species);
                        if (putCardsAndFindValue.containsKey(cardAndPlace)){
                            Integer currentValue = putCardsAndFindValue.get(cardAndPlace);
                            int highestValue = currentValue.compareTo(compareValue) > 0 ? currentValue : compareValue;
                            putCardsAndFindValue.put(cardAndPlace, highestValue);
                        }
                        else{
                            putCardsAndFindValue.put(cardAndPlace, compareValue);
                        }
                    }
                }

                // find the species with the highest path value
                int maxValue=(Collections.max(putCardsAndFindValue.values()));
                if (maxValue > 0){
                    String place = keyOfHighestValue(putCardsAndFindValue, maxValue);
                    output[0] = place;
                    newArboretum += place;
                    newOnHand = removeCard(gameState[1][2], place, 1);
                }
                // if there is no highest value, then put the random card in the arboretum
                else{
                    String cardName = randomCardSpecies + cardNum;
                    Set<String> places = getAllValidPlacements(gameState, cardName);
                    ArrayList<String> setToArray = new ArrayList<>(places);
                    int random = (int) (Math.random() * (setToArray.size()));
                    String place = setToArray.get(random);
                    output[0] = place;
                    newArboretum += place;
                    newOnHand = removeCard(gameState[1][2], place, 1);
                }

                // find each species and its present in the arboretum
                HashMap<String, Integer> presentTimesInArboretum = findPresentTimes(newArboretum, 8);
                int minValue=(Collections.min(presentTimesInArboretum.values()));
                // find the lowest number of species present in the arboretum
                String lowestNumSpeciesInArboretum = keyOfHighestValue(presentTimesInArboretum, minValue);

                Set<String> cardCanDisplay = new HashSet<>();

                // find each species and its present on hand
                HashMap<String, Integer> presentTimesNewOnHand = findPresentTimes(newOnHand, 2);
                int minValueInMap=(Collections.min(presentTimesNewOnHand.values()));
                // find the lowest number of species present on hand
                String lessNumPresentInOnHand = keyOfHighestValue(presentTimesNewOnHand, minValueInMap);

                // if the on hand species is not in the species of arboretum, then the species add the cardCanDisplay
                for (int i = 1; i < newOnHand.length(); i=i+2){
                    if (!presentTimesInArboretum.containsKey(String.valueOf(newOnHand.charAt(i)))){
                        cardCanDisplay.add(String.valueOf(newOnHand.charAt(i))+newOnHand.charAt(i+1));
                    }
                }

                if (cardCanDisplay.size() > 0){
                    // remove the species represent the lowest number appear in the arboretum
                    if (cardCanDisplay.contains(lowestNumSpeciesInArboretum)) {
                        for (int i = 1; i < newOnHand.length(); i = i + 2){
                            if (String.valueOf(newOnHand.charAt(i)).equals(lowestNumSpeciesInArboretum))
                                output[1] = String.valueOf(newOnHand.charAt(i)) + newOnHand.charAt(i+1);
                        }
                    }
                    // remove the species represent the lowest number appear on hand
                    else if (cardCanDisplay.contains(lessNumPresentInOnHand)){
                        for (int i = 1; i < newOnHand.length(); i = i + 2){
                            if (String.valueOf(newOnHand.charAt(i)).equals(lessNumPresentInOnHand))
                                output[1] = String.valueOf(newOnHand.charAt(i)) + newOnHand.charAt(i+1);
                        }
                    }
                    // otherwise, remove any card
                    else {
                        ArrayList<String> setToArray = new ArrayList<>(cardCanDisplay);
                        int random = (int) (Math.random() * (setToArray.size()));
                        String card = setToArray.get(random);
                        output[1] = card;
                    }
                }
                // otherwise, remove any card
                else {
                    int random = (int) (Math.random() * ((newOnHand.length()-1)/2));
                    output[1] = newOnHand.charAt(random * 2+1) + String.valueOf(newOnHand.charAt(random * 2 + 2));
                }
            }
        }
        return output;
    }

    /**
     * Input a string of lists of cards and a card that wish to remove, return a new String that
     * remove the card
     * @param listOfCards a String of lists of cards
     * @param card the card wish to remove
     * @param startAt the index
     * @return a new String that removed the card
     * @author Rita Zhou
     */
    public static String removeCard(String listOfCards, String card, int startAt){
        String newString = "";
        for (int i = startAt; i < listOfCards.length(); i = i + 2){
            if (listOfCards.charAt(i) == card.charAt(0) && listOfCards.charAt(i+1) == card.charAt(1)) {
                if (i + 2 >= listOfCards.length())
                    newString = listOfCards.substring(0, i);
                else
                    newString = listOfCards.substring(0, i) + listOfCards.substring(i + 2);
            }
        }
        return newString;
    }
}
