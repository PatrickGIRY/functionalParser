package tools.functional.parser;

import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

public class ParserInt {
    private final Function<Input, Result> parser;

    public static ParserInt of(Function<Input, ParserInt.Result> parser) {
        return new ParserInt(requireNonNull(parser));
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
        requireNonNull(mapper);
        return Parser.of(input -> parse(input).mapToObj(mapper));
    }

    public ParserInt flatMap(IntFunction<ParserInt> mapper) {
        requireNonNull(mapper);
        return of(input -> parse(input).applyMatchedValueAndParseRemainingInput(mapper));
    }

    public <U> Parser<U> flatMapToObj(IntFunction<Parser<U>> mapper) {
        return Parser.of(input -> parse(input).flatMap(v -> out -> mapper.apply(v).parse(out)));
    }

    public ParserInt satisfy(IntPredicate predicate) {
        requireNonNull(predicate);
        return flatMap(v -> predicate.test(v) ? valueOf(v) : failure());
    }

    public <U> Parser<U> apply(Parser<IntFunction<U>> parserFunction) {
        requireNonNull(parserFunction);
        return Parser.of(input -> parserFunction.parse(input)
                .flatMap((matchedObject, remainingInput) -> mapToObj(matchedObject).parse(remainingInput)));
    }

    public Parser<List<Integer>> many() {
        return some().orElse(Parser.valueOf(List.of()));
    }

    public Parser<List<Integer>> some() {
       return Parser.of(input -> parse(input)
               .flatMap(matchedValue -> remainingInput1 ->
                       many().parse(remainingInput1)
                               .flatMap((matchedValues, remainingInput2) ->
                                       Parser.Result.success(cons(matchedValue, matchedValues),
                                               remainingInput2))));
    }

    private List<Integer> cons(int v, List<Integer> collection) {
       return Stream.concat(Stream.of(v), collection.stream()).collect(toUnmodifiableList());
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

        Result or(Supplier<Result> otherResult) {
            return this;
        }

        Result map(IntUnaryOperator mapper) {
            return this;
        }

        Result applyMatchedValueAndParseRemainingInput(IntFunction<ParserInt> mapper) {
            return this;
        }

        abstract <U> Parser.Result<U> mapToObj(IntFunction<U> mapper);

        abstract <U> Parser.Result<U> flatMap(IntFunction<Function<Input, Parser.Result<U>>> mapper);

        private static class Success extends Result {
            private final int matchedValue;
            private final Input remainingInput;

            private Success(int matchedValue, Input remainingInput) {
                this.matchedValue = matchedValue;
                this.remainingInput = remainingInput;
            }

            @Override
            Result map(IntUnaryOperator mapper) {
                return Result.success(mapper.applyAsInt(matchedValue), remainingInput);
            }

            @Override
            <U> Parser.Result<U> mapToObj(IntFunction<U> mapper) {
                return Parser.Result.success(mapper.apply(matchedValue), remainingInput);
            }

            @Override
            Result applyMatchedValueAndParseRemainingInput(IntFunction<ParserInt> mapper) {
                return mapper.apply(matchedValue).parse(remainingInput);
            }

            @Override
            <U> Parser.Result<U> flatMap(IntFunction<Function<Input, Parser.Result<U>>> mapper) {
                return mapper.apply(matchedValue).apply(remainingInput);
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
            Result or(Supplier<Result> otherResult) {
                return requireNonNull(otherResult.get());
            }

            @Override
            <U> Parser.Result<U> mapToObj(IntFunction<U> mapper) {
                return Parser.Result.failure(errorMessage);
            }

            @Override
            <U> Parser.Result<U> flatMap(IntFunction<Function<Input, Parser.Result<U>>> mapper) {
                return Parser.Result.failure(errorMessage);
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
