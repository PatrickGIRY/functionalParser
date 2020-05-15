package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Optional.empty()
                : Optional.of(new SuccessChar(input.charAt(0), input.substring(1)));
    }

    static ParserChar failure() {
        return input -> Optional.empty();
    }

    static ParserChar valueOf(char c) {
       return input -> Optional.of(new SuccessChar(c, input));
    }

    Optional<SuccessChar> parse(String input);

    record SuccessChar(char matchedChar, String remainingInput) {}
}
