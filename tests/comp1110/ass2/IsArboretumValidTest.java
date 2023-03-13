package comp1110.ass2;


import org.junit.jupiter.api.Test;

import static comp1110.ass2.help.IsArboretumValid.isArboretumWellFormed;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsArboretumValidTest {

    private String errorPrefix(String arboretum) {
        return "Arboretum.isArboretumValid: " + arboretum + System.lineSeparator();
    }

    public void test(String arboretum, boolean expected) {
        String errorPrefix = errorPrefix(arboretum);
        boolean out = isArboretumWellFormed(arboretum);
        assertEquals(expected, out, errorPrefix);
    }

    private static final String[] validArboretum = new String[]{
            "A",
            "B",
            "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01",
            "Bj5C00C00j6S01C00j7S02C00j4C00W01m6C00E01m3C00W02j3N01W01",
            "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01",
            "Bj5C00C00j6S01C00j7S02C00j4C00W01m6C00E01m3C00W02j3N01W01",
            "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01c8N02W02c1S01C00b6N03E01m8N03W01m1S02E01",
            "Bj5C00C00j6S01C00j7S01W01j4C00W01m6C00E01m3C00W02j3N01W01d2N02W01d7S02C00b8S02E01b3N01C00d1N03W01d8S03C00",
            "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01c8N02W02c1S01C00b6N03E01m8N03W01m1S02E01a8N04E01c2N01W01",
            "Bj5C00C00j6S01C00j7S01W01j4C00W01m6C00E01m3C00W02j3N01W01d2N02W01d7S02C00b8S02E01b3N01C00d1N03W01d8S03C00b2N02C00",
            "Ab1C00C00a4N01C00c3C00E01c6N02C00m7N02W01m4N01E01a5N02E01a2S01E01c8N02W02c1S01C00b6N03E01m8N03W01m1S02E01a8N04E01c2N01W01",
            "Bj5C00C00j6S01C00j7S01W01j4C00W01m6C00E01m3C00W02j3N01W01d2N02W01d7S02C00b8S02E01b3N01C00d1N03W01d8S03C00b2N02C00j8S01E01"
    };

    private static final String[] invalidArboretum = new String[]{
            "Aa1",
            "Bb5C00",
            "Aa5C00W01",
            "Bb1E01W02",
            "Aa1C00C00b2C00W02",
            "Bb5C00C00b7N02C00",
            "Aa5C00C00c7S05E07",
            "Bb1C00C00c8N12W14",
            "Aa21C00C00",
            "Bb9C00C00",
            "Aa4C00C00b8C00C00",
            "Bb1C00C00b2C00C00",
            "Aa3C00C00b2C00E01d4C00E01",
            "Bb5C00C00b1S01C00b8S01C00",
    };

    @Test
    public void testValidArboretum(){
        for (String arboretum : validArboretum){
            test(arboretum, true);
        }
    }

    @Test
    public void testInvalidArboretum(){
        for (String arboretum : invalidArboretum){
            test(arboretum, false);
        }
    }
}
