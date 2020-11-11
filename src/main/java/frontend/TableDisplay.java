package frontend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableDisplay {
    private int rows;
    private int cols;
    private ArrayList<List<String>> data;
    private List<String> header;

    public TableDisplay(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        // Produce a table consisting of ROW empty array lists.
        data = Stream.generate(LinkedList<String>::new).limit(rows).collect(Collectors.toCollection(ArrayList::new));

        // Set each row list to contain COL elements (default: a single space " ")
        for (int row = 0; row < rows; row++) {
            data.set(row, emptyRow());
        }

        header = emptyRow();
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    // width of table based off padding
    private int width() {
        return padding()*cols + cols + 1;
    }

    // Try to enforce, len(rowData) should equal rows
    public void setRow(int row, List<String> rowData) {
        data.set(row, rowData);
    }

    private List<String> emptyRow() {
        return Stream.generate(() -> " ").limit(cols).collect(Collectors.toList());
    }

    private String divider() {
        return "+" + Stream.generate(() -> "-").limit(width()-2).collect(Collectors.joining()) + "+";
    }

    // padding of the table based off widest element
    private int padding() {

        // Merge all elements in the table into a single list, and find the longest string. This is our padding.
        return Stream.concat(data.stream().flatMap(Collection::stream), header.stream())
                .map(String::length)
                .reduce(Integer::max)
                .orElse(0) + 1;
    }

    private void displayHeader() {
        System.out.println(divider());
        displayRow(header);
        System.out.println(divider());
    }

    private void displayRow(List<String> row) {
        System.out.print("|");
        for (String item : row) {
            System.out.printf("%-" + padding() + "s|", item);
        }
        System.out.println("");
    }

    public void display() {
        displayHeader();
        for (List<String> row : data) {
            displayRow(row);
        }
        System.out.println(divider());
    }

    public static void main(String[] args) {
        TableDisplay table = new TableDisplay(1, 7);

        table.setHeader(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        table.setRow(0, List.of("4.0", "3.9", "4.2", "4.3", "4.1", "3.8", "3.9"));

        table.display();
    }

}
