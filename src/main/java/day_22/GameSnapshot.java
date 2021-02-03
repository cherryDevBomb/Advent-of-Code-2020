package day_22;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSnapshot {

    private List<Long> deck1;
    private List<Long> deck2;

    public GameSnapshot(List<Long> deck1, List<Long> deck2) {
        this.deck1 = new ArrayList<>(deck1);
        this.deck2 = new ArrayList<>(deck2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSnapshot that = (GameSnapshot) o;
        return deck1.equals(that.deck1) &&
                deck2.equals(that.deck2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deck1, deck2);
    }
}
