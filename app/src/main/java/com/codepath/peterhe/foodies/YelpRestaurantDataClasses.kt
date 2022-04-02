package com.codepath.peterhe.foodies
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class YelpSearchResult (
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val restaurants: List<YelpRestaurant>

)

data class YelpRestaurant(
    val name:String,
    val rating:Double,
    val price:String,
    val id:String,
    val is_closed:Boolean,
    val url:String,
    val transactions:List<String>,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl:String,
    val categories:List<YelpCategory>,
    val location: YelpLocation,
    val phone:String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.createTypedArrayList(YelpCategory)!!,
        parcel.readParcelable(YelpLocation::class.java.classLoader)!!,
        parcel.readString()!!
    ) {
    }

    fun displayDistance():String {
        val milesPerMeter = 0.000621371
        var distanceInMiles = ""
        if (distanceInMeters != null) {
            distanceInMiles = "%.2f".format(distanceInMeters*milesPerMeter)
        }
        return "$distanceInMiles mi"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(rating)
        parcel.writeString(price)
        parcel.writeString(id)
        parcel.writeByte(if (is_closed) 1 else 0)
        parcel.writeString(url)
        parcel.writeStringList(transactions)
        parcel.writeInt(numReviews)
        parcel.writeDouble(distanceInMeters)
        parcel.writeString(imageUrl)
        parcel.writeTypedList(categories)
        parcel.writeParcelable(location, flags)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YelpRestaurant> {
        override fun createFromParcel(parcel: Parcel): YelpRestaurant {
            return YelpRestaurant(parcel)
        }

        override fun newArray(size: Int): Array<YelpRestaurant?> {
            return arrayOfNulls(size)
        }
    }
}

data class YelpCategory (
    val title:String
    ) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YelpCategory> {
        override fun createFromParcel(parcel: Parcel): YelpCategory {
            return YelpCategory(parcel)
        }

        override fun newArray(size: Int): Array<YelpCategory?> {
            return arrayOfNulls(size)
        }
    }
}

data class YelpLocation(
    val address1:String,
    val city:String,
    val state:String,
    val country:String,
    val zip_code:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address1)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeString(zip_code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YelpLocation> {
        override fun createFromParcel(parcel: Parcel): YelpLocation {
            return YelpLocation(parcel)
        }

        override fun newArray(size: Int): Array<YelpLocation?> {
            return arrayOfNulls(size)
        }
    }
}

