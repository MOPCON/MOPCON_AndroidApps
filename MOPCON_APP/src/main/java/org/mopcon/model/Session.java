package org.mopcon.model;

/**
 * Created by chuck on 13/8/29.
 */
public class Session {
    public class Key{
        public static final String id = "id";
        public static final String name = "name";
        public static final String speaker = "speaker";
        public static final String speaker_bio = "speaker_bio";
        public static final String keywork = "keywork";
        public static final String loc = "loc";
        public static final String start_time = "start_time";
        public static final String end_time = "end_time";
    }
    public int id;
    public String name;
    public String speaker;
    public String speaker_bio;
    public String keyword;
    public String loc;
    public long start_time;
    public long end_time;
}
