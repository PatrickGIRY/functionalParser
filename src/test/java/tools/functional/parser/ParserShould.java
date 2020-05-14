package tools.functional.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A parser should")
public class ParserShould {

    @Nested
    @DisplayName("in case of basic parser")
    public class BasicParser {

        @Test
        @DisplayName("fails if the input is empty")
        public void fails_if_the_input_is_empty() {
            var parser = Parser.item();

            var result = parser.parse("");

            assertThat(result).isEmpty();
        }
    }
}
