package tools.functional.parser;

import java.util.Objects;
import java.util.function.Function;

public class Parser<T> {
    private final Function<Input, Result<T>> parser;

    public static <T> Parser<T> of(Function<Input, Result<T>> parser) {
        return new Parser<>(parser);
    }

    private Parser(Function<Input, Result<T>> parser) {
        this.parser = parser;
    }

    public Result<T> parse(Input input) {
        return parser.apply(input);
    }

    public abstract static class Result<T> {

        public static <T> Result<T> success(T matchedObject, Input remainingInput) {
            return new Result.Success<>(matchedObject, remainingInput);
        }

        public static <T> Result<T> failure(String errorMessage) {
            return null;
        }

        private static class Success<T> extends Result<T> {
            private final T matchedObject;
            private final Input remainingInput;

            private Success(T matchedObject, Input remainingInput) {

                this.matchedObject = matchedObject;
                this.remainingInput = remainingInput;
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
    }
}
