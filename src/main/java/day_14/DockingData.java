package day_14;

import util.InputReader;

import java.util.Arrays;
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

        long finalSumInMemoryVersionOne = findFinalSum(instructions, bitmaskApplierVersionOne());
        long finalSumInMemoryVersionTwo = findFinalSum(instructions, bitmaskApplierVersionTwo());

        System.out.println("The sum of all values in memory after the program completes: " + finalSumInMemoryVersionOne);
        System.out.println("The sum of all values in memory after the program run with emulator version 2 completes: " + finalSumInMemoryVersionTwo);
    }

    /**
     * Given a list of instructions of two types, get the sum of all the values that are in memory after executing each instruction.
     * A "mask" (UpdateBitmaskInstruction) instruction updates the bitmask.
     * A "mem" (WriteToMemoryInstruction) instruction applies the bitmask on the value and writes it value to memory
     *
     * @param instructions   a list of instructions to execute
     * @param bitmaskApplier specifies how the bitmask should be used to update the memory values
     * @return sum of values left in in memory after executing the instructions
     */
    private long findFinalSum(List<Instruction> instructions, BitmaskApplier bitmaskApplier) {
        AtomicReference<String> mask = new AtomicReference<>();
        Map<Long, Long> memory = new HashMap<>();

        instructions.forEach(instruction -> {
            if (instruction instanceof UpdateBitmaskInstruction) {
                mask.set(((UpdateBitmaskInstruction) instruction).getMask());
            } else if (instruction instanceof WriteToMemoryInstruction) {
                WriteToMemoryInstruction writeToMemoryInstruction = (WriteToMemoryInstruction) instruction;
                bitmaskApplier.updateMemoryWithBitmask(memory, writeToMemoryInstruction, mask.get());
            }
        });

        return memory.values().stream()
                .mapToLong(value -> value)
                .sum();
    }

    /**
     * The bitmask used in the first version of the program is applied on the value, then written to the given memory address.
     *
     * @return a lambda expression that applies a bitmask on a given value and updates the memory with the new value.
     */
    private BitmaskApplier bitmaskApplierVersionOne() {
        return (memory, instruction, mask) -> {
            long valueWithMaskApplied = applyBitmaskVersionOne(instruction.getValue(), mask);
            memory.put(instruction.getAddress(), valueWithMaskApplied);
        };
    }

    /**
     * The bitmask used in the second version of the program is applied on the memory address, then the given value is written to all addresses obtained by applying the mask..
     *
     * @return a lambda expression that applies a bitmask on a given address and updates the memory for all resulting addresses with the given value.
     */
    private BitmaskApplier bitmaskApplierVersionTwo() {
        return (memory, instruction, mask) -> {
            List<Long> addressesWithMaskApplied = applyBitmaskVersionTwo(instruction.getAddress(), mask);
            addressesWithMaskApplied.forEach(address -> memory.put(address, instruction.getValue()));
        };
    }

    /**
     * Given a binary representation of a number and a mask of the same length, update the binary representation
     * by applying the bitmask to each bit with the following rules:
     * If the bit is 0 or 1, it overwrites the corresponding bit in the value
     * If the bit is X the bit in the value is unchanged.
     *
     * @param value number in binary representation
     * @param mask  String of 36 bits
     * @return decimal number of the binary representation resulted after applying the bitmask
     */
    private long applyBitmaskVersionOne(long value, String mask) {
        String bitValue = overwriteCharsFromMask(value, mask, 'X');
        return Long.parseLong(bitValue, 2);
    }

    /**
     * Given a binary representation of a number and a mask of the same length, update the binary representation
     * by applying the bitmask to each bit with the following rules:
     * If the bitmask bit is 0, the corresponding memory address bit is unchanged.
     * If the bitmask bit is 1, the corresponding memory address bit is overwritten with 1.
     * If the bitmask bit is X, the corresponding memory address bit is floating (can be both 0 and 1)
     *
     * @param value number in binary representation
     * @param mask  String of 36 bits
     * @return list of decimal numbers of the binary representation resulted after applying the bitmask by replacing appearances of X with all combinations of 0 and 1
     */
    private List<Long> applyBitmaskVersionTwo(long value, String mask) {
        String bitValue = overwriteCharsFromMask(value, mask, '0');
        List<String> values = Arrays.asList(bitValue);
        boolean hasLeftChanges = true;

        while (hasLeftChanges) {
            values = values.stream()
                    .map(val -> Arrays.asList(val.replaceFirst("X", "0"), val.replaceFirst("X", "1")))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            hasLeftChanges = values.stream().anyMatch(val -> val.contains("X"));
        }

        return values.stream()
                .mapToLong(val -> Long.parseLong(val, 2))
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Given a binary representation of a number and a mask of the same length, update the binary representation
     * by overwriting each bit in the number with the value from the mask, except if the value from the mask is the specified ignoreChar.
     *
     * @param value      number in binary representation
     * @param mask       String of 36 bits
     * @param ignoreChar for indices which are ignoreChar in the mask, the bit value in the number remains unchanged
     * @return binary representation resulted after applying the bitmask
     */
    private String overwriteCharsFromMask(long value, String mask, char ignoreChar) {
        String binaryValue = Long.toBinaryString(value);
        String initialBitValue = String.format("%36s", binaryValue).replace(' ', '0');
        StringBuilder bitValue = new StringBuilder(initialBitValue);

        IntStream.range(0, 36)
                .forEach(index -> {
                    if (mask.charAt(index) != ignoreChar) {
                        bitValue.setCharAt(index, mask.charAt(index));
                    }
                });
        return bitValue.toString();
    }
}
