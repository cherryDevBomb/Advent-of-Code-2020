package day_17;

import util.InputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConwayCubes {

    private static final String INPUT_FILE = "input_17.txt";

    private static final Character ACTIVE = '#';
    private static final Character INACTIVE = '.';

    public static void main(String[] args) {
        new ConwayCubes().simulate();
    }

    private void simulate() {
        List<String> slice = InputReader.readInputFile(INPUT_FILE);
        List<Cube> cubes3D = initCubes3D(slice);
        List<Cube> cubes4D = initCubes4D(slice);

        long active3D = playGame(cubes3D);
        long active4D = playGame(cubes4D);

        System.out.println("Number of active cubes after 6 3D cycles: " + active3D);
        System.out.println("Number of active cubes after 6 4D cycles: " + active4D);
    }

    private List<Cube> initCubes3D(List<String> slice) {
        return IntStream.range(0, slice.size()).boxed()
                .flatMap(lineNr -> IntStream.range(0, slice.get(lineNr).length()).boxed()
                        .map(colNr -> {
                            Coordinate coordinate = new Coordinate3D(colNr, lineNr, 0);
                            return slice.get(lineNr).charAt(colNr) == ACTIVE ? new Cube(coordinate, true) : new Cube(coordinate, false);
                        }))
                .collect(Collectors.toList());
    }

    private List<Cube> initCubes4D(List<String> slice) {
        return IntStream.range(0, slice.size()).boxed()
                .flatMap(lineNr -> IntStream.range(0, slice.get(lineNr).length()).boxed()
                        .map(colNr -> {
                            Coordinate coordinate = new Coordinate4D(colNr, lineNr, 0, 0);
                            return slice.get(lineNr).charAt(colNr) == ACTIVE ? new Cube(coordinate, true) : new Cube(coordinate, false);
                        }))
                .collect(Collectors.toList());
    }

    /**
     * Perform 6 cycles of the game
     *
     * @param initialState list of cubes representing initial state
     * @return number of active cubes after performing 6 cycles
     */
    private long playGame(List<Cube> initialState) {
        int cycle = 0;
        while (cycle++ < 6) {
            initialState = performCycle(initialState);
        }

        return initialState.stream()
                .filter(Cube::isActive)
                .count();
    }

    /**
     * During a cycle, all cubes simultaneously change their state according to the following rules:
     * If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active. Otherwise, the cube becomes inactive.
     * If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active. Otherwise, the cube remains inactive.
     *
     * @param cubes list of cubes before the cycle
     * @return list of cubes after the cycle
     */
    private List<Cube> performCycle(List<Cube> cubes) {
        List<Cube> additionalNeighbours = cubes.stream().flatMap(cube -> {
            List<? extends Coordinate> neighbourCoordinates = cube.getCoordinate().getNeighbourCoordinates();
            neighbourCoordinates.removeIf(c -> cubes.stream().anyMatch(existing -> existing.getCoordinate().equals(c)));
            return neighbourCoordinates.stream().map(c -> new Cube(c, false));
        }).distinct().collect(Collectors.toList());

        List<Cube> cubesCopy = new ArrayList<>(cubes);
        cubesCopy.addAll(additionalNeighbours);
        Map<Cube, Integer> cubeMap = cubesCopy.stream().collect(Collectors.toMap(cube -> cube, cube -> 0));

        cubes.forEach(cube -> {
            if (cube.isActive()) {
                List<? extends Coordinate> neighbourCoordinates = cube.getCoordinate().getNeighbourCoordinates();
                neighbourCoordinates.forEach(c -> {
                    Cube cubeOnCoordinate = lookupCube(cubeMap.keySet(), c);
                    cubeMap.put(cubeOnCoordinate, cubeMap.get(cubeOnCoordinate) + 1);
                });
            }
        });

        return cubeMap.entrySet().stream()
                .map(entry -> {
                    if (entry.getKey().isActive() && entry.getValue() != 2 && entry.getValue() != 3) {
                        entry.getKey().setActive(false);
                    } else if (!entry.getKey().isActive() && entry.getValue() == 3) {
                        entry.getKey().setActive(true);
                    }
                    return entry.getKey();
                })
                .collect(Collectors.toList());
    }

    private Cube lookupCube(Set<Cube> cubes, Coordinate coordinate) {
        return cubes.stream().filter(cube -> coordinate.equals(cube.getCoordinate())).findFirst().get();
    }
}