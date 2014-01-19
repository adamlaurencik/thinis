package sk.gymdb.thinis.model.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 16.1.2014
 * Time: 20:49
 */
public class UserInfo {

    private String name;
    private HashMap<String, ArrayList<String>> evaluation = new HashMap<String, ArrayList<String>>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, ArrayList<String>> getEvaluation() {
        return evaluation;
    }

    public void addEvaluation(String subject, ArrayList<String> grades) {
        this.evaluation.put(subject, grades);
    }
}
