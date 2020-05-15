package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A parser of character should")
public class ParserCharShould {

    @Nested
    @DisplayName("in case of item parser")
    public class ItemParser {

        @Test
        @DisplayName("fails if the input is empty")
        public void fails_if_the_input_is_empty() {
            var parser = ParserChar.item();

            var result = parser.parse("");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("fails if the input is null")
        public void fails_if_the_input_is_null() {
            var parser = ParserChar.item();

            var result = parser.parse(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("consumes the first character of input if it is not empty")
        public void consumes_the_first_character_of_the_input_if_it_is_not_empty() {
            var parser = ParserChar.item();

            var result = parser.parse("A");

            assertThat(result).hasValue(new ParserChar.SuccessChar('A', ""));
        }
    }

    @Nested
    @DisplayName("in case of failure parser")
    public class FailureParser {

        @Test
        @DisplayName("always fails")
        public void always_fails() {
            var parser = ParserChar.failure();

            var result = parser.parse("Any input");

            assertThat(result).isEmpty();
        }
    }
}
