package tools.functional.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserIntShould {

    @Test
    public void create_a_failure_result_with_the_given_error_message() {
        var errorMessage = "Error message";
        var failure = ParserInt.Result.failure(errorMessage);

        assertThat(failure).isNotNull();
        assertThat(failure).isEqualTo(ParserInt.Result.failure(errorMessage));
    }

    @Test
    public void create_a_success_with_the_matched_value_and_the_remaining_input() {
        var matchedValue = 123;
        var remainingInput = new Input("Any remaining input");

        var success = ParserInt.Result.success(matchedValue, remainingInput);

        assertThat(success).isNotNull();
    }

    @Test
    public void execute_the_given_parser_function_and_return_its_result_on_parse() {
        var parserResult = ParserInt.Result.success('A', new Input("Any remaining input"));
        var parser = ParserInt.of(input -> parserResult);
        var input = new Input("A");

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(parserResult);
    }

    @Test
    public void create_a_parser_that_always_fail() {
        var parser = ParserInt.failure();
        var input = new Input("Any input");

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.failure(""));


    }
}
