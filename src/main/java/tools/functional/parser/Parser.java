package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface Parser<T> {
    Optional<Success<T>> parse(String input);

    record Success<T>(T matchedObject, String remainingInput) {}
}
