package com.reputasi.library.database.record;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vikraa on 8/1/2015.
 */
public class SearchResultItem implements Parcelable {

    private String mCategory;
    private String mPhoneNumber;
    private String mOwnerName;
    private String mOwnerAddress;
    private String mScore;
    private String mThumbUpScore;
    private String mThumbDownScore;

    public SearchResultItem() {

    }

    public SearchResultItem(Parcel in) {
        readFromParcel(in);
    }

    public String getCategory() {
        return mCategory.isEmpty() ? "Unknown" : mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = (category == null ? "Unknown" : category);
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = (phoneNumber == null ? "Unknown" : phoneNumber);
    }

    public String getOwnerName() {
        return mOwnerName.isEmpty() ? "Unknown" : mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        this.mOwnerName = ownerName == null ? "Unknown" : ownerName;
    }

    public String getOwnerAddress() {
        return mOwnerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.mOwnerAddress = ownerAddress == null ? "Unknown" : ownerAddress;
    }

    public String getScore() {
        return mScore;
    }

    public void setScore(String score) {
        this.mScore = score;
    }

    public String getThumbUpScore() {
        return mThumbUpScore;
    }

    public void setThumbUpScore(String thumbUpScore) {
        this.mThumbUpScore = thumbUpScore;
    }

    public String getThumbDownScore() {
        return mThumbDownScore;
    }

    public void setThumbDownScore(String thumbDownScore) {
        this.mThumbDownScore = thumbDownScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategory);
        dest.writeString(mPhoneNumber);
        dest.writeString(mOwnerAddress);
        dest.writeString(mOwnerName);
        dest.writeString(mScore);
        dest.writeString(mThumbUpScore);
        dest.writeString(mThumbDownScore);
    }

    private void readFromParcel(Parcel in) {
        mCategory = in.readString();
        mPhoneNumber = in.readString();
        mOwnerAddress = in.readString();
        mOwnerName = in.readString();
        mScore = in.readString();
        mThumbUpScore = in.readString();
        mThumbDownScore = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new SearchResultItem(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };
}
