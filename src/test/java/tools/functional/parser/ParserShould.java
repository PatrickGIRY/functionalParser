package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Parser should")
public class ParserShould {
    private static final Input ANY_INPUT = new Input("Any input");
    private static final Input ANY_REMAINING_INPUT = new Input("Any remaining input");
    private static final String ANY_MATCHED_OBJECT = "Any matched object";
    private static final String ERROR_MESSAGE = "Error message";

    @Nested
    @DisplayName("create a result the represents")
    public class CreateAResultThatRepresents {

        @Test
        @DisplayName("a success with the matched value and the remaining input")
        public void a_success_with_the_matched_value_and_the_remaining_input() {
            var success = Parser.Result.success(ANY_MATCHED_OBJECT, ANY_REMAINING_INPUT);

            assertThat(success).isNotNull();
        }

        @Test
        @DisplayName("a failure with the given error message")
        public void a_failure_with_the_given_error_message() {
            var failure = Parser.Result.failure(ERROR_MESSAGE);

            assertThat(failure).isNotNull();
            assertThat(failure).isEqualTo(Parser.Result.failure(ERROR_MESSAGE));
        }

    }

    @Nested
    @DisplayName("execute the given parser")
    public class ExecuteTheGivenParser {

        @Test
        @DisplayName("with the given function and return its result on parse")
        public void with_the_given_function_and_return_its_result_on_parse() {
            var parserResult = Parser.Result.success(ANY_MATCHED_OBJECT, ANY_REMAINING_INPUT);
            var parser = Parser.of(input -> parserResult);

            var result = parser.parse(ANY_INPUT);

            assertThat(result).isEqualTo(parserResult);
        }
    }
}