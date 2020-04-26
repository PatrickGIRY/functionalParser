package tools.functional.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserIntShould {

    @Test
    public void return_failure_result_when_input_contains_null() {
        var parser = ParserInt.of(input -> ParserInt.Result.failure());
        var input = new Input(null);

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.failure());
    }

    @Test
    public void return_success_result_with_the_signle_character_and_an_empty_input_when_there_is_signle_character_as_input() {
        var emptyInput = new Input("");
        var parser = ParserInt.of(input -> ParserInt.Result.success('A', emptyInput));
        var input = new Input("A");

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.success('A', emptyInput));
    }
}
