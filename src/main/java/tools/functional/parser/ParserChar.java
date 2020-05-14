package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface ParserChar {
    Optional<SuccessChar> parse(String input);

    record SuccessChar(char matchedChar, String remainingInput) {}
}
