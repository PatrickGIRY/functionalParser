package tools.functional.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static tools.functional.parser.ParserItem.item;

public class ParserItemShould {

    private static final String NO_INPUT = "No input";

    private static Stream<Input> nullOrEmptyInputProvider() {
        return Stream.of(new Input(null), new Input(""));
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyInputProvider")
    public void return_a_failure_result_when_input_is_empty(Input input) {
        var parser = item();

        var result = parser.parse(input);

        assertThat(result).isEqualTo(ParserInt.Result.failure(NO_INPUT));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    public void return_a_success_result_with_first_character_and_an_empty_input_when_input_contains_one_character(String line) {

        var parser = item();
        var input = new Input(line);

        var result = parser.parse(input);

        var expectResult = ParserInt.Result.success(line.codePointAt(0), new Input(""));
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "HELLO"})
    public void return_a_success_result_with_first_character_and_input_containing_remaining_characters_when_input_contains_more_than_one_character(String line) {

        var parser = item();
        var input = new Input(line);

        var result = parser.parse(input);

        var expectResult = ParserInt.Result.success(line.codePointAt(0), new Input(line.substring(1)));
        assertThat(result).isEqualTo(expectResult);
    }

    @Test
    public void apply_parser_as_long_as_it_succeeds() {
        var parser = item().satisfy(Character::isDigit).many();
        var input = new Input("123ABC");

        var result = parser.parse(input);

        assertThat(result).isEqualTo(Parser.Result.success(CodePoints.of('1', '2', '3'), new Input("ABC")));
    }

    private static class CodePoints {
        private static List<Integer> of(char ... characters) {
            return IntStream.range(0, numberOf(characters))
                    .mapToObj(codePointsOf(characters))
                    .collect(toUnmodifiableList());
        }

        private static int numberOf(char[] characters) {
            return characters != null ? characters.length : 0;
        }

        private static IntFunction<Integer> codePointsOf(char[] characters) {
            return index -> Character.codePointAt(characters, index);
        }
    }
}
