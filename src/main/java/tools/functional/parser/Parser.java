package tools.functional.parser;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Parser<T> {
    private final Function<Input, Result<T>> parser;

    public static <T> Parser<T> of(Function<Input, Result<T>> parser) {
        return new Parser<>(parser);
    }

    private Parser(Function<Input, Result<T>> parser) {
        this.parser = parser;
    }

    public static <T> Parser<T> valueOf(T value) {
        return of(input -> Result.success(value, input));
    }

    public Result<T> parse(Input input) {
        return parser.apply(input);
    }

    public Parser<T> orElse(Parser<T> other) {
        return of(input -> parse(input).or(() -> other.parse(input)));
    }

    public <U> Parser<U> apply(Parser<Function<T, U>> functionParser) {
        return Parser.of(input -> functionParser.parse(input)
        .flatMap((matchedObject, remainingInput) -> this.map(matchedObject)
                .parse(remainingInput)));
    }

    public <U> Parser<U> map(Function<T, U> mapper) {
        return Parser.of(input -> parse(input).map(mapper));
    }

    public abstract static class Result<T> {

        public static <T> Result<T> success(T matchedObject, Input remainingInput) {
            return new Result.Success<>(matchedObject, remainingInput);
        }

        public static <T> Result<T> failure(String errorMessage) {
            return new Result.Failure<>(errorMessage);
        }

        public Result<T> or(Supplier<Result<T>> supplier) {
            return this;
        }

        public abstract <U> Result<U> flatMap(BiFunction<T, Input, Result<U>> mapper);

        public abstract <U> Result<U> map(Function<T, U> mapper);

        private static class Success<T> extends Result<T> {
            private final T matchedObject;
            private final Input remainingInput;

            private Success(T matchedObject, Input remainingInput) {

                this.matchedObject = matchedObject;
                this.remainingInput = remainingInput;
            }

            @Override
            public <U> Result<U> map(Function<T, U> mapper) {
                return success(mapper.apply(matchedObject), remainingInput);
            }

            @Override
            public <U> Result<U> flatMap(BiFunction<T, Input, Result<U>> mapper) {
                return mapper.apply(matchedObject, remainingInput);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Success<?> success)) return false;
                return Objects.equals(matchedObject, success.matchedObject) &&
                        Objects.equals(remainingInput, success.remainingInput);
            }

            @Override
            public int hashCode() {
                return Objects.hash(matchedObject, remainingInput);
            }

            @Override
            public String toString() {
                return "Success{" +
                        "matchedObject=" + matchedObject +
                        ", remainingInput=" + remainingInput +
                        '}';
            }
        }

        private static class Failure<T> extends Result<T> {
            private final String errorMessage;

            private Failure(String errorMessage) {
                super();
                this.errorMessage = errorMessage;
            }

            @Override
            public Result<T> or(Supplier<Result<T>> supplier) {
                return supplier.get();
            }

            @Override
            public <U> Result<U> map(Function<T, U> mapper) {
                return failure(errorMessage);
            }

            @Override
            public <U> Result<U> flatMap(BiFunction<T, Input, Result<U>> mapper) {
                return failure(errorMessage);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Failure<?> failure)) return false;
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
