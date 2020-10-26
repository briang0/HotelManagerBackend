package frontend;

import java.util.LinkedHashMap;

public class HelpDisplay {
    private LinkedHashMap<String, String> commands;

    public static class HelpDisplayBuilder {
        private LinkedHashMap<String, String> commands = new LinkedHashMap<>();

        private HelpDisplayBuilder() {}

        public HelpDisplayBuilder add(String command, String description) {
            commands.put(command, description);
            return this;
        }

        public HelpDisplay build() {
            return new HelpDisplay(commands);
        }
    }

    public static HelpDisplayBuilder builder() {
        return new HelpDisplayBuilder();
    }

    private HelpDisplay(LinkedHashMap<String, String> commands) {
        this.commands = commands;
    }

    public void display() {
        System.out.println("Commands:");
        // Potential errors? (Since builder technically allows zero help items)
        int padding = commands.keySet().stream()
                .reduce((acc, item) -> acc.length() > item.length() ? acc : item)
                .get()
                .length();

        for (String command : commands.keySet()) {
            System.out.printf("%-" + padding + "s - %s\n", command, commands.get(command));
        }
    }
}
