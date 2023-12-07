package com.nat.nexastream.example.news.model;

public class Meta {
    private String footnotes;

    public String getFootnotes() {
        return footnotes;
    }

    public void setFootnotes(String footnotes) {
        this.footnotes = footnotes;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "footnotes='" + footnotes + '\'' +
                '}';
    }
}
