package frontend;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Display class for formatting tables
 *
 * @author Collin
 */
public class TableDisplay {
    private int rows;
    private int cols;
    private ArrayList<List<String>> data;
    private List<String> header;
    private boolean enableHeader;
    private boolean enableColumnSeparators;

    /**
     * Builder for producing table displays
     */
    public static class TableDisplayBuilder {
        private int rows;
        private int cols;
        private boolean enableHeader;
        private boolean enableColumnSeparators;

        public TableDisplayBuilder(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        /**
         * Display header on table display
         * @return
         *  Builder reference
         */
        public TableDisplayBuilder enableHeader() {
            enableHeader = true;
            return this;
        }

        /**
         * Display column separators on table display
         * @return
         *  Builder reference
         */
        public TableDisplayBuilder enableColumnSeparators() {
            enableColumnSeparators = true;
            return this;
        }

        /**
         * Build the table display
         * @return
         *  TableDisplay as configured
         */
        public TableDisplay build() {
            return new TableDisplay(rows, cols, enableHeader, enableColumnSeparators);
        }
    }

    /**
     * Obtain a table display builder
     * @param rows
     *  Rows in the table
     * @param cols
     *  Columns in the table
     * @return
     *  A builder
     */
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

    /**
     * Set the table header to the specified list
     * @param header
     *  The header list
     */
    public void setHeader(List<String> header) {
        this.header = header;
    }

    /**
     * Get the width of the entire table
     * @return
     *  Table width
     */
    private int width() {
        //return padding()*cols + cols + 1;
        return IntStream.range(0, cols)
                .map(this::columnPadding)
                .sum() + (enableColumnSeparators ? cols + 1 : 0);  // accounts for dividers
    }

    /**
     * Set a row in the table to the provided list
     * @param row
     *  row to set
     * @param rowData
     *  row data
     */
    public void setRow(int row, List<String> rowData) {
        data.set(row, rowData);
    }

    /**
     * Produce an empty row for the table
     * @return
     *  An empty row
     */
    private List<String> emptyRow() {
        return Stream.generate(() -> " ").limit(cols).collect(Collectors.toList());
    }

    /**
     * Produce a horizontal divider for the table
     * @return
     *  A string representing the divider
     */
    private String divider() {
        return "+" + Stream.generate(() -> "-").limit(width()-2).collect(Collectors.joining()) + "+";
    }

    private int padding() {

        // Merge all elements in the table into a single list, and find the longest string. This is our padding.
        return Stream.concat(data.stream().flatMap(Collection::stream), header.stream())
                .map(String::length)
                .reduce(Integer::max)
                .orElse(0) + 1;
    }

    /**
     * Calculate padding for a given column for formatting
     * @param col
     *  The column to calculate padding
     * @return
     *  The padding in characters
     */
    private int columnPadding(int col) {
        return Stream.concat(data.stream().map(row -> row.get(col)), header.stream())
                .map(String::length)
                .reduce(Integer::max)
                .orElse(0) + 1;
    }

    /**
     * Display the header
     */
    private void displayHeader() {
        System.out.println(divider());
        displayRow(header);
        System.out.println(divider());
    }

    /**
     * Display a row of data in the table
     * @param row
     *  The row to display
     */
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

    /**
     * Display all rows in the table
     */
    private void displayRows() {
        for (List<String> row : data) {
            displayRow(row);
        }
    }

    /**
     * Display the entire table
     */
    private void displayFullTable() {
        displayHeader();
        displayRows();
        System.out.println(divider());
    }

    /**
     * Handle display
     */
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
