package day_05;

public class Seat {
    private int row;
    private int column;
    private int id;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.id = row * 8 + column;
    }

    public int getId() {
        return id;
    }
}
