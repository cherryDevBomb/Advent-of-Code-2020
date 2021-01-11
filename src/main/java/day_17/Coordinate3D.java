package day_17;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Coordinate3D implements Coordinate {

    private int x;
    private int y;
    private int z;

    public Coordinate3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public List<Coordinate3D> getNeighbourCoordinates() {
        List<Coordinate3D> neighbours = IntStream.rangeClosed(x - 1, x + 1).boxed()
                .flatMap(newX -> IntStream.rangeClosed(y - 1, y + 1).boxed()
                        .flatMap(newY -> IntStream.rangeClosed(z - 1, z + 1).boxed()
                                .map(newZ -> new Coordinate3D(newX, newY, newZ))))
                .collect(Collectors.toList());

        neighbours.removeIf(c -> c.x == this.x && c.y == this.y && c.z == this.z);
        return neighbours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate3D that = (Coordinate3D) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
