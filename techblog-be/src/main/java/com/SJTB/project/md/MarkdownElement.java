package com.SJTB.project.md;

public class MarkdownElement {
    String content;
    String type;
    int depth;

    MarkdownElement(String content, String type, int depth) {
        this.content = content;
        this.type = type;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Depth: %d, Content: %s", type, depth, content);
    }

    public String toStringContent() {
        return content;
    }
}
