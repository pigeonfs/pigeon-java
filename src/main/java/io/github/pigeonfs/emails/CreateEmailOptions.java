package io.github.pigeonfs.emails;

import java.util.Arrays;
import java.util.List;

public class CreateEmailOptions {
    private final String from;
    private final List<String> to;
    private final String subject;
    private final String html;
    private final String text;
    private final List<String> cc;
    private final List<String> bcc;
    private final List<String> reply_to;

    private CreateEmailOptions(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.subject = builder.subject;
        this.html = builder.html;
        this.text = builder.text;
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.reply_to = builder.replyTo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String from;
        private List<String> to;
        private String subject;
        private String html;
        private String text;
        private List<String> cc;
        private List<String> bcc;
        private List<String> replyTo;

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String... to) {
            this.to = Arrays.asList(to);
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder html(String html) {
            this.html = html;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder cc(String... cc) {
            this.cc = Arrays.asList(cc);
            return this;
        }

        public Builder bcc(String... bcc) {
            this.bcc = Arrays.asList(bcc);
            return this;
        }

        public Builder replyTo(String... replyTo) {
            this.replyTo = Arrays.asList(replyTo);
            return this;
        }

        public CreateEmailOptions build() {
            return new CreateEmailOptions(this);
        }
    }
}
