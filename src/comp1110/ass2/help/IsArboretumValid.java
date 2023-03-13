package comp1110.ass2.help;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static comp1110.ass2.Arboretum.isTree;

/**
 * This class is to define the arboretum is valid or not
 * @author Rita Zhou
 */
public class IsArboretumValid {
    /**
     * Input a state of arboretum and check if this arboretum is good formed
     * @param arboretum a String to represent the state of arboretum
     * @return if this arboretum is good format, return true, return false if this arboretum is not a good formed.
     * @author Rita Zhou
     */
    public static boolean isArboretumWellFormed(String arboretum){
        int num = 0;

        // an arraylist of cards already placed.
        ArrayList<String> cardPlaced = new ArrayList<>();
        // a set for place of card can put.
        Set<String> cardWillPlace = new HashSet<>();

        // define the validity
        if (arboretum.charAt(0) == 'A' || arboretum.charAt(0) == 'B') {
            if ((arboretum.length() - 1) % 8 == 0)
                for (int i = 0; i < (arboretum.length() - 1) / 8; i++) {

                    // find y-ray
                    char yRayLetter = arboretum.charAt(i * 8 + 3);
                    char yRayNum1 = arboretum.charAt(i * 8 + 4);
                    char yRayNum2 = arboretum.charAt(i * 8 + 5);
                    String yRay = yRayLetter + String.valueOf(yRayNum1) + yRayNum2;

                    // find x-ray
                    char xRayLetter = arboretum.charAt(i * 8 + 6);
                    char xRayNum1 = arboretum.charAt(i * 8 + 7);
                    char xRayNum2 = arboretum.charAt(i * 8 + 8);
                    String xRay = xRayLetter + String.valueOf(xRayNum1) + xRayNum2;

                    // find current place
                    String place = yRay+xRay;

                    // for the first time, put the card and find initial adjacent place
                    if (i == 0){
                        if (isTree(arboretum.charAt(i + 1)))
                            if (arboretum.charAt(i + 2) >= '1' && arboretum.charAt(i + 2) <= '8')
                                if (yRayLetter == 'C')
                                    if (yRayNum1 >= '0' && yRayNum1 <= '9' && yRayNum2 >= '0' && yRayNum2 <= '9')
                                        if (xRayLetter == 'C')
                                            if (xRayNum1 >= '0' && xRayNum1 <= '9' && xRayNum2 >= '0' && xRayNum2 <= '9') {
                                                cardPlaced.add(place);
                                                cardWillPlace.add("C00E01");
                                                cardWillPlace.add("C00W01");
                                                cardWillPlace.add("S01C00");
                                                cardWillPlace.add("N01C00");
                                                num++;
                                            }
                    }

                    // return false if the card is not adjacent to the card we put before
                    else if (!cardWillPlace.contains(place))
                        return false;

                    // find the following cards
                    else if (isTree(arboretum.charAt(i * 8 + 1)))
                        if (arboretum.charAt(i * 8 + 2) >= '1' && arboretum.charAt(i * 8 + 2) <= '8')
                            if (yRayLetter == 'S' || yRayLetter == 'N' || yRayLetter == 'C')
                                if (yRayNum1 >= '0' && yRayNum1 <= '9' && yRayNum2 >= '0' && yRayNum2 <= '9')
                                    if (xRayLetter == 'E' || xRayLetter == 'W' || xRayLetter == 'C')
                                        if (xRayNum1 >= '0' && xRayNum1 <= '9' && xRayNum2 >= '0' && xRayNum2 <= '9'){
                                            cardPlaced.add(place);
                                            cardWillPlace.addAll(findAdjacent(place));
                                            cardPlaced.forEach(cardWillPlace::remove);
                                            num++;
                                        }
                }
        }
        // return if there is no arboretum (initial) or the number of valid form is equal to the length of state
        return arboretum.length() == 1 || (num == (arboretum.length() - 1) / 8 && (arboretum.length() - 1) % 8 == 0);
    }


    /**
     * Find adjacent place of an input String, return the adjacent place doesn't have a card
     * @param place a valid String of input state (e.g. a1C00C00, b4S03E05)
     * @return an arraylist of place can put
     * @author Rita Zhou
     */
    public static ArrayList<String> findAdjacent(String place){
        // output
        ArrayList<String> adjacent = new ArrayList<>();

        // find y-ray
        char yRayLetter = place.charAt(0);
        int yRayNum1 = place.charAt(1)-48;
        int yRayNum2 = place.charAt(2)-48;
        // current y-ray
        int yRayNum = yRayNum1*10 + yRayNum2;
        String yKeep = yRayNum1 + String.valueOf(yRayNum2);
        // current y-ray + 1
        int yPlus = yRayNum + 1;
        int yPlusFirst = findFirstNum(yPlus);
        int yPlusSecond = yPlus % 10;
        String yPlusInt = yPlusFirst + String.valueOf(yPlusSecond);
        // current y-ray -1
        int yMinus = yRayNum - 1;
        int yMinusFirst = findFirstNum(yMinus);
        int yMinusSecond = yMinus % 10;
        String yMinusInt = yMinusFirst + String.valueOf(yMinusSecond);

        // find x-ray
        char xRayLetter = place.charAt(3);
        int xRayNum1 = place.charAt(4)-48;
        int xRayNum2 = place.charAt(5)-48;
        // current x-ray
        int xRayNum = xRayNum1*10+xRayNum2;
        String xKeep = xRayNum1 + String.valueOf(xRayNum2);
        // current x-ray + 1
        int xPlus = xRayNum + 1;
        int xPlusFirst = findFirstNum(xPlus);
        int xPlusSecond = xPlus % 10;
        String xPlusInt = xPlusFirst + String.valueOf(xPlusSecond);
        // current x-ray - 1
        int xMinus = xRayNum - 1;
        int xMinusFirst = findFirstNum(xMinus);
        int xMinusSecond = xMinus % 10;
        String xMinusInt = xMinusFirst + String.valueOf(xMinusSecond);

        // if y-ray is not 1 and x-ray is not 1, find the adjacent place and add to the output arraylist
        if (yRayNum != 1 && xRayNum != 1){
            switch (yRayLetter){
                case ('C'):
                    switch (xRayLetter) {
                        case ('E') -> {
                            adjacent.add("C00E" + xPlusInt);
                            adjacent.add("C00E" + xMinusInt);
                            adjacent.add("N01E" + xKeep);
                            adjacent.add("S01E" + xKeep);
                        }
                        case ('W') -> {
                            adjacent.add("C00W" + xPlusInt);
                            adjacent.add("C00W" + xMinusInt);
                            adjacent.add("N01W" + xKeep);
                            adjacent.add("S01W" + xKeep);
                        }
                    }
                    break;
                case ('N'):
                    switch (xRayLetter) {
                        case ('C') -> {
                            adjacent.add("N" + yKeep + "E01");
                            adjacent.add("N" + yKeep + "W01");
                            adjacent.add("N" + yPlusInt + "C00");
                            adjacent.add("N" + yMinusInt + "C00");
                        }
                        case ('E') -> {
                            adjacent.add("N" + yKeep + "E" + xPlusInt);
                            adjacent.add("N" + yKeep + "E" + xMinusInt);
                            adjacent.add("N" + yPlusInt + "E" + xKeep);
                            adjacent.add("N" + yMinusInt + "E" + xKeep);
                        }
                        case ('W') -> {
                            adjacent.add("N" + yKeep + "W" + xPlusInt);
                            adjacent.add("N" + yKeep + "W" + xMinusInt);
                            adjacent.add("N" + yPlusInt + "W" + xKeep);
                            adjacent.add("N" + yMinusInt + "W" + xKeep);
                        }
                    }
                    break;
                case ('S'):
                    switch (xRayLetter) {
                        case ('C') -> {
                            adjacent.add("S" + yKeep + "E01");
                            adjacent.add("S" + yKeep + "W01");
                            adjacent.add("S" + yPlusInt + "C00");
                            adjacent.add("S" + yMinusInt + "C00");
                        }
                        case ('E') -> {
                            adjacent.add("S" + yKeep + "E" + xPlusInt);
                            adjacent.add("S" + yKeep + "E" + xMinusInt);
                            adjacent.add("S" + yPlusInt + "E" + xKeep);
                            adjacent.add("S" + yMinusInt + "E" + xKeep);
                        }
                        case ('W') -> {
                            adjacent.add("S" + yKeep + "W" + xPlusInt);
                            adjacent.add("S" + yKeep + "W" + xMinusInt);
                            adjacent.add("S" + yPlusInt + "W" + xKeep);
                            adjacent.add("S" + yMinusInt + "W" + xKeep);
                        }
                    }
                    break;
            }
        }

        // if y-ray is 1 but x-ray is not 1, find the adjacent place and add to the output arraylist
        if (yRayNum == 1 && xRayNum != 1){
            switch (yRayLetter) {
                case ('N'):
                    switch (xRayLetter) {
                        case ('C') -> {
                            adjacent.add("C00C00");
                            adjacent.add("N01E01");
                            adjacent.add("N01W01");
                            adjacent.add("N02C00");
                        }
                        case ('E') -> {
                            adjacent.add("N01E" + xPlusInt);
                            adjacent.add("N01E" + xMinusInt);
                            adjacent.add("N02E" + xKeep);
                            adjacent.add("C00E" + xKeep);
                        }
                        case ('W') -> {
                            adjacent.add("N01W" + xPlusInt);
                            adjacent.add("N01W" + xMinusInt);
                            adjacent.add("N02W" + xKeep);
                            adjacent.add("C00W" + xKeep);
                        }
                    }
                    break;
                case ('S'):
                    switch (xRayLetter) {
                        case ('C') -> {
                            adjacent.add("C00C00");
                            adjacent.add("S01E01");
                            adjacent.add("S01W01");
                            adjacent.add("S02C00");
                        }
                        case ('E') -> {
                            adjacent.add("S01E" + xPlusInt);
                            adjacent.add("S01E" + xMinusInt);
                            adjacent.add("S02E" + xKeep);
                            adjacent.add("C00E" + xKeep);
                        }
                        case ('W') -> {
                            adjacent.add("S01W" + xPlusInt);
                            adjacent.add("S01W" + xMinusInt);
                            adjacent.add("S02W" + xKeep);
                            adjacent.add("C00W" + xKeep);
                        }
                    }
                    break;
            }
        }

        // if x-ray is 1 and y-ray is not 1, find the adjacent place and add to the output arraylist
        if (xRayNum == 1 && yRayNum != 1){
            switch (xRayLetter) {
                case ('E'):
                    switch (yRayLetter) {
                        case ('C') -> {
                            adjacent.add("C00C00");
                            adjacent.add("N01E01");
                            adjacent.add("S01E01");
                            adjacent.add("C00E02");
                        }
                        case ('S') -> {
                            adjacent.add("S" + yPlusInt + "E01");
                            adjacent.add("S" + yMinusInt + "E01");
                            adjacent.add("S" + yKeep + "E02");
                            adjacent.add("S" + yKeep + "C00");
                        }
                        case ('N') -> {
                            adjacent.add("N" + yPlusInt + "E01");
                            adjacent.add("N" + yMinusInt + "E01");
                            adjacent.add("N" + yKeep + "E02");
                            adjacent.add("N" + yKeep + "C00");
                        }
                    }
                    break;
                case ('W'):
                    switch (yRayLetter) {
                        case ('C') -> {
                            adjacent.add("C00C00");
                            adjacent.add("N01W01");
                            adjacent.add("S01W01");
                            adjacent.add("C00W02");
                        }
                        case ('S') -> {
                            adjacent.add("S" + yPlusInt + "W01");
                            adjacent.add("S" + yMinusInt + "W01");
                            adjacent.add("S" + yKeep + "W02");
                            adjacent.add("S" + yKeep + "C00");
                        }
                        case ('N') -> {
                            adjacent.add("N" + yPlusInt + "W01");
                            adjacent.add("N" + yMinusInt + "W01");
                            adjacent.add("N" + yKeep + "W02");
                            adjacent.add("N" + yKeep + "C00");
                        }
                    }
                    break;
            }
        }

        // find y-ray and x-ray both are 1, find the adjacent place and add to the output arraylist
        if (yRayNum == 1 && xRayNum == 1){
            switch (yRayLetter) {
                case ('N'):
                    switch (xRayLetter) {
                        case ('E') -> {
                            adjacent.add("N01E02");
                            adjacent.add("N01C00");
                            adjacent.add("N02E01");
                            adjacent.add("C00E01");
                        }
                        case ('W') -> {
                            adjacent.add("N01W02");
                            adjacent.add("N01C00");
                            adjacent.add("N02W01");
                            adjacent.add("C00W01");
                        }
                    }
                    break;
                case ('S'):
                    switch (xRayLetter) {
                        case ('E') -> {
                            adjacent.add("S01E02");
                            adjacent.add("S01C00");
                            adjacent.add("S02E01");
                            adjacent.add("C00E01");
                        }
                        case ('W') -> {
                            adjacent.add("S01W02");
                            adjacent.add("S01C00");
                            adjacent.add("S02W01");
                            adjacent.add("C00W01");
                        }
                    }
                    break;
            }
        }

        return adjacent;
    }


    /**
     * Find the first number of a two-digit number
     * @param num input a two-digit number
     * @return return it's first number, if it is only one-digit, return 0.
     * @author Rita Zhou
     */
    public static int findFirstNum(int num){
        // output, the first number in two-digit number
        int number = 0;
        if (num > 9)
            number = num / 10;

        return number;
    }
}
