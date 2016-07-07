package co.kogi.chronometerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 1/07/2016.
 */
public class Lap implements Parcelable {

    private String days;
    private String hours;
    private String mins;
    private String seconds;
    private String millis;

    public Lap(String days, String hours, String mins, String seconds, String millis) {
        this.days = days;
        this.hours = hours;
        this.mins = mins;
        this.seconds = seconds;
        this.millis = millis;
    }


    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMins() {
        return mins;
    }

    public void setMins(String mins) {
        this.mins = mins;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.days);
        dest.writeString(this.hours);
        dest.writeString(this.mins);
        dest.writeString(this.seconds);
        dest.writeString(this.millis);
    }

    protected Lap(Parcel in) {
        this.days = in.readString();
        this.hours = in.readString();
        this.mins = in.readString();
        this.seconds = in.readString();
        this.millis = in.readString();
    }

    public static final Parcelable.Creator<Lap> CREATOR = new Parcelable.Creator<Lap>() {
        @Override
        public Lap createFromParcel(Parcel source) {
            return new Lap(source);
        }

        @Override
        public Lap[] newArray(int size) {
            return new Lap[size];
        }
    };
}
