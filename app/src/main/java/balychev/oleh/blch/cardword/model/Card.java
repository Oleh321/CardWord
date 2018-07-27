package balychev.oleh.blch.cardword.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Card implements Parcelable{
    private Long id;
    private String frontSide;
    private String backSide;
    private String dateAdd;
    private Integer state;

    public Card(Long id, String frontSide, String backSide, String dateAdd, Integer state) {
        this.id = id;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.dateAdd = dateAdd;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }



    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", frontSide='" + frontSide + '\'' +
                ", backSide='" + backSide + '\'' +
                ", dateAdd='" + dateAdd + '\'' +
                ", state=" + state +
                '}';
    }

    protected Card(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        frontSide = in.readString();
        backSide = in.readString();
        dateAdd = in.readString();
        if (in.readByte() == 0) {
            state = null;
        } else {
            state = in.readInt();
        }
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(frontSide);
        dest.writeString(backSide);
        dest.writeString(dateAdd);
        if (state == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(state);
        }
    }
}

