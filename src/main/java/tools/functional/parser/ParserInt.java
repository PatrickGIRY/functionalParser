package tools.functional.parser;

public class ParserInt {
    public static ParserInt of() {
        return new ParserInt();
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
