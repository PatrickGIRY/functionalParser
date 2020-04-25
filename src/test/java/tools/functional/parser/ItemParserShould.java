package tools.functional.parser;

import org.junit.jupiter.api.Test;
import tools.functional.parser.Input;
import tools.functional.parser.ItemParser;
import tools.functional.parser.Result;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemParserShould {

    @Test
    public void return_empty_when_input_is_empty() {
        var itemParser = ItemParser.of();
        var input = Input.of();

        Stream<Result> result = itemParser.parse(input);

        assertThat(result).isEmpty();
    }
}
