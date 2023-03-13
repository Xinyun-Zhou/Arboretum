package comp1110.ass2.help;

import static comp1110.ass2.help.IsArboretumValid.isArboretumWellFormed;

/**
 * This class is for drawing the matrix of Arboretum
 */
public class Matrix {
    /**
     * Input an arboretum state and return a String[][]
     * @param arboretum A String to represent the shape of arboretum
     * @return a String[][] to define each card's place
     * @author Rita Zhou
     */
    public static String[][] findMatrix(String arboretum){
        // create matrix
        String[][] matrix = new String[20][20];
        if (isArboretumWellFormed(arboretum)){
            for (int i = 0; i < (arboretum.length() - 1) / 8; i++) {
                // find card
                char cardName = arboretum.charAt(i * 8 + 1);
                char cardNum = arboretum.charAt(i * 8 + 2);
                String card = cardName + String.valueOf(cardNum);

                // find y-ray
                char yRayLetter = arboretum.charAt(i * 8 + 3);
                int yRayNum1 = arboretum.charAt(i * 8 + 4)  - 48;
                int yRayNum2 = arboretum.charAt(i * 8 + 5) - 48;
                int yRay = yRayNum1 * 10 + yRayNum2;

                // find x-ray
                char xRayLetter = arboretum.charAt(i * 8 + 6);
                int xRayNum1 = arboretum.charAt(i * 8 + 7) - 48;
                int xRayNum2 = arboretum.charAt(i * 8 + 8) - 48;
                int xRay = xRayNum1 * 10 + xRayNum2;

                // put the card in the matrix
                switch (yRayLetter){
                    case ('C') -> {
                        switch (xRayLetter){
                            case ('C') -> matrix[9][9] = card;
                            case ('W') -> matrix[9][9-xRay] = card;
                            case ('E') -> matrix[9][9+xRay] = card;
                        }
                    }
                    case ('S') -> {
                        switch (xRayLetter){
                            case ('C') -> matrix[9+yRay][9] = card;
                            case ('W') -> matrix[9+yRay][9-xRay] = card;
                            case ('E') -> matrix[9+yRay][9+xRay] = card;
                        }
                    }
                    case ('N') -> {
                        switch (xRayLetter){
                            case ('C') -> matrix[9-yRay][9] = card;
                            case ('W') -> matrix[9-yRay][9-xRay] = card;
                            case ('E') -> matrix[9-yRay][9+xRay] = card;
                        }
                    }
                }
            }
        }
        return matrix;
    }
}
