package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Optional.empty()
                : Result.success(input.charAt(0), input.substring(1));
    }

    static ParserChar failure() {
        return input -> Optional.empty();
    }

    static ParserChar valueOf(char c) {
       return input -> Result.success(c, input);
    }

    Optional<Result.Success> parse(String input);

    interface Result {
        static Optional<Success> success(char matchedChar, String input) {
            return Optional.of(new Success(matchedChar, input));
        }

        record Success(char matchedChar, String remainingInput) implements Result {}
    }
}
