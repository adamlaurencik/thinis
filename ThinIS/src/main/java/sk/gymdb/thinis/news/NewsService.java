package sk.gymdb.thinis.news;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class NewsService implements Parcelable {
    private static NewsService instance=null;
    private HashSet<NewsItem> news = new LinkedHashSet<NewsItem>();

    public static NewsService getInstance(){
        if(instance==null){
            instance=new NewsService();
        }
        return instance;
    }
    public NewsService(){}
    public NewsService (Parcel in){
        TypedArray data= new TypedArray();
       in.readTypedArray(data,CREATOR,);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray((Parcelable[]) news.toArray(),0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NewsService createFromParcel(Parcel in) {
            return new NewsService(in);
        }

        public NewsService[] newArray(int size) {
            return new NewsService[size];
        }
    };
}
