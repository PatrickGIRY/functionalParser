package tools.functional.parser;

public record Input(String line) {
    public static Input empty() {
        return new Input(null);
    }

    boolean isEmpty() {
        return null == line || line.isEmpty();
    }

    int head() {
        return line.codePointAt(0);
    }

    Input tail() {
        return new Input(line.substring(1));
    }
}
