package tools.functional.parser;

import java.util.function.Function;
import java.util.stream.Stream;

public class ItemParser {
    private final Function<Input, Stream<Result>> parser;

    public static ItemParser of() {
        return new ItemParser(input -> input.isEmpty()
                ? Stream.empty()
                : Stream.of(new Result(input.head(), input.tail())));
    }

    private ItemParser(Function<Input, Stream<Result>> parser) {
        this.parser = parser;
    }

    public Stream<Result> parse(Input input) {
        return parser.apply(input);
    }
}
