package day_12;

public class Instruction {

    private String action;
    private int value;

    public Instruction(String action, int value) {
        this.action = action;
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }
}
