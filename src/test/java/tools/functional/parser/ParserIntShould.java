package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserInt should")
public class ParserIntShould {

    private static final Input EMPTY_INPUT = new Input("");

    @Nested
    @DisplayName("return failure result")
    class FailureResult {

        @Test
        public void when_the_content_of_the_input_is_null() {
            var parser = ParserInt.of(input -> ParserInt.Result.success('Z', null));
            var input = new Input("A");

            ParserInt.Result result = parser.parse(input);

            assertThat(result).isEqualTo(ParserInt.Result.failure());
        }
        @Test
        public void when_the_content_of_the_input_is_empty() {
            var parser = ParserInt.of(input -> ParserInt.Result.failure());

            ParserInt.Result result = parser.parse(EMPTY_INPUT);

            assertThat(result).isEqualTo(ParserInt.Result.failure());
        }

    }

    @Test
    public void return_success_result_with_the_single_character_and_an_empty_input_when_the_content_of_the_input_is_a_single_character() {
        var parser = ParserInt.of(input -> ParserInt.Result.success('A', EMPTY_INPUT));
        var input = new Input("A");

        ParserInt.Result result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.success('A', EMPTY_INPUT));
    }
}
