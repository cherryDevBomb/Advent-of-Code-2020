package day_14;

public class WriteToMemoryInstruction extends Instruction {

    private long address;
    private long value;

    public WriteToMemoryInstruction(long address, long value) {
        this.address = address;
        this.value = value;
    }

    public long getAddress() {
        return address;
    }

    public long getValue() {
        return value;
    }
}
