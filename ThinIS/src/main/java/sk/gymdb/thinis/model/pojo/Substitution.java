/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.gymdb.thinis.model.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Admin
 */
public class Substitution {

    private List<String> who;
    private String hour;
    private String subject;
    private String teacher;
    private String comment;
    private String clazz;
    private Boolean canceled;
    private Boolean teacherChange;
    private Boolean subjectChange;
    private Boolean clazzChange;
    private Date date;
    private int ID;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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
        this.canceled = odpada;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public String getClazz() {
        return clazz;
    }

    public Boolean isCanceled() {
        return canceled;
    }

    public Boolean isClazzChange() {
        return clazzChange;
    }

    public Boolean isSubjectChange() {
        return subjectChange;
    }

    public Boolean isTeacherChange() {
        return teacherChange;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setClazzChange(Boolean clazzChange) {
        this.clazzChange = clazzChange;
    }

    public void setSubjectChange(Boolean subjectChange) {
        this.subjectChange = subjectChange;
    }

    public void setTeacherChange(Boolean teacherChange) {
        this.teacherChange = teacherChange;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("d. M.");
        String dateString= dateFormat.format(date);
        String dayOfWeek= new SimpleDateFormat("EEEE",Locale.getDefault()).format(date);
        dateString= dateString+ " ("+dayOfWeek+")";
        String out = "Dňa "+ dateString ;
        if (isCanceled()) {
            out= out+" ti odpadne "+ getHour()+". hodina ("+ getSubject()+")";
        } else if(isTeacherChange() && isClazzChange() && isSubjectChange()){
            out= out+" máš "+ getHour()+". hodinu  "+getSubject()+" s učiteľom "+getTeacher()+" v učebni "+getClazz();
        }else if(isTeacherChange() && isClazzChange()){
            out= out+" máš "+ getHour()+". hodinu  "+getSubject()+" s učiteľom "+getTeacher()+" v učebni "+getClazz();
        } else if(isSubjectChange()) {
            out= out+" máš "+ getHour()+". hodinu predmet "+getSubject()+" s učiteľom "+getTeacher();
        } else if(isClazzChange()){
            out= out+" máš "+ getHour()+". hodinu v učebni "+getClazz();
        } else if(isTeacherChange()){
            out= out+" máš "+ getHour()+". hodinu "+getSubject()+" s učiteľom "+getTeacher();
        }
        else out= out+" máš "+ getHour()+". hodinu  "+getSubject()+" s učiteľom "+getTeacher()+" v učebni "+getClazz();
        if(getComment()!="") out= out+" Poznámka: " + comment;
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Substitution)) return false;

        Substitution that = (Substitution) o;

        if (!hour.equals(that.hour)) return false;
        if (canceled != null ? !canceled.equals(that.canceled) : that.canceled != null) return false;
        if (!subject.equals(that.subject)) return false;
        if (!teacher.equals(that.teacher)) return false;

        if (!who.equals(that.who)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = who.hashCode();
        result = 31 * result + hour.hashCode();
        result = 31 * result + subject.hashCode();
        result = 31 * result + teacher.hashCode();
        result = 31 * result + (canceled != null ? canceled.hashCode() : 0);
        return result;
    }

    
}
