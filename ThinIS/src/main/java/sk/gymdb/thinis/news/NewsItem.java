package sk.gymdb.thinis.news;

/**
 * Created by Admin on 10/26/13.
 */
import android.text.Html;
import android.text.Spanned;

import java.util.HashSet;

public class NewsItem {
    private String message;
    private String title;
    private HashSet<String> images= new HashSet<String>();
    public NewsItem(){

    }
    public void setMessage(String message){
        this.message=message;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void addImage(String image){
        images.add(image);
    }
    public String getMessage(){
        return this.message;
    }
    public String getTitle(){
        return this.title;
    }
    public Spanned getHtmlString(){
        return Html.fromHtml("<b>"+title+"</b>");
    }

}
