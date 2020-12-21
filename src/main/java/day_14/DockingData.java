package day_14;

import util.InputReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DockingData {

    private static final String INPUT_FILE = "input_14.txt";

    public static void main(String[] args) {
        new DockingData().executeProgram();
    }

    private void executeProgram() {
        List<Instruction> instructions = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> line.split(" = "))
                .map(line -> {
                    if ("mask".equals(line[0])) {
                        return new UpdateBitmaskInstruction(line[1]);
                    } else {
                        int address = Integer.parseInt(line[0].substring(4, line[0].length() - 1));
                        return new WriteToMemoryInstruction(address, Long.parseLong(line[1]));
                    }
                })
                .collect(Collectors.toList());

        long finalSumInMemory = findFinalSum(instructions);
        
        System.out.println("The sum of all values in memory after the program completes: " + finalSumInMemory);
    }

    private long findFinalSum(List<Instruction> instructions) {
        AtomicReference<String> mask = new AtomicReference<>();
        Map<Integer, Long> memory = new HashMap<>();

        instructions.forEach(instruction -> {
            if (instruction instanceof UpdateBitmaskInstruction) {
                mask.set(((UpdateBitmaskInstruction) instruction).getMask());
            } else if (instruction instanceof WriteToMemoryInstruction) {
                WriteToMemoryInstruction writeToMemoryInstruction = (WriteToMemoryInstruction) instruction;
                long valueWithMaskApplied = applyBitmask(writeToMemoryInstruction.getValue(), mask.get());
                memory.put(writeToMemoryInstruction.getAddress(), valueWithMaskApplied);
            }
        });

        return memory.values().stream()
                .mapToLong(value -> value)
                .sum();
    }

    private long applyBitmask(long value, String mask) {
        String binaryValue = Long.toBinaryString(value);
        String initialBitValue = String.format("%36s", binaryValue).replace(' ', '0');
        StringBuilder bitValue = new StringBuilder(initialBitValue);

        IntStream.range(0, 36)
                .forEach(index -> {
                    if (mask.charAt(index) != 'X') {
                        bitValue.setCharAt(index, mask.charAt(index));
                    }
                });

        return Long.parseLong(bitValue.toString(), 2);
    }
}
