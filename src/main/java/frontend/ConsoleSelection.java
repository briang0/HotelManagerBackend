package frontend;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ConsoleSelection {
    private LinkedList<String> selectionKey;
    private HashMap<String, String> selectionPrompt;

    public static class ConsoleSelectionBuilder {
        private ConsoleSelection selectionPrompt = new ConsoleSelection();

        public ConsoleSelectionBuilder add(String prompt, String key) {
            selectionPrompt.add(prompt, key);
            return this;
        }

        public ConsoleSelection build() {
            return selectionPrompt;
        }
    }

    public static ConsoleSelectionBuilder builder() {
        return new ConsoleSelectionBuilder();
    }

    private ConsoleSelection() {
        selectionKey = new LinkedList<>();
        selectionPrompt = new HashMap<>();
    }

    private void add(String prompt, String key) {
        selectionPrompt.put(key, prompt);
        selectionKey.add(key);
    }

    private void display() {
        System.out.println("Please select an option.");
        for (int i = 0; i < selectionKey.size(); i++) {
            String key = selectionKey.get(i);
            System.out.printf("%d. %s\n", i, selectionPrompt.get(key));
        }
        System.out.print("> ");
    }

    public String select(Scanner scanner) {
        display();
        int selection = Integer.valueOf(scanner.nextLine());

        return selectionKey.get(selection);
    }
}
