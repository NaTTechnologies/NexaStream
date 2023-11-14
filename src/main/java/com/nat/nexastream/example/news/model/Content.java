package com.nat.nexastream.example.news.model;

public class Content {
    private String rendered;
    private boolean protectedField;

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

    public boolean isProtectedField() {
        return protectedField;
    }

    public void setProtectedField(boolean protectedField) {
        this.protectedField = protectedField;
    }

    @Override
    public String toString() {
        return "Content{" +
                "rendered='" + rendered + '\'' +
                ", protectedField=" + protectedField +
                '}';
    }
}
