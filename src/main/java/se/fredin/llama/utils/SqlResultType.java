package se.fredin.llama.utils;

public enum SqlResultType {

    ITERATE("true"),
    ALL("false");

    private String label;

    SqlResultType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}