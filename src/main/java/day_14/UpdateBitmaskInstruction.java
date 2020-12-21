package day_14;

public class UpdateBitmaskInstruction extends Instruction {

    private String mask;

    public UpdateBitmaskInstruction(String mask) {
        this.mask = mask;
    }

    public String getMask() {
        return mask;
    }
}
