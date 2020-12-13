package day_12;

import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RainRisk {

    private static final String INPUT_FILE = "input_12.txt";

    private static final String N = "N";
    private static final String S = "S";
    private static final String E = "E";
    private static final String W = "W";
    private static final String L = "L";
    private static final String R = "R";
    private static final String F = "F";

    private static final List<String> directions = Arrays.asList(E, S, W, N);

    public static void main(String[] args) {
        new RainRisk().navigate();
    }

    private void navigate() {
        List<Instruction> instructions = InputReader.readInputFile(INPUT_FILE).stream()
                .map(i -> new Instruction(i.substring(0, 1), Integer.parseInt(i.substring(1))))
                .collect(Collectors.toList());

        Map<String, Integer> distances = getTraveledDistances(instructions);
        int manhattanDistance = Math.abs(distances.get(N) - distances.get(S)) + Math.abs(distances.get(E) - distances.get(W));


        System.out.println("Manhattan distance between the ship's starting position and end location: " + manhattanDistance);
    }

    private Map<String, Integer> getTraveledDistances(List<Instruction> instructions) {
        AtomicReference<String> facingDirection = new AtomicReference<>(E);

        Map<String, Integer> distances = instructions.stream()
                .map(instruction -> {
                    if (F.equals(instruction.getAction())) {
                        return new Instruction(facingDirection.get(), instruction.getValue());
                    } else if (L.equals(instruction.getAction()) || R.equals(instruction.getAction())) {
                        facingDirection.set(getNewFacingDirection(facingDirection.get(), instruction));
                    }
                    return instruction;
                })
                .filter(instruction -> !L.equals(instruction.getAction()) && !R.equals(instruction.getAction()))
                .collect(Collectors.groupingBy(Instruction::getAction, Collectors.summingInt(Instruction::getValue)));

        directions.forEach(dir -> {
            if (!distances.containsKey(dir)) {
                distances.put(dir, 0);
            }
        });

        return distances;
    }

    /**
     * Calculates the direction the ship will be facing after turning left or right with a specified number of degrees.
     *
     * @param currentDirection the direction which the ship faces at the start
     * @param instruction      specifies the direction of the turn and the number of degrees
     * @return new facing direction
     */
    private String getNewFacingDirection(String currentDirection, Instruction instruction) {
        int currentIndex = directions.indexOf(currentDirection);
        int nrOf90DegTurns = instruction.getValue() / 90;

        int newDirIndex = L.equals(instruction.getAction()) ? currentIndex - nrOf90DegTurns : currentIndex + nrOf90DegTurns;
        if (newDirIndex < 0) {
            newDirIndex = directions.size() - Math.abs(newDirIndex);
        }
        newDirIndex = newDirIndex % directions.size();

        return directions.get(newDirIndex);
    }
}
