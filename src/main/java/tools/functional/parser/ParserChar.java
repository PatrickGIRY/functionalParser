package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Optional.empty()
                : Optional.of(new SuccessChar(input.charAt(0), input.substring(1)));
    }

    Optional<SuccessChar> parse(String input);

    record SuccessChar(char matchedChar, String remainingInput) {}
}
