package com.app.framework.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Place(parcel);
        }

        @Override
        public Object[] newArray(int size) {
            return new Place[size];
        }
    };

    public String placeId;
    public String description;
    public String primaryText;
    public String secondaryText;

    public Place(Parcel parcel) {
        placeId = parcel.readString();
        description = parcel.readString();
        primaryText = parcel.readString();
        secondaryText = parcel.readString();
    }

    public Place() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(placeId);
        parcel.writeString(description);
        parcel.writeString(primaryText);
        parcel.writeString(secondaryText);
    }
}
