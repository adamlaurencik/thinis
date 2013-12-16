package sk.gymdb.thinis.model.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import sk.gymdb.thinis.model.pojo.NewsItem;

public class NewsDAO implements Parcelable {

    private static NewsDAO instance = null;
    private List<NewsItem> news = new ArrayList<NewsItem>();

    public static NewsDAO getInstance() {
        if (instance == null) {
            instance = new NewsDAO();
        }
        return instance;
    }

    public List<NewsItem> getNews() {
        return news;
    }

    public void setNews(ArrayList<NewsItem> notices) {
        news.addAll(notices);
    }

    public NewsItem getNewsItemById(int i) {
        return this.news.get(i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray((Parcelable[]) news.toArray(), 0);
    }

    public static final Creator CREATOR = new Creator() {

        public NewsDAO createFromParcel(Parcel in) {
            return new NewsDAO();
        }

        public NewsDAO[] newArray(int size) {
            return new NewsDAO[size];
        }

    };
}
