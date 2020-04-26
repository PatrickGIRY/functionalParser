package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserInt should")
public class ParserIntShould {

    private static final Input EMPTY_INPUT = new Input("");

    @Test
    public void create_a_failure_result_with_the_given_error_message() {
        var errorMessage = "Error message";
        var failure = ParserInt.Result.failure(errorMessage);

        assertThat(failure.errorMessage()).isEqualTo(errorMessage);
    }

    @Test
    public void execute_the_given_parser_function_and_return_its_result_on_parse() {
        var parserResult = ParserInt.Result.success('A', EMPTY_INPUT);
        var parser = ParserInt.of(input -> parserResult);
        var input = new Input("A");

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(parserResult);
    }
}
