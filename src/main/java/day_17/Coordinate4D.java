package day_17;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Coordinate4D implements Coordinate {

    private int x;
    private int y;
    private int z;
    private int w;

    public Coordinate4D(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public List<Coordinate4D> getNeighbourCoordinates() {
        List<Coordinate4D> neighbours = IntStream.rangeClosed(x - 1, x + 1).boxed()
                .flatMap(newX -> IntStream.rangeClosed(y - 1, y + 1).boxed()
                        .flatMap(newY -> IntStream.rangeClosed(z - 1, z + 1).boxed()
                                .flatMap(newZ -> IntStream.rangeClosed(w - 1, w + 1).boxed()
                                        .map(newW -> new Coordinate4D(newX, newY, newZ, newW)))))
                .collect(Collectors.toList());

        neighbours.removeIf(c -> c.x == this.x && c.y == this.y && c.z == this.z && c.w == this.w);
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
        Coordinate4D that = (Coordinate4D) o;
        return x == that.x &&
                y == that.y &&
                z == that.z &&
                w == that.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
