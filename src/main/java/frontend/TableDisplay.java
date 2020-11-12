package frontend;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TableDisplay {
    private int rows;
    private int cols;
    private ArrayList<List<String>> data;
    private List<String> header;
    private boolean enableHeader;
    private boolean enableColumnSeparators;

    public static class TableDisplayBuilder {
        private int rows;
        private int cols;
        private boolean enableHeader;
        private boolean enableColumnSeparators;

        public TableDisplayBuilder(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public TableDisplayBuilder enableHeader() {
            enableHeader = true;
            return this;
        }

        public TableDisplayBuilder enableColumnSeparators() {
            enableColumnSeparators = true;
            return this;
        }

        public TableDisplay build() {
            return new TableDisplay(rows, cols, enableHeader, enableColumnSeparators);
        }
    }

    public static TableDisplayBuilder builder(int rows, int cols) {
        return new TableDisplayBuilder(rows, cols);
    }

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

    private TableDisplay(int rows, int cols, boolean enableHeader, boolean enableColumnSeparators) {
        this(rows, cols);
        this.enableHeader = enableHeader;
        this.enableColumnSeparators = enableColumnSeparators;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    // width of table based off padding
    private int width() {
        //return padding()*cols + cols + 1;
        return IntStream.range(0, cols)
                .map(this::columnPadding)
                .sum() + (enableColumnSeparators ? cols + 1 : 0);  // accounts for dividers
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

    private int columnPadding(int col) {
        return Stream.concat(data.stream().map(row -> row.get(col)), header.stream())
                .map(String::length)
                .reduce(Integer::max)
                .orElse(0) + 1;
    }

    private void displayHeader() {
        System.out.println(divider());
        displayRow(header);
        System.out.println(divider());
    }

    // Spacing should actually be calculated on a per column basis, rather than entirety of table
    private void displayRow(List<String> row) {
        if (enableColumnSeparators) System.out.print("|");
        /*
        for (String item : row) {
            System.out.printf("%-" + padding() + "s|", item);
        }
         */
        for (int i = 0; i < row.size(); i++) {
            System.out.printf("%-" + columnPadding(i) + "s%s", row.get(i), (enableColumnSeparators) ? "|" : "");
        }
        System.out.println("");
    }

    private void displayRows() {
        for (List<String> row : data) {
            displayRow(row);
        }
    }

    private void displayFullTable() {
        displayHeader();
        displayRows();
        System.out.println(divider());
    }

    public void display() {
        if (enableHeader) displayFullTable();
        else displayRows();
    }

    public static void main(String[] args) {
        TableDisplay table = new TableDisplay(1, 7);

        table.setHeader(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        table.setRow(0, List.of("4.0", "3.9", "4.2", "4.3", "4.1", "3.8", "3.9"));

        table.display();
    }

}
