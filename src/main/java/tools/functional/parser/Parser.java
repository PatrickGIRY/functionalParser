package tools.functional.parser;

@FunctionalInterface
public interface Parser {
    Tree parse(String input);
}
