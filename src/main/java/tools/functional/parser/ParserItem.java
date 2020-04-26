package tools.functional.parser;

public class ParserItem {

    private static final String NO_INPUT_ERROR_MESSAGE = "No input";

    public static ParserInt of() {
        return ParserInt.of(input -> input.isEmpty()
                ? ParserInt.Result.failure(NO_INPUT_ERROR_MESSAGE)
                : ParserInt.Result.success(input.head(), input.tail()));
    }

    private ParserItem() { }
}
