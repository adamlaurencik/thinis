package sk.gymdb.thinis.model.pojo;

/**
 * Created by Admin on 10/26/13.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

public class NewsItem implements Parcelable {
    private String message;
    private String title;
    private String url;
    public NewsItem(){
    }
    public NewsItem(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.message = data[0];
        this.title = data[1];
        this.url = data[2];
    };
    public void setUrl(String url){
        this.url=url;
    }
    public String getUrl()
    {
        return this.url;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public void setTitle(String title){
        this.title=title;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.message,
                this.title,
                this.url});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}
