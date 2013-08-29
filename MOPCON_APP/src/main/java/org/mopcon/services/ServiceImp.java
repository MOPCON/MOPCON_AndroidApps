package org.mopcon.services;

import org.mopcon.model.News;
import org.mopcon.model.Session;

import java.util.ArrayList;

/**
 * Created by chuck on 13/8/29.
 */
public interface ServiceImp {
    public boolean UpdateNews();
    public boolean UpdateSession();
    public ArrayList<News> getNews();
    public ArrayList<Session> getSession();
    public News getNews(int id);
    public Session getSession(int id);
}
