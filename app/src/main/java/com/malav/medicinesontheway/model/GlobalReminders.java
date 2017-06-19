package com.malav.medicinesontheway.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shahmalav on 26/04/17.
 */

public class GlobalReminders implements Parcelable{

    private String reminderId;
    private String title;
    private String message;
    private String startDate;
    private String endDate;
    private String startTime;
    private Boolean isMondaySelected;
    private Boolean isTuesdaySelected;
    private Boolean isWednesdaySelected;
    private Boolean isThursdaySelected;
    private Boolean isFridaySelected;
    private Boolean isSaturdaySelected;
    private Boolean isSundaySelected;
    private Boolean isEveryHourSelected;
    private Boolean isEveryDaySelected;
    private Boolean isEveryWeekSelected;
    private Boolean isEveryMonthSelected;
    private Boolean isEveryYearSelected;
    private String dateAndTime;
    private Boolean isForeverState;
    private int numberToShow;
    private int interval;
    private Boolean isRepetition;

    public GlobalReminders() {

    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getMondaySelected() {
        return isMondaySelected;
    }

    public void setMondaySelected(Boolean mondaySelected) {
        isMondaySelected = mondaySelected;
    }

    public Boolean getTuesdaySelected() {
        return isTuesdaySelected;
    }

    public void setTuesdaySelected(Boolean tuesdaySelected) {
        isTuesdaySelected = tuesdaySelected;
    }

    public Boolean getWednesdaySelected() {
        return isWednesdaySelected;
    }

    public void setWednesdaySelected(Boolean wednesdaySelected) {
        isWednesdaySelected = wednesdaySelected;
    }

    public Boolean getThursdaySelected() {
        return isThursdaySelected;
    }

    public void setThursdaySelected(Boolean thursdaySelected) {
        isThursdaySelected = thursdaySelected;
    }

    public Boolean getFridaySelected() {
        return isFridaySelected;
    }

    public void setFridaySelected(Boolean fridaySelected) {
        isFridaySelected = fridaySelected;
    }

    public Boolean getSaturdaySelected() {
        return isSaturdaySelected;
    }

    public void setSaturdaySelected(Boolean saturdaySelected) {
        isSaturdaySelected = saturdaySelected;
    }

    public Boolean getSundaySelected() {
        return isSundaySelected;
    }

    public void setSundaySelected(Boolean sundaySelected) {
        isSundaySelected = sundaySelected;
    }

    public Boolean getEveryHourSelected() {
        return isEveryHourSelected;
    }

    public void setEveryHourSelected(Boolean everyHourSelected) {
        isEveryHourSelected = everyHourSelected;
    }

    public Boolean getEveryDaySelected() {
        return isEveryDaySelected;
    }

    public void setEveryDaySelected(Boolean everyDaySelected) {
        isEveryDaySelected = everyDaySelected;
    }

    public Boolean getEveryWeekSelected() {
        return isEveryWeekSelected;
    }

    public void setEveryWeekSelected(Boolean everyWeekSelected) {
        isEveryWeekSelected = everyWeekSelected;
    }

    public Boolean getEveryMonthSelected() {
        return isEveryMonthSelected;
    }

    public void setEveryMonthSelected(Boolean everyMonthSelected) {
        isEveryMonthSelected = everyMonthSelected;
    }

    public Boolean getEveryYearSelected() {
        return isEveryYearSelected;
    }

    public void setEveryYearSelected(Boolean everyYearSelected) {
        isEveryYearSelected = everyYearSelected;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDate() {
        return dateAndTime.substring(0, 8);
    }

    public Boolean getForeverState() {
        return isForeverState;
    }

    public void setForeverState(Boolean foreverState) {
        isForeverState = foreverState;
    }

    public int getNumberToShow() {
        return numberToShow;
    }

    public void setNumberToShow(int numberToShow) {
        this.numberToShow = numberToShow;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Boolean getRepetition() { return isRepetition; }

    public void setRepetition(Boolean repetition) { isRepetition = repetition; }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reminderId);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(startTime);
        dest.writeInt((isMondaySelected ? 1 : 0));
        dest.writeInt((isTuesdaySelected ? 1 : 0));
        dest.writeInt((isWednesdaySelected ? 1 : 0));
        dest.writeInt((isThursdaySelected ? 1 : 0));
        dest.writeInt((isFridaySelected ? 1 : 0));
        dest.writeInt((isSaturdaySelected ? 1 : 0));
        dest.writeInt((isSundaySelected ? 1 : 0));
        dest.writeInt((isEveryHourSelected ? 1 : 0));
        dest.writeInt((isEveryDaySelected ? 1 : 0));
        dest.writeInt((isEveryWeekSelected ? 1 : 0));
        dest.writeInt((isEveryMonthSelected ? 1 : 0));
        dest.writeInt((isEveryYearSelected ? 1 : 0));
        dest.writeString(dateAndTime);
        dest.writeInt((isForeverState ? 1 : 0));
        dest.writeInt(numberToShow);
        dest.writeInt(interval);
        if(isRepetition!=null) {
            dest.writeInt((isRepetition ? 1 : 0));
        }else{
            dest.writeInt(0);
        }

    }

    // Creator
    public static final Creator CREATOR = new Creator() {
        public GlobalReminders createFromParcel(Parcel in) {
            return new GlobalReminders(in);
        }

        public GlobalReminders[] newArray(int size) {
            return new GlobalReminders[size];
        }
    };

    // "De-parcel object
    public GlobalReminders(Parcel in) {

        reminderId = in.readString();
        title = in.readString();
        message = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        startTime = in.readString();
        isMondaySelected = in.readInt() != 0;
        isTuesdaySelected = in.readInt() != 0;
        isWednesdaySelected = in.readInt() != 0;
        isThursdaySelected = in.readInt() != 0;
        isFridaySelected = in.readInt() != 0;
        isSaturdaySelected = in.readInt() != 0;
        isSundaySelected = in.readInt() != 0;
        isEveryHourSelected = in.readInt() != 0;
        isEveryDaySelected = in.readInt() != 0;
        isEveryWeekSelected = in.readInt() != 0;
        isEveryMonthSelected = in.readInt() != 0;
        isEveryYearSelected = in.readInt() != 0;
        dateAndTime = in.readString();
        isForeverState = in.readInt() != 0;
        numberToShow = in.readInt();
        interval = in.readInt();
        isRepetition = in.readInt() != 0;
    }

    @Override
    public String toString() {
        return "GlobalReminders{" +
                "reminderId='" + reminderId + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", isMondaySelected=" + isMondaySelected +
                ", isTuesdaySelected=" + isTuesdaySelected +
                ", isWednesdaySelected=" + isWednesdaySelected +
                ", isThursdaySelected=" + isThursdaySelected +
                ", isFridaySelected=" + isFridaySelected +
                ", isSaturdaySelected=" + isSaturdaySelected +
                ", isSundaySelected=" + isSundaySelected +
                ", isEveryHourSelected=" + isEveryHourSelected +
                ", isEveryDaySelected=" + isEveryDaySelected +
                ", isEveryWeekSelected=" + isEveryWeekSelected +
                ", isEveryMonthSelected=" + isEveryMonthSelected +
                ", isEveryYearSelected=" + isEveryYearSelected +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", isForeverState=" + isForeverState +
                ", numberToShow=" + numberToShow +
                ", interval=" + interval +
                ", isRepetition=" + isRepetition +
                '}';
    }
}
