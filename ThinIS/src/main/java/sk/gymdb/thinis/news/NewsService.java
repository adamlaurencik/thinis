package sk.gymdb.thinis.news;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class NewsService {
    private static NewsService instance=null;
    private HashSet<NewsItem> news = new LinkedHashSet<NewsItem>();

    public static NewsService getInstance(){
        if(instance==null){
            instance=new NewsService();
        }
        return instance;
    }

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
        NewsItem ret=new NewsItem();
       if(it.hasNext()) {
            ret = it.next();

        for (int j = 0; j < i; j++) {
            ret = it.next();
        }
       }
        return ret;
    }

}
