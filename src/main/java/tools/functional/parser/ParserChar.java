package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Result.failure()
                : Result.success(input.charAt(0), input.substring(1));
    }

    static ParserChar failure() {
        return input -> Result.failure();
    }

    static ParserChar valueOf(char c) {
       return input -> Result.success(c, input);
    }

    Optional<Success> parse(String input);

    interface Result {
        private static Optional<Success> success(char matchedChar, String input) {
            return Optional.of(new Success(matchedChar, input));
        }

        private static Optional<Success> failure() {
            return Optional.empty();
        }

    }
    record Success(char matchedChar, String remainingInput) {}
}
