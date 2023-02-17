package com.service.scanner;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceResponseModel implements Parcelable {

    int errorCode;
    String errorString;
    FingerPrintModel fingerPrintModel;

    public ServiceResponseModel(int errorCode, String errorString) {
        super();
        this.errorCode = errorCode;
        this.errorString = errorString;
    }

    public ServiceResponseModel(int errorCode, String errorString,
                                FingerPrintModel fingerPrintModel) {
        super();
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.fingerPrintModel = fingerPrintModel;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorString() {
        return errorString;
    }

    public FingerPrintModel getFingerPrintModel() {
        return fingerPrintModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errorCode);
        dest.writeString(errorString);
        dest.writeParcelable(fingerPrintModel, flags);
    }

    // Creator
    public static final Creator<ServiceResponseModel> CREATOR = new Creator<ServiceResponseModel>() {
        public ServiceResponseModel createFromParcel(Parcel in) {
            return new ServiceResponseModel(in);
        }

        public ServiceResponseModel[] newArray(int size) {
            return new ServiceResponseModel[size];
        }
    };

    // "De-parcel object
    public ServiceResponseModel(Parcel in) {
        errorCode = in.readInt();
        errorString = in.readString();
        fingerPrintModel = in.readParcelable(FingerPrintModel.class
                .getClassLoader());
    }

    @Override
    public String toString() {
        return "ServiceResponseModel [errorCode=" + errorCode
                + ", errorString=" + errorString + ", fingerPrintModel="
                + fingerPrintModel.toString() + "]";
    }

}
