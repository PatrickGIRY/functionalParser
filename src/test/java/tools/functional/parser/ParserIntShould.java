package tools.functional.parser;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserIntShould {

    private static final Input ANY_INPUT = new Input("Any input");
    private static final Input ANY_REMAINING_INPUT = new Input("Any remaining input");
    private static final int ANY_MATCHED_VALUE = 123;
    private static final int ANY_VALUE = 234;

    @Test
    public void create_a_failure_result_with_the_given_error_message() {
        var errorMessage = "Error message";
        var failure = ParserInt.Result.failure(errorMessage);

        assertThat(failure).isNotNull();
        assertThat(failure).isEqualTo(ParserInt.Result.failure(errorMessage));
    }

    @Test
    public void create_a_success_with_the_matched_value_and_the_remaining_input() {
        var success = ParserInt.Result.success(ANY_MATCHED_VALUE, ANY_REMAINING_INPUT);

        assertThat(success).isNotNull();
    }

    @Test
    public void execute_the_given_parser_function_and_return_its_result_on_parse() {
        var parserResult = ParserInt.Result.success(ANY_MATCHED_VALUE, ANY_REMAINING_INPUT);
        var parser = ParserInt.of(input -> parserResult);

        ParserInt.Result result = parser.parse(ANY_INPUT);

        assertThat(result).isEqualTo(parserResult);
    }

    @Test
    public void create_a_parser_that_always_fail() {
        var parser = ParserInt.failure();

        ParserInt.Result result = parser.parse(ANY_INPUT);

        assertThat(result).isEqualTo(ParserInt.Result.failure(""));
    }

    @Test
    public void create_a_parser_that_always_succeed() {
        var parser = ParserInt.valueOf(ANY_VALUE);

        ParserInt.Result result = parser.parse(ANY_INPUT);

        assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
    }

    @Test
    public void create_a_parser_that_choice_the_first_one_when_it_succeed() {
        var parser = ParserInt.valueOf(ANY_VALUE).orElse(ParserInt.failure());

        ParserInt.Result result = parser.parse(ANY_INPUT);

        assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
    }

    @Test
    public void create_a_parser_that_choice_the_second_one_when_the_first_fail() {
        var parser = ParserInt.failure().orElse(ParserInt.valueOf(ANY_VALUE));

        ParserInt.Result result = parser.parse(ANY_INPUT);

        assertThat(result).isEqualTo(ParserInt.Result.success(ANY_VALUE, ANY_INPUT));
    }
}
