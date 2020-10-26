package frontend;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A text interface that presents a list of numbered
 * choices, and prompts the user for their selection.
 *
 * @author Collin
 */
public class ConsoleSelection {
    private LinkedList<String> selectionKey;
    private HashMap<String, String> selectionPrompt;

    /**
     * A ConsoleSelection builder that handles configuration of a
     * selection prompt.
     */
    public static class ConsoleSelectionBuilder {
        private ConsoleSelection selectionPrompt = new ConsoleSelection();

        /**
         * Add the given prompt as an option to the console selection prompt
         * @param prompt
         *  The prompt that will be displayed to the user as a choice
         * @param key
         *  A key that represents this prompt as a choice. This is the 'answer' returned by
         *  the prompt when the user makes a selection
         * @return
         *  The updated builder instance
         */
        public ConsoleSelectionBuilder add(String prompt, String key) {
            selectionPrompt.add(prompt, key);
            return this;
        }

        /**
         * Build the selection prompt configuration
         * @return
         *  The selection prompt with the configured prompts
         */
        public ConsoleSelection build() {
            return selectionPrompt;
        }
    }

    /**
     * Create a new builder instance
     * @return
     *  The selection prompt builder
     */
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

    /**
     * Prompt the user to make a selection from the configured list.
     * @param scanner
     *  The system input
     * @return
     *  The key (as configured) representing the selection of the user
     */
    public String select(Scanner scanner) {
        display();
        int selection = Integer.parseInt(scanner.nextLine());

        return selectionKey.get(selection);
    }
}
