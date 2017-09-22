package com.app.framework.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Address(parcel);
        }

        @Override
        public Object[] newArray(int size) {
            return new Address[size];
        }
    };

    public String placeId;
    public String addressLine1;
    public String addressLine2;
    public String streetNumber;
    public String city;
    public String state;
    public String stateCode;
    public String postalCode;
    public String country;
    public String countryCode;
    public String formattedAddress;
    public double latitude;
    public double longitude;

    public Address(Parcel parcel) {
        placeId = parcel.readString();
        addressLine1 = parcel.readString();
        addressLine2 = parcel.readString();
        streetNumber = parcel.readString();
        city = parcel.readString();
        state = parcel.readString();
        stateCode = parcel.readString();
        postalCode = parcel.readString();
        country = parcel.readString();
        countryCode = parcel.readString();
        formattedAddress = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
    }

    public Address() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(placeId);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeString(streetNumber);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(stateCode);
        parcel.writeString(postalCode);
        parcel.writeString(country);
        parcel.writeString(countryCode);
        parcel.writeString(formattedAddress);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
