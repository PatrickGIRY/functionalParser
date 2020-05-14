package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface Parser<T> {
    static ParserChar item() {
        return input -> input.isEmpty()
                ? Optional.empty()
                : Optional.of(new ParserChar.SuccessChar(input.charAt(0), input.substring(1)));
    }

    Optional<Success<T>> parse(String input);

    record Success<T>(T matchedObject, String remainingInput) {}
}
