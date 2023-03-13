package comp1110.ass2.gui;

import static comp1110.ass2.Arboretum.drawFromDeck;

/**
 * This class is to help me initial the game state
 * @author Rita Zhou
 */
public class InitialState {
    /**
     * using the drawFrom Deck to return an initial state of the game
     * @param state the state that only deck has the card, no cards for player A and player B
     * @return the state that player A and player B both have cards on hand
     * @author Rita Zhou
     */
    public static String[][] drawDeck(String[][] state){
        // using the recursion to find the initial state
        // base case : return the state
        if (state[1][1].length() == 15 && state[1][2].length() == 15)
            return state;

        // step case : not enough cards for A's on hand and B's on hand, so we need to keep rotating to find cards
        else{
            String card = drawFromDeck(state[1][0]);
            for (int i = 0; i < state[1][0].length(); i = i+2){
                if(card.charAt(0) == state[1][0].charAt(i) && card.charAt(1) == state[1][0].charAt(i+1)){
                    if (i + 2 >= state[1][0].length()) {
                        state[1][0] = state[1][0].substring(0, i);
                    }
                    else {
                        state[1][0] = state[1][0].substring(0, i) + state[1][0].substring(i + 2);
                    }
                    if (state[1][1].length() < 15)
                        state[1][1] += card;

                    else if (state[1][2].length() < 15)
                        state[1][2] += card;
                    return drawDeck(state);
                }
            }
        }
        return null;
    }



}
