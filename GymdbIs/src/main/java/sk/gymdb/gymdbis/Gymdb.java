package sk.gymdb.gymdbis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by Admin on 10/26/13.
 */
    public class Gymdb {
        private HashSet<Notice> news= new LinkedHashSet<Notice>();

        public Gymdb(){

        }
        public void addNotice(Notice notice){
            news.add(notice);
        }
        public LinkedHashSet<Notice> getNews(){


           return (LinkedHashSet<Notice>) news;
        }
        public void setNews(LinkedHashSet<Notice> notices) {
            news.addAll(notices);

        }
        public Notice getNoticeById(int i){
            Iterator<Notice> it = news.iterator();
            Notice ret = it.next();
            for (int j = 0; j < i; j++) {
                ret=it.next();
            }
            return ret;
        }

    }
