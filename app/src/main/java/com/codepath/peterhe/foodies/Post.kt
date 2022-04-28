package com.codepath.peterhe.foodies

import android.os.Parcel
import android.os.Parcelable
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post() : ParseObject() {
    constructor(parcel: Parcel) : this() {
    }

    fun getCaption():String? {
        return getString("Caption")
    }
    fun setCaption(caption: String) {
       put("Caption",caption)
    }
    fun getImage(): ParseFile? {
        return getParseFile("Image")
    }
    fun setImage(image: ParseFile) {
        put("Image",image)
    }
    fun getUserId():ParseUser? {
        return getParseUser("userId")
    }
    fun setUserId(user: ParseUser) {
        put("userId",user)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}