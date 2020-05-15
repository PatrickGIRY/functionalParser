package tools.functional.parser;

import java.util.Optional;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Optional.empty()
                : Optional.of(new Success(input.charAt(0), input.substring(1)));
    }

    static ParserChar failure() {
        return input -> Optional.empty();
    }

    static ParserChar valueOf(char c) {
       return input -> Optional.of(new Success(c, input));
    }

    Optional<Success> parse(String input);

    default ParserChar map(IntUnaryOperator mapper) {
        return input -> parse(input).map(success -> new Success((char)mapper.applyAsInt(success.matchedChar), success.remainingInput));
    }

    record Success(char matchedChar, String remainingInput) {}
}
