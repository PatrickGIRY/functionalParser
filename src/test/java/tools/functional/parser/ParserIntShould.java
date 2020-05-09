package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Parser of int should")
public class ParserIntShould {

    private static final String ANY_INPUT_LINE = "Any input";
    private static final int ANY_INPUT_LENGTH = ANY_INPUT_LINE.length();
    private static final Input ANY_INPUT = new Input(ANY_INPUT_LINE);
    private static final Input ANY_REMAINING_INPUT = new Input("Any remaining input");
    private static final Input EMPTY_REMAINING_INPUT = new Input("");
    private static final int ANY_MATCHED_VALUE = 123;
    private static final int ANY_VALUE = 234;
    private static final String ERROR_MESSAGE = "Error message";

    @Nested
    @DisplayName("create a result the represents")
    public class CreateAResultThatRepresents {

        @Test
        @DisplayName("a failure with the given error message")
        public void a_failure_with_the_given_error_message() {
            var failure = ParserInt.Result.failure(ERROR_MESSAGE);

            assertThat(failure).isNotNull();
            assertThat(failure).isEqualTo(ParserInt.Result.failure(ERROR_MESSAGE));
        }

        @Test
        @DisplayName("a success with the matched value and the remaining input")
        public void a_success_with_the_matched_value_and_the_remaining_input() {
            var success = ParserInt.Result.success(ANY_MATCHED_VALUE, ANY_REMAINING_INPUT);

            assertThat(success).isNotNull();
        }
    }

    @Nested
    @DisplayName("execute the given parser")
    public class ExecuteTheGivenParser {

        @Test
        @DisplayName("with the given function and return its result on parse")
        public void with_the_given_function_and_return_its_result_on_parse() {
            var parserResult = ParserInt.Result.success(ANY_MATCHED_VALUE, ANY_REMAINING_INPUT);
            var parser = ParserInt.of(input -> parserResult);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(parserResult);
        }
    }

    @Nested
    @DisplayName("create a parser that")
    public class CreateAParserThat {

        @Test
        @DisplayName("always fail")
        public void always_fail() {
            var parser = ParserInt.failure();

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.failure(""));
        }

        @Test
        @DisplayName("always succeed")
        public void always_succeed() {
            var parser = ParserInt.valueOf(ANY_VALUE);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
        }

        @Test
        @DisplayName("choice the first one when it succeed")
        public void choice_the_first_one_when_it_succeed() {
            var parser = ParserInt.valueOf(ANY_VALUE).orElse(ParserInt.failure());

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
        }

        @Test
        @DisplayName("choice the second one when the first fail")
        public void choice_the_second_one_when_the_first_fail() {
            var parser = ParserInt.failure().orElse(ParserInt.valueOf(ANY_VALUE));

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
        }

        @Test
        @DisplayName("apply a function on parser result")
        public void apply_a_function_on_parser_result() {
            var parser = ParserInt.valueOf(10).map(v -> v + 10);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(20, ANY_INPUT));
        }

        @Test
        @DisplayName("apply a function that return a parser on parser result")
        public void apply_a_function_that_return_a_parser_on_parser_result() {
            var parser = ParserInt.valueOf(42).flatMap(x -> ParserInt.valueOf(69));

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(69, ANY_INPUT));
        }

        @Test
        @DisplayName("satisfy a predicate")
        public void satisfy_a_predicate() {
            var parser = ParserInt.valueOf(19).satisfy(x -> x > 10);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.success(19, ANY_INPUT));
        }

        @Test
        @DisplayName("not satisfy a predicate")
        public void not_satisfy_a_predicate() {
            var parser = ParserInt.valueOf(9).satisfy(x -> x > 10);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(ParserInt.failure().parse(ANY_INPUT));
        }

        @Test
        @DisplayName("map parser result with success as an object")
        public void map_parser_result_with_success_as_an_object() {
            var parser = ParserInt.valueOf(10).mapToObj(String::valueOf);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(Parser.Result.success("10", ANY_INPUT));
        }

        @Test
        @DisplayName("map parser result with failure as an object")
        public void map_parser_result_with_failure_as_an_object() {
            var parser = ParserInt.failure().mapToObj(String::valueOf);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(Parser.Result.failure(""));
        }

        @Test
        @DisplayName("apply a parser that return a function")
        public void apply_a_parser_that_return_a_function() {
            IntFunction<String> function = String::valueOf;
            var functionParser = Parser.of(input -> Parser.Result.success(function, input));
            var parser = ParserInt.valueOf(10).apply(functionParser);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(Parser.Result.success("10", ANY_INPUT));
        }

        @Test
        @DisplayName("apply a parser many times")
        public void apply_a_parser_many_times() {
            var matchedValue = 9;
            var parser = ParserInt.of(input ->
                    input.isEmpty()
                            ? ParserInt.Result.failure("")
                            : ParserInt.Result.success(matchedValue, input.tail()))
                    .many();

            var result = parser.parse(ANY_INPUT);

            var expectedMatchedList = IntStream
                    .range(0, ANY_INPUT_LENGTH)
                    .mapToObj(i -> matchedValue)
                    .collect(toUnmodifiableList());
            assertThat(result).isEqualTo(Parser.Result.success(expectedMatchedList, EMPTY_REMAINING_INPUT));
        }

        @Test
        @DisplayName("apply a parser many times even when it fails")
        public void apply_a_parser_many_times_even_when_it_fails() {
            var parser = ParserInt.failure().many();

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(Parser.Result.success(List.of(), ANY_INPUT));
        }

        @Test
        @DisplayName("apply a parser some times")
        public void apply_a_parser_some_times() {
            var matchedValue = 9;
            var parser = ParserInt.of(input ->
                    input.isEmpty()
                            ? ParserInt.Result.failure("")
                            : ParserInt.Result.success(matchedValue, input.tail()))
                    .some();

            var result = parser.parse(ANY_INPUT);

            var expectedMatchedList = IntStream
                    .range(0, ANY_INPUT_LENGTH)
                    .mapToObj(i -> matchedValue)
                    .collect(toUnmodifiableList());
            assertThat(result).isEqualTo(Parser.Result.success(expectedMatchedList, EMPTY_REMAINING_INPUT));
        }

        @Test
        @DisplayName("not apply a parser some times when it fails")
        public void not_apply_a_parser_some_times_when_it_fails() {
            var parser = ParserInt.failure().some();

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(Parser.Result.failure(""));
        }
    }

    @Nested
    @DisplayName("throw NullPointerException when")
    public class ThrowNullPointerExceptionWhen {

        @Test
        @DisplayName("the given parser function is null")
        public void the_given_parser_function_is_null() {
            assertThatThrownBy(() -> ParserInt.of(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("the given input is null")
        public void the_given_input_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).parse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("the parser result is null")
        public void the_parser_result_is_null() {
            assertThatThrownBy(() -> ParserInt.of(input -> null).parse(ANY_INPUT))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("other choice parser is null")
        public void other_choice_parser_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).orElse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("function to apply on parser result is null")
        public void function_to_apply_on_parser_result_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).map(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("function to map to object on parser result is null")
        public void function_to_map_to_object_on_parser_result_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).mapToObj(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("predicate to map to TEST parser result is null")
        public void predicate_to_test_parser_result_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).satisfy(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("function that return parser to apply on parser result is null")
        public void function_that_return_parser_to_apply_on_parser_result_is_null() {
            assertThatThrownBy(() -> ParserInt.valueOf(ANY_VALUE).flatMap(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("failure error message is null")
        public void failure_error_message_is_null() {
            assertThatThrownBy(() -> ParserInt.Result.failure(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("success remaining input is null")
        public void success_remaining_input_is_null() {
            assertThatThrownBy(() -> ParserInt.Result.success(ANY_VALUE, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("failure or supplier return null")
        public void failure_or_supplier_return_null() {
            assertThatThrownBy(() -> ParserInt.Result.failure("").or(() -> null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("apply with a null parser function as parameter")
        public void apply_with_a_null_parser_function_as_parameter() {
            assertThatThrownBy(() -> ParserInt.valueOf(1).apply(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
