package frontend;

import java.util.LinkedHashMap;

/**
 * A text interface for displaying help for a given system.
 *
 * @author Collin
 */
public class HelpDisplay {
    private LinkedHashMap<String, String> commands;
    private String delimiter;

    /**
     * A builder for configuring a HelpDisplay
     */
    public static class HelpDisplayBuilder {
        private LinkedHashMap<String, String> commands = new LinkedHashMap<>();
        private String delimiter = "-";

        private HelpDisplayBuilder() {}

        /**
         * Add a command to the help display with a description.
         * @param command
         *  The name of the command.
         * @param description
         *  The description of what the commands does.
         * @return
         *  The builder.
         */
        public HelpDisplayBuilder add(String command, String description) {
            commands.put(command, description);
            return this;
        }

        public HelpDisplayBuilder setDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        /**
         * Build the configured help display.
         * @return
         *  The configured help display.
         */
        public HelpDisplay build() {
            return new HelpDisplay(commands, delimiter);
        }
    }

    /**
     * Create a HelpDisplayBuilder instance.
     * @return
     *  The builder.
     */
    public static HelpDisplayBuilder builder() {
        return new HelpDisplayBuilder();
    }

    private HelpDisplay(LinkedHashMap<String, String> commands, String delimiter) {
        this.commands = commands;
        this.delimiter = delimiter;
    }

    /**
     * Display the help.
     */
    public void display() {
        // Potential errors? (Since builder technically allows zero help items)
        int padding = commands.keySet().stream()
                .reduce((acc, item) -> acc.length() > item.length() ? acc : item)
                .get()
                .length();

        for (String command : commands.keySet()) {
            System.out.printf("%-" + padding + "s %s %s\n", command, delimiter, commands.get(command));
        }
    }
}
