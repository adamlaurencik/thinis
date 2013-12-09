package sk.gymdb.thinis.news;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by Admin on 10/26/13.
 */
public class NewsService {

    private HashSet<NewsItem> news = new LinkedHashSet<NewsItem>();

    public void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

    public LinkedHashSet<NewsItem> getNews() {
        return (LinkedHashSet<NewsItem>) news;
    }

    public void setNews(LinkedHashSet<NewsItem> notices) {
        news.addAll(notices);
    }

    public NewsItem getNewsItemById(int i) {
        Iterator<NewsItem> it = news.iterator();
        NewsItem ret = it.next();
        for (int j = 0; j < i; j++) {
            ret = it.next();
        }
        return ret;
    }

}
