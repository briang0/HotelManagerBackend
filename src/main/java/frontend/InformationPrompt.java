package frontend;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A text interface that prompts the user for (potentially) multiple
 * questions and records their answers.
 *
 * @author Collin
 */
public class InformationPrompt {
    private LinkedHashMap<String, String> prompts;

    /**
     * An InformationPrompt builder that handles configuration of an
     * information prompt.
     */
    public static class InformationPromptBuilder {
        private LinkedHashMap<String, String> prompts = new LinkedHashMap<>();

        private InformationPromptBuilder() {}

        /**
         * Add a new prompt to the information prompt, identified internally by the given key.
         * @param prompt
         *  The prompt that will be displayed to the user.
         * @param key
         *  The key representing this prompt, which can later be used to retrieve the answer to this prompt.
         * @return
         */
        public InformationPromptBuilder add(String prompt, String key) {
            prompts.put(key, prompt);
            return this;
        }

        /**
         * Build the information prompt configuation.
         * @return
         *  The configured information prompt.
         */
        public InformationPrompt build() {
            return new InformationPrompt(prompts);
        }
    }

    private InformationPrompt(LinkedHashMap<String, String> prompts) {
        this.prompts = prompts;
    }

    /**
     * Create a new information prompt builder.
     * @return
     *  The information prompt builder.
     */
    public static InformationPromptBuilder builder() {
        return new InformationPromptBuilder();
    }

    /**
     * Prompt the user with each question as configured, and
     * return the results.
     * @param scanner
     *  The system input
     * @return
     *  A map of the form {key: answer}, where key representing a prompt (as configured),
     *  and answer is the answer to that prompt.
     */
    public HashMap<String, String> prompt(Scanner scanner) {
        HashMap<String, String> answers = new HashMap<>();
        // Uniform padding here too? Maybe pad prompts to the right..? (So left pad)
        int padding = prompts.values().stream()
                .reduce((acc, item) -> acc.length() > item.length() ? acc : item)
                .get()
                .length();

        System.out.println("Please enter the following information:");
        for (String key : prompts.keySet()) {
            System.out.printf("%" + padding + "s: ", prompts.get(key));
            answers.put(key, scanner.nextLine());
        }

        return answers;
    }
}