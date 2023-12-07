package com.nat.nexastream.example.news.model;

public class Link {
    private String href;
    private boolean embeddable = true;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
    }

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                ", embeddable=" + embeddable +
                '}';
    }
}
