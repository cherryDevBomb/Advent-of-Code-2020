package day_14;

public class WriteToMemoryInstruction extends Instruction {

    private int address;
    private long value;

    public WriteToMemoryInstruction(int address, long value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public long getValue() {
        return value;
    }
}
