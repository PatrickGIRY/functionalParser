package tools.functional.parser;

import java.util.function.Function;

public class Parser<T> {
    public static <T> Parser<T> of(Function<Input, Result<T>> parser) {
        return new Parser<>(parser);
    }

    private Parser(Function<Input, Result<T>> parser) {

    }

    public Result<T> parse(Input input) {
        return null;
    }

    public static abstract class Result<T> {

        public static <T> Result<T> success(T matchedObject, Input remainingInput) {
            return null;
        }
    }
}
