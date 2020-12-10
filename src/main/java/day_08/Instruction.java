package day_08;

public class Instruction {
    private Operation operation;
    private int argument;

    public Instruction(Operation operation, int argument) {
        this.operation = operation;
        this.argument = argument;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getArgument() {
        return argument;
    }
}
