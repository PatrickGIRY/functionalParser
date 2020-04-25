package tools.functional.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemParserShould {

    @Test
    public void return_empty_when_input_is_empty() {
        var itemParser = ItemParser.of();
        var input = Input.empty();

        Stream<Result> result = itemParser.parse(input);

        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    public void return_a_singleton_stream_with_result_with_first_character_and_an_empty_input_when_input_contains_one_character(String line) {

        var itemParser = ItemParser.of();
        var input = new Input(line);

        Stream<Result> result = itemParser.parse(input);

        var expectResult = new Result(line.codePointAt(0), Input.empty());
        assertThat(result).containsOnly(expectResult);
    }
}
