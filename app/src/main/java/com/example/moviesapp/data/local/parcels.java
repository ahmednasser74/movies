package com.example.moviesapp.data.local;

import android.os.Parcel;
import android.os.Parcelable;

public class parcels implements Parcelable {
    private int id;
    private String value;

    // Constructor
    public parcels(int id, String value){
        this.id = id;
        this.value= value;
    }

    // Parcelling part
    public parcels(Parcel in){
        this.id = in.readInt();
        this.value= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(value);
    }

    public final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public parcels createFromParcel(Parcel in) {
            return new parcels(in);
        }

        public parcels[] newArray(int size) {
            return new parcels[size];
        }
    };
}

