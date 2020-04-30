package tools.functional.parser;

import java.util.function.*;

import static java.util.Objects.requireNonNull;

public record ParserInt(Function<Input, ParserInt.Result> parser) {

    public ParserInt {
        requireNonNull(parser);
    }

    public static ParserInt failure() {
        return new ParserInt(input -> Result.failure(""));
    }

    public static ParserInt valueOf(int value) {
        return new ParserInt(input -> Result.success(value, input));
    }

    public Result parse(Input input) {
        requireNonNull(input);
        return requireNonNull(parser.apply(input));
    }

    public ParserInt orElse(ParserInt other) {
        requireNonNull(other);
        return new ParserInt(input -> parse(input).or(() -> other.parse(input)));
    }

    public ParserInt map(IntUnaryOperator mapper) {
        requireNonNull(mapper);
        return new ParserInt(input -> parse(input).map(mapper));
    }

    public ParserInt flatMap(IntFunction<ParserInt> mapper) {
        requireNonNull(mapper);
        return new ParserInt(input -> parse(input).flatMap(mapper));
    }

    public ParserInt satisfy(IntPredicate predicate) {
        return flatMap(v -> predicate.test(v) ? valueOf(v) : null);
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
