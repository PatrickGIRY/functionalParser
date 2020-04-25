package tools.functional.parser;

public record Input(String line) {
    public static Input empty() {
        return new Input(null);
    }

    public boolean isEmpty() {
        return null == line;
    }

    public int head() {
        return line.codePointAt(0);
    }

    public Input tail() {
        return new Input("");
    }
}
