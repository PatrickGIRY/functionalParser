package tools.functional.parser;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class ParserInt {
    private final Function<Input, Result> parser;

    public static ParserInt of(Function<Input, ParserInt.Result> parser) {
        requireNonNull(parser);
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
        requireNonNull(input);
        return requireNonNull(parser.apply(input));
    }

    public ParserInt orElse(ParserInt other) {
        requireNonNull(other);
        return of(input -> parse(input).or(() -> other.parse(input)));
    }

    public ParserInt map(IntUnaryOperator mapper) {
        requireNonNull(mapper);
        return of(input -> parse(input).map(mapper));
    }

    public ParserInt flatMap(IntFunction<ParserInt> mapper) {
        return of(input -> parse(input).flatMap(mapper));
    }

    public interface Result {
        static Result failure(String errorMessage) {
            return new Failure(errorMessage);
        }

        static Result success(int matchedValue, Input remainingInput) {
            return new Success(matchedValue, remainingInput);
        }

        default Result or(Supplier<Result> otherResult) {
            return this;
        }

        default Result map(IntUnaryOperator mapper) { return this; };

        default Result flatMap(IntFunction<ParserInt> mapper) {
            return this;
        }
    }

    private record Success(int matchedValue, Input remainingInput) implements Result {
        public Success {
            requireNonNull(remainingInput);
        }

        @Override
        public Result map(IntUnaryOperator mapper) {
            return Result.success(mapper.applyAsInt(matchedValue), remainingInput);
        }

        @Override
        public Result flatMap(IntFunction<ParserInt> mapper) {
            return mapper.apply(matchedValue).parse(remainingInput);
        }
    }

    private record Failure(String errorMessage) implements Result {
        public Failure {
            requireNonNull(errorMessage);
        }
        @Override
        public Result or(Supplier<Result> otherResult) {
            return requireNonNull(otherResult.get());
        }
    }
}
