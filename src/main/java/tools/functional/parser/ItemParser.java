package tools.functional.parser;

import java.util.stream.Stream;

public class ItemParser {
    public static ItemParser of() {
        return new ItemParser();
    }

    private ItemParser() {}

    public Stream<Result> parse(Input input) {
        return input.isEmpty()
                ? Stream.empty()
                : Stream.of(new Result(input.head(), input.tail()));
    }
}
