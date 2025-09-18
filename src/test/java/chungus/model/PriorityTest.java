package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PriorityTest {

    @Test
    public void getSymbol_returnsCorrectSymbols() {
        assertEquals("H", Priority.HIGH.getSymbol());
        assertEquals("M", Priority.MEDIUM.getSymbol());
        assertEquals("L", Priority.LOW.getSymbol());
    }

    @Test
    public void parseOrDefault_withValidInputs_returnsCorrectPriority() {
        // Test single letter inputs
        assertEquals(Priority.HIGH, Priority.parseOrDefault("h"));
        assertEquals(Priority.HIGH, Priority.parseOrDefault("H"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("m"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("M"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("l"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("L"));
    }

    @Test
    public void parseOrDefault_withFullWords_returnsCorrectPriority() {
        // Test full word inputs
        assertEquals(Priority.HIGH, Priority.parseOrDefault("high"));
        assertEquals(Priority.HIGH, Priority.parseOrDefault("HIGH"));
        assertEquals(Priority.HIGH, Priority.parseOrDefault("High"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("medium"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("MEDIUM"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("Medium"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("low"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("LOW"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("Low"));
    }

    @Test
    public void parseOrDefault_withMedVariations_returnsMedium() {
        // Test "med" variation
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("med"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("MED"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("Med"));
    }

    @Test
    public void parseOrDefault_withWhitespace_returnsCorrectPriority() {
        // Test inputs with whitespace
        assertEquals(Priority.HIGH, Priority.parseOrDefault(" h "));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault(" m "));
        assertEquals(Priority.LOW, Priority.parseOrDefault(" l "));
        assertEquals(Priority.HIGH, Priority.parseOrDefault(" high "));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault(" medium "));
        assertEquals(Priority.LOW, Priority.parseOrDefault(" low "));
    }

    @Test
    public void parseOrDefault_withNullInput_returnsMedium() {
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault(null));
    }

    @Test
    public void parseOrDefault_withEmptyInput_returnsMedium() {
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault(""));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("   "));
    }

    @Test
    public void parseOrDefault_withUnknownInput_returnsMedium() {
        // Test unknown inputs that should default to MEDIUM
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("unknown"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("xyz"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("123"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault(""));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("invalid"));
    }

    @Test
    public void parseOrDefault_withCaseVariations_returnsCorrectPriority() {
        // Test various case combinations
        assertEquals(Priority.HIGH, Priority.parseOrDefault("HiGh"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("MeDiUm"));
        assertEquals(Priority.LOW, Priority.parseOrDefault("LoW"));
    }

    @Test
    public void parseOrDefault_withSpecialCharacters_returnsMedium() {
        // Test inputs with special characters
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("h!"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("high@"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("m#"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("l$"));
    }

    @Test
    public void parseOrDefault_withNumbers_returnsMedium() {
        // Test numeric inputs
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("1"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("2"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("3"));
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("0"));
    }

    @Test
    public void parseOrDefault_withPartialMatches_returnsMedium() {
        // Test partial matches that should not match
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("hi"));  // partial "high"
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("lo"));  // partial "low"
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("med")); // should match
        assertEquals(Priority.MEDIUM, Priority.parseOrDefault("me"));  // partial "medium"
    }
}
