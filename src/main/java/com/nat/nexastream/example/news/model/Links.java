package com.nat.nexastream.example.news.model;

import java.util.List;

public class Links {
    private List<Link> self;
    private List<Link> collection;
    private List<Link> about;
    private List<Link> author;
    private List<Link> replies;
    private List<Link> version_history;
    private List<Link> predecessor_version;
    private List<Link> wp;

    public List<Link> getSelf() {
        return self;
    }

    public void setSelf(List<Link> self) {
        this.self = self;
    }

    public List<Link> getCollection() {
        return collection;
    }

    public void setCollection(List<Link> collection) {
        this.collection = collection;
    }

    public List<Link> getAbout() {
        return about;
    }

    public void setAbout(List<Link> about) {
        this.about = about;
    }

    public List<Link> getAuthor() {
        return author;
    }

    public void setAuthor(List<Link> author) {
        this.author = author;
    }

    public List<Link> getReplies() {
        return replies;
    }

    public void setReplies(List<Link> replies) {
        this.replies = replies;
    }

    public List<Link> getVersion_history() {
        return version_history;
    }

    public void setVersion_history(List<Link> version_history) {
        this.version_history = version_history;
    }

    public List<Link> getPredecessor_version() {
        return predecessor_version;
    }

    public void setPredecessor_version(List<Link> predecessor_version) {
        this.predecessor_version = predecessor_version;
    }

    public List<Link> getWp() {
        return wp;
    }

    public void setWp(List<Link> wp) {
        this.wp = wp;
    }

    @Override
    public String toString() {
        return "Links{" +
                "self=" + self +
                ", collection=" + collection +
                ", about=" + about +
                ", author=" + author +
                ", replies=" + replies +
                ", version_history=" + version_history +
                ", predecessor_version=" + predecessor_version +
                ", wp=" + wp +
                '}';
    }
}
