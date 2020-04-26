package tools.functional.parser;

import java.util.function.Function;

public class ParserInt {
    private final Function<Input, Result> parser;

    public static ParserInt of(Function<Input, ParserInt.Result> parser) {
        return new ParserInt(parser);
    }

    private ParserInt(Function<Input, Result> parser) {
        this.parser = parser;
    }

    public Result parse(Input input) {
        return Result.failure();
    }

    public interface Result {

        static Result failure() {
            return null;
        }
    }
}
