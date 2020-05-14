package tools.functional.parser;

@FunctionalInterface
public interface Parser {
    Success parse(String input);

    record Success(Tree tree, String remainingInput) {}
}
