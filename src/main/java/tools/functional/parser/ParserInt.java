package tools.functional.parser;

import java.util.Objects;
import java.util.function.*;

import static java.util.Objects.requireNonNull;

public class ParserInt {
    private final Function<Input, Result> parser;

    public static ParserInt of(Function<Input, ParserInt.Result> parser) {
        return new ParserInt(requireNonNull(parser));
    }

    public static ParserInt failure() {
        return new ParserInt(input -> Result.failure(""));
    }

    public static ParserInt valueOf(int value) {
        return new ParserInt(input -> Result.success(value, input));
    }

    private ParserInt(Function<Input, Result> parser) {
        this.parser = parser;
    }

    public Result parse(Input input) {
        return requireNonNull(parser.apply(requireNonNull(input)));
    }

    public ParserInt orElse(ParserInt other) {
        requireNonNull(other);
        return of(input -> parse(input).or(() -> other.parse(input)));
    }

    public ParserInt map(IntUnaryOperator mapper) {
        requireNonNull(mapper);
        return of(input -> parse(input).map(mapper));
    }

    public <U> Parser<U> mapToObj(IntFunction<U> mapper) {
        return Parser.of(input -> parse(input).mapToObj(mapper));
    }

    public ParserInt flatMap(IntFunction<ParserInt> mapper) {
        requireNonNull(mapper);
        return of(input -> parse(input).flatMap(mapper));
    }

    public ParserInt satisfy(IntPredicate predicate) {
        return flatMap(v -> predicate.test(v) ? valueOf(v) : failure());
    }

    public abstract static class Result {
        public static Result failure(String errorMessage) {
            return new Failure(requireNonNull(errorMessage));
        }

        public static Result success(int matchedValue, Input remainingInput) {
            return new Success(matchedValue, requireNonNull(remainingInput));
        }

        private Result() {
        }

        public Result or(Supplier<Result> otherResult) {
            return this;
        }

        public Result map(IntUnaryOperator mapper) {
            return this;
        }

        public Result flatMap(IntFunction<ParserInt> mapper) {
            return this;
        }

        public abstract <U> Parser.Result<U> mapToObj(IntFunction<U> mapper);

        private static class Success extends Result {
            private final int matchedValue;
            private final Input remainingInput;

            private Success(int matchedValue, Input remainingInput) {
                this.matchedValue = matchedValue;
                this.remainingInput = remainingInput;
            }

            @Override
            public Result map(IntUnaryOperator mapper) {
                return Result.success(mapper.applyAsInt(matchedValue), remainingInput);
            }

            @Override
            public <U> Parser.Result<U> mapToObj(IntFunction<U> mapper) {
                return Parser.Result.success(mapper.apply(matchedValue), remainingInput);
            }

            @Override
            public Result flatMap(IntFunction<ParserInt> mapper) {
                return mapper.apply(matchedValue).parse(remainingInput);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Success success)) return false;
                return matchedValue == success.matchedValue &&
                        Objects.equals(remainingInput, success.remainingInput);
            }

            @Override
            public int hashCode() {
                return Objects.hash(matchedValue, remainingInput);
            }

            @Override
            public String toString() {
                return "Success{" +
                        "matchedValue=" + matchedValue +
                        ", remainingInput=" + remainingInput +
                        '}';
            }
        }

        private static class Failure extends Result {
            private final String errorMessage;

            private Failure(String errorMessage) {
                this.errorMessage = errorMessage;
            }

            @Override
            public Result or(Supplier<Result> otherResult) {
                return requireNonNull(otherResult.get());
            }

            @Override
            public <U> Parser.Result<U> mapToObj(IntFunction<U> mapper) {
                return  Parser.Result.failure(errorMessage);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Failure failure)) return false;
                return Objects.equals(errorMessage, failure.errorMessage);
            }

            @Override
            public int hashCode() {
                return Objects.hash(errorMessage);
            }

            @Override
            public String toString() {
                return "Failure{" +
                        "errorMessage='" + errorMessage + '\'' +
                        '}';
            }
        }
    }

}
