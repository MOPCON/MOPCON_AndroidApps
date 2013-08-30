package org.mopcon.model;

/**
 * Created by chuck on 13/8/29.
 */
public class News {
    public class Key{
        public static final String id = "id";
        public static final String title = "title";
        public static final String content = "content";
        public static final String publisher = "publisher";
        public static final String pub_time = "pub_time";
    }
    public int id;
    public String title;
    public String content;
    public String publisher;
    public long pub_time;
}
