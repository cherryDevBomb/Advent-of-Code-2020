package day_08;

public enum Operation {
    NOP, ACC, JMP;

    public static Operation getOperation(String operationCode) {
        return valueOf(operationCode.toUpperCase());
    }
}
