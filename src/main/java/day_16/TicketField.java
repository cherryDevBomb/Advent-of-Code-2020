package day_16;

import org.apache.commons.lang.math.IntRange;

public class TicketField {

    private String name;
    private IntRange firstRange;
    private IntRange secondRange;

    public TicketField(String name, IntRange firstRange, IntRange secondRange) {
        this.name = name;
        this.firstRange = firstRange;
        this.secondRange = secondRange;
    }

    public String getName() {
        return name;
    }

    public IntRange getFirstRange() {
        return firstRange;
    }

    public IntRange getSecondRange() {
        return secondRange;
    }

    public boolean isValidValue(int val) {
        return firstRange.containsInteger(val) || secondRange.containsInteger(val);
    }
}
