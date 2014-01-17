/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.gymdb.thinis.gcm.substitutions;

import java.util.List;

/**
 * @author Admin
 */
public class Substitution {

    private List<String> who;
    private String hour;
    private String subject;
    private String teacher;
    private String comment;
    private Boolean odpadne;

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setSubject(String subject) {
        if (subject.contains(Character.toString((char) 10132))) {
            int charIndex = subject.indexOf(Character.toString((char) 10132));
            subject = subject.substring(charIndex, subject.length() - 1);
        }
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        if (teacher.contains(Character.toString((char) 10132))) {
            int charIndex = teacher.indexOf(Character.toString((char) 10132));
            teacher = teacher.substring(charIndex, teacher.length() - 1);
        }
        this.teacher = teacher;
    }

    public void setWho(List<String> who) {
        this.who = who;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHour() {
        return hour;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getWho() {
        return who;
    }

    public String getComment() {
        return comment;
    }

    public void setOdpada(Boolean odpada) {
        this.odpadne = odpada;
    }


    @Override
    public String toString() {
        String out = "";
        if (odpadne) {
            out = who + "     " + hour + "    " + subject + "     " + teacher + "     " + comment + "- ODPADNE!<br>";
        } else {
            out = who + "     " + hour + "    " + subject + "     " + teacher + "     " + comment + "<br>";
        }
        return out;
    }


}
