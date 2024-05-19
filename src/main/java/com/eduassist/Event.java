package com.eduassist;


public class Event {

    private final int code;
    private String title;
    private String info;
    private String stringDate;
    private float[] date;
    private String eventColor;
    private String textColor;

    public Event(int code, String title, String info, String stringDate, String eventColor, String textColor) {

        this.code = code;
        this.title = title;
        this.info = info;
        this.eventColor = eventColor;
        this.textColor = textColor;
        this.stringDate = stringDate;

        this.date = transformDate(stringDate);
    }

    private float[] transformDate(String txt) {

        String[] list = txt.split(" ");
        float[] date = new float[list.length];

        for (int i = 0; i < list.length; i++) {
            date[i] = Float.parseFloat(list[i]);
        }

        return date;
    }

    public void updateInfo(String title, String info, String stringDate) {
        this.title = title;
        this.info = info;
        this.stringDate = stringDate;
        this.date = transformDate(stringDate);
    }

    public void setColors(String eventColor, String textColor) {
        this.eventColor = eventColor;
        this.textColor = textColor;
        DataBase.updateColor(Integer.toString(code), eventColor, textColor, false);
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public float[] getDate() {
        return date;
    }

    public String getEventColor() {
        return eventColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public Object getStringDate() {
        return stringDate;
    }
}
