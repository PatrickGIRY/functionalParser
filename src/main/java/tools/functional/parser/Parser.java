package tools.functional.parser;

import java.util.Optional;

@FunctionalInterface
public interface Parser {
    Optional<Success> parse(String input);

    record Success(Tree tree, String remainingInput) {}
}
