package com.luong.note;

import java.text.DecimalFormat;

public class NoteItem {
    private Integer id;
    private String place;
    private String start_day;
    private String end_day;
    private Integer cost;
    private Integer number_member;

    public NoteItem(String place, String start_day, String end_day, Integer number_member, Integer cost, Integer id) {
        this.place = place;
        this.start_day = start_day;
        this.end_day = end_day;
        this.number_member = number_member;
        this.cost = cost;
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public String getCost() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        String numberAsString = decimalFormat.format(cost*1000);
        return numberAsString + " đ" + "/" + number_member.toString() + " ng";
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getNumber_member() {
        return number_member;
    }

    public void setNumber_member(Integer number_member) {
        this.number_member = number_member;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
