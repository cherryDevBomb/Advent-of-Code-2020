package day_05;

public class Range {
    private int min;
    private int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void getLowerHalf() {
        this.max = min + (max - min) / 2;
    }

    public void getUpperHalf() {
        this.min = min + (max - min) / 2 + 1;
    }

    public int getExactValue() {
        if (min == max) {
            return min;
        } else {
            throw new IllegalArgumentException("Cannot get exact value: Range min != max");
        }
    }
}
