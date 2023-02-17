package com.service.scanner;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class FingerPrintModel implements Parcelable {

	String templateType;
	int templateSize, quality;
	byte[] template;
	Bitmap fingerPrintImage;

	public FingerPrintModel(String templateType, int templateSize, int quality,
                            byte[] template, Bitmap fingerPrintImage) {
		super();
		this.templateType = templateType;
		this.templateSize = templateSize;
		this.quality = quality;
		this.template = Arrays.copyOf(template, templateSize);
		this.fingerPrintImage = fingerPrintImage;
	}

	public String getTemplateType() {
		return templateType;
	}

	public int getTemplateSize() {
		return templateSize;
	}

	public byte[] getTemplate() {
		return template;
	}

	public Bitmap getFingerPrintImage() {
		return fingerPrintImage;
	}

	public int getQuality() {
		return quality;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(templateType);
		dest.writeInt(templateSize);
		dest.writeInt(quality);
		dest.writeByteArray(template);
		dest.writeParcelable(fingerPrintImage, flags);
	}

	// Creator
	public static final Creator<FingerPrintModel> CREATOR = new Creator<FingerPrintModel>() {
		public FingerPrintModel createFromParcel(Parcel in) {
			return new FingerPrintModel(in);
		}

		public FingerPrintModel[] newArray(int size) {
			return new FingerPrintModel[size];
		}
	};

	// "De-parcel object
	public FingerPrintModel(Parcel in) {
		templateType = in.readString();
		templateSize = in.readInt();
		quality = in.readInt();
		template = new byte[templateSize];
		in.readByteArray(template);
		fingerPrintImage = (Bitmap) in.readParcelable(getClass()
				.getClassLoader());
	}

	@Override
	public String toString() {
		return "FingerPrintModel [templateType=" + templateType
				+ ", templateSize=" + templateSize + ", quality=" + quality
				+ ", template=" + Arrays.toString(template) + "]";
	}

}
