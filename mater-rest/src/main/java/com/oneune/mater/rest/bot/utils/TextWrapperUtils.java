package com.oneune.mater.rest.bot.utils;

import com.oneune.mater.rest.main.store.enums.TextWrapperType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextWrapperUtils {

    private final static String MARKDOWN_ITALIC_TEMPLATE = "_%s_";
    private final static String HTML_ITALIC_TEMPLATE = "<i>%s</i>";

    private final static String MARKDOWN_BOLD_TEMPLATE = "*%s*";
    private final static String HTML_BOLD_TEMPLATE = "<b>%s</b>";

    private final static String MARKDOWN_MONOSPACED_TEMPLATE = "```%s```";
    private final static String HTML_MONOSPACED_TEMPLATE = "<code>%s</code>";

    private final static String MARKDOWN_STRIKETHROUGH_TEMPLATE = "~%s~";
    private final static String HTML_STRIKETHROUGH_TEMPLATE = "<s>%s</s>";

    private final static String MARKDOWN_UNDERLINE_TEMPLATE = "__%s__";
    private final static String HTML_UNDERLINE_TEMPLATE = "<u>%s</u>";

    private final static String MARKDOWN_LINK_TEMPLATE = "[%s](%s)";
    private final static String HTML_LINK_TEMPLATE = "<a href='%s'>%s</a>";

    String content;
    final TextWrapperType type;

    public static TextWrapperUtils wrap(String rawContent, TextWrapperType type) {
        return new TextWrapperUtils(rawContent, type);
    }

    public static TextWrapperUtils wrapHtml(String rawContent) {
        return new TextWrapperUtils(rawContent, TextWrapperType.HTML);
    }

    public static TextWrapperUtils wrapMarkdown(String rawContent) {
        return new TextWrapperUtils(rawContent, TextWrapperType.MARKDOWN);
    }

    private TextWrapperUtils(String rawContent, TextWrapperType type) {
        this.content = rawContent;
        this.type = type;
    }

    private String getTemplate(String htmlTemplate, String markdownTemplate) {
        return switch (this.type) {
            case HTML -> htmlTemplate;
            case MARKDOWN -> markdownTemplate;
            default -> throw new IllegalArgumentException("Not implemented wrapper type");
        };
    }

    public TextWrapperUtils italics() {
        this.content = this.getTemplate(
                HTML_ITALIC_TEMPLATE, MARKDOWN_ITALIC_TEMPLATE
        ).formatted(this.content);
        return this;
    }

    public TextWrapperUtils bold() {
        this.content = this.getTemplate(
                HTML_BOLD_TEMPLATE, MARKDOWN_BOLD_TEMPLATE
        ).formatted(this.content);
        return this;
    }

    public TextWrapperUtils monospaced() {
        this.content = this.getTemplate(
                HTML_MONOSPACED_TEMPLATE, MARKDOWN_MONOSPACED_TEMPLATE
        ).formatted(this.content);
        return this;
    }

    public TextWrapperUtils strikethrough() {
        this.content = this.getTemplate(
                HTML_STRIKETHROUGH_TEMPLATE, MARKDOWN_STRIKETHROUGH_TEMPLATE
        ).formatted(this.content);
        return this;
    }

    public TextWrapperUtils underlined() {
        this.content = this.getTemplate(
                HTML_UNDERLINE_TEMPLATE, MARKDOWN_UNDERLINE_TEMPLATE
        ).formatted(this.content);
        return this;
    }

//    public TextWrapperUtils link() {
//        this.content = this.getTemplate(
//                HTML_LINK_TEMPLATE, MARKDOWN_LINK_TEMPLATE
//        ).formatted(this.content);
//        return this;
//    }

    public TextWrapperUtils customHtml(String tag) {
        if (!this.type.equals(TextWrapperType.HTML)) {
            throw new IllegalStateException("Selected wrapper text not HTML!");
        }
        this.content = "<%s>".formatted(tag) + this.content + "</%s>".formatted(tag);
        return this;
    }

    public TextWrapperUtils customMarkdown(String tag) {
        if (!this.type.equals(TextWrapperType.MARKDOWN)) {
            throw new IllegalStateException("Selected wrapper text not Markdown!");
        }
        this.content = tag + this.content + tag;
        return this;
    }

    public String complete() {
        return this.content;
    }
}
