package tools.functional.parser;

import org.junit.jupiter.api.Test;

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

    @Test
    public void return_a_singleton_stream_with_result_with_first_character_and_an_empty_input_when_input_contains_one_character() {

        var itemParser = ItemParser.of();
        var line = "A";
        var input = new Input(line);

        Stream<Result> result = itemParser.parse(input);

        var expectResult = new Result('A', Input.empty());
        assertThat(result).containsOnly(expectResult);
    }
}
