package tools.functional.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserIntShould {


    @Test
    public void return_result_failure_when_input_contains_null() {
        var parser = ParserInt.of();
        var input = new Input(null);

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.failure());
    }

}
