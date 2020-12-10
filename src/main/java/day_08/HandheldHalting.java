package day_08;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import util.InputReader;

public class HandheldHalting {

    private static final String INPUT_FILE = "input_08.txt";

    public static void main(String[] args) {
        new HandheldHalting().runBootCode();
    }

    private void runBootCode() {
        List<Instruction> instructions = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> line.strip().split(" "))
                .map(line -> new Instruction(Operation.getOperation(line[0]), Integer.parseInt(line[1])))
                .collect(Collectors.toList());

        long accValue = calculateAccumulatorValue(instructions, getInstructionsToExecute(instructions));

        List<Instruction> correctFlow = getAllPossibleFlows(instructions).stream()
                .filter(flow -> getInstructionsToExecute(flow).get(getInstructionsToExecute(flow).size() - 1) == flow.size() - 1)
                .findAny()
                .get();

        long correctAccValue = calculateAccumulatorValue(correctFlow, getInstructionsToExecute(correctFlow));

        System.out.println("Immediately before any instruction is executed a second time, the value in the accumulator is: " + accValue);
        System.out.println("The value in the accumulator after executing the correct flow is: " + correctAccValue);
    }

    /**
     * Get the list of instruction indices that represent the order in which instructions will be executed, until the program terminates or a loop is found.
     *
     * @param instructions boot code that contains the whole list of instructions
     * @return indices of instructions to execute in that exact order
     */
    private List<Integer> getInstructionsToExecute(List<Instruction> instructions) {
        List<Integer> instructionsToExecute = new ArrayList<>();
        int index = 0;
        while (!instructionsToExecute.contains(index) && index < instructions.size()) {
            instructionsToExecute.add(index);
            index += instructions.get(index).getOperation() == Operation.JMP ? instructions.get(index).getArgument() : 1;
        }
        return instructionsToExecute;
    }

    /**
     * Calculate the value of the accumulator after executing a sequence of instructions from a given list.
     * Assume accumulator starts at 0.
     *
     * @param instructions          boot code that contains the whole list of instructions
     * @param instructionsToExecute indices of instructions to execute
     * @return final value contained in the accumulator
     */
    private long calculateAccumulatorValue(List<Instruction> instructions, List<Integer> instructionsToExecute) {
        return instructionsToExecute.stream()
                .map(instructions::get)
                .filter(instruction -> instruction.getOperation() == Operation.ACC)
                .mapToLong(Instruction::getArgument)
                .sum();
    }

    /**
     * Generate all possible instruction sequences based on the given sequence, by changing exactly one instruction in the whole code:
     * Either change a JMP operation to NOP, or a NOP operation to JMP
     *
     * @param instructions initial list of instructions
     * @return all possible instruction sequences that result from applying the described change to the initial sequence
     */
    private List<List<Instruction>> getAllPossibleFlows(List<Instruction> instructions) {
        return instructions.stream()
                .filter(instruction -> instruction.getOperation() != Operation.ACC)
                .map(instruction -> {
                    List<Instruction> newFlow = new ArrayList<>(instructions);
                    Operation replacedOperation = instruction.getOperation() == Operation.JMP ? Operation.NOP : Operation.JMP;
                    Instruction instructionReplacement = new Instruction(replacedOperation, instruction.getArgument());
                    newFlow.set(instructions.indexOf(instruction), instructionReplacement);
                    return newFlow;
                })
                .collect(Collectors.toList());
    }
}
