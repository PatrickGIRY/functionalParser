package tools.functional.parser;

import java.util.List;
import java.util.Objects;
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

    public abstract static class Result<T> {

        public static <T> Result<T> success(T matchedObject, Input remainingInput) {
            return new Result.Success<>(matchedObject, remainingInput);
        }

        public static <T> Result<T> failure(String errorMessage) {
            return new Result.Failure<>(errorMessage);
        }

        abstract <U> Result<U> applyMatchedObjectAndParseRemainingInput(Function<T, Parser<U>> mapper);

        public Result<T> or(Supplier<Result<T>> supplier) {
            return this;
        }

        private static class Success<T> extends Result<T> {
            private final T matchedObject;
            private final Input remainingInput;

            private Success(T matchedObject, Input remainingInput) {

                this.matchedObject = matchedObject;
                this.remainingInput = remainingInput;
            }

            @Override
            <U> Result<U> applyMatchedObjectAndParseRemainingInput(Function<T, Parser<U>> mapper) {
                return mapper.apply(matchedObject).parse(remainingInput);
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
            <U> Result<U> applyMatchedObjectAndParseRemainingInput(Function<T, Parser<U>> mapper) {
                throw new IllegalAccessError("Not Yet Implemented");
            }

            @Override
            public Result<T> or(Supplier<Result<T>> supplier) {
                return supplier.get();
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
