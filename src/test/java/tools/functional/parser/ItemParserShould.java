package tools.functional.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemParserShould {

    private static Stream<Input> nullOrEmptyInputProvider() {
        return Stream.of(new Input(null), new Input(""));
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyInputProvider")
    public void return_empty_when_input_is_empty(Input input) {
        var itemParser = ItemParser.of();

        Stream<Result> result = itemParser.parse(input);

        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    public void return_a_singleton_stream_with_result_with_first_character_and_an_empty_input_when_input_contains_one_character(String line) {

        var itemParser = ItemParser.of();
        var input = new Input(line);

        Stream<Result> result = itemParser.parse(input);

        var expectResult = new Result(line.codePointAt(0), new Input(""));
        assertThat(result).containsOnly(expectResult);
    }
}
