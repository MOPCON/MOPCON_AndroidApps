package org.mopcon.services;

import org.mopcon.model.News;
import org.mopcon.model.Session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 13/8/29.
 */
public interface ServiceImp {
    public boolean updateNews();
    public boolean updateSession();
    public HashMap<Integer, News> getNews();
    public HashMap<Integer, Session> getSession();
    public News getNews(int id);
    public Session getSession(int id);
}
