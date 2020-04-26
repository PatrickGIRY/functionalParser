package tools.functional.parser;

import java.util.function.Function;

public class ParserInt {
    private final Function<Input, Result> parser;

    public static ParserInt of(Function<Input, ParserInt.Result> parser) {
        return new ParserInt(parser);
    }

    public static ParserInt failure() {
        return of(input -> Result.failure(""));
    }

    public static ParserInt valueOf(int value) {
        return of(input -> Result.success(value, input));
    }

    private ParserInt(Function<Input, Result> parser) {
        this.parser = parser;
    }

    public Result parse(Input input) {
        return parser.apply(input);
    }

    public ParserInt orElse(ParserInt other) {
        return of(input -> parse(input));
    }

    public interface Result {
        static Result failure(String errorMessage) {
            return new Failure(errorMessage);
        }

        static Result success(int matchedValue, Input remainingInput) {
            return new Success(matchedValue, remainingInput);
        }
    }

    private record Success(int matchedValue, Input remainingInput) implements Result { }

    private record Failure(String errorMessage) implements Result { }
}
