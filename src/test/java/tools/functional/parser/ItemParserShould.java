package tools.functional.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemParserShould {

    private static final String NO_INPUT = "No input";

    private static Stream<Input> nullOrEmptyInputProvider() {
        return Stream.of(new Input(null), new Input(""));
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyInputProvider")
    public void return_empty_when_input_is_empty(Input input) {
        var itemParser = ItemParser.of();

        var result = itemParser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.failure(NO_INPUT));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    public void return_a_singleton_stream_with_result_with_first_character_and_an_empty_input_when_input_contains_one_character(String line) {

        var itemParser = ItemParser.of();
        var input = new Input(line);

        var result = itemParser.parse(input);

        var expectResult = ParserInt.Result.success(line.codePointAt(0), new Input(""));
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "HELLO"})
    public void return_a_singleton_stream_with_result_with_first_character_and_input_containing_remaining_characters_when_input_contains_more_than_one_character(String line) {

        var itemParser = ItemParser.of();
        var input = new Input(line);

        var result = itemParser.parse(input);

        var expectResult = ParserInt.Result.success(line.codePointAt(0), new Input(line.substring(1)));
        assertThat(result).isEqualTo(expectResult);
    }
}
