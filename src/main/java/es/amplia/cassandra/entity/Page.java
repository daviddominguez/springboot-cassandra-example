package es.amplia.cassandra.entity;

import java.util.List;

public class Page<T> {

    public final String pageContext;
    public final List<T> content;

    public Page(String pageContext, List<T> content) {
        this.pageContext = pageContext;
        this.content = content;
    }
}
