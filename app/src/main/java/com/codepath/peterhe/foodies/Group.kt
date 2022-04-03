package com.codepath.peterhe.foodies

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import org.json.JSONArray

@ParseClassName("Group")
class Group:ParseObject() {
    fun getDescription():String? {
        return getString(DESCRIPTION_KEY)
    }
    fun setDescription(description:String) {
        put(DESCRIPTION_KEY,description)
    }
    fun getName(): String? {
        return getString(NAME_KEY)
    }
    fun setName(username:String) {
        put(NAME_KEY,username)
    }
   /* fun getUser():ParseUser? {
        return getParseUser()
    }*/
   fun getRestaurant(): String? {
       return getString(RESTAURANT_KEY)
   }
    fun setRestaurant(restaurant:String) {
        put(RESTAURANT_KEY,restaurant)
    }
    fun getTime(): String? {
        return getString(TIME_KEY)
    }
    fun setTime(time:String) {
        put(TIME_KEY,time)
    }
    fun getDate(): String? {
        return getString(DATE_KEY)
    }
    fun setDate(date:String) {
        put(DATE_KEY,date)
    }

    fun getMax(): Int? {
        return getInt(MAX_KEY)
    }
    fun setMax(max:Int) {
        put(MAX_KEY,max)
    }

    fun getCurrent(): Int? {
        return getInt(CUR_KEY)
    }
    fun setCurrent(cur:Int) {
        put(CUR_KEY,cur)
    }

    fun getFounder(): ParseUser? {
        return getParseUser(FOUNDER_KEY)
    }
    fun setFounder(founder:ParseUser) {
        put(FOUNDER_KEY,founder)
    }
    fun getMemberList(): JSONArray? {
        return getJSONArray(MEMBERLIST_KEY)
    }
    fun setMemberList(memberList:JSONArray) {
        return put(MEMBERLIST_KEY,memberList)
    }

    companion object {
        const val DESCRIPTION_KEY = "description"
        //const val PROFILE_KEY = "profile"
        const val NAME_KEY = "name"
        const val RESTAURANT_KEY = "restaurantID"
        const val TIME_KEY = "Time"
        const val DATE_KEY = "Date"
        const val MAX_KEY = "maxMember"
        const val CUR_KEY = "curMember"
        const val FOUNDER_KEY = "FounderID"
        const val MEMBERLIST_KEY = "memberList"
        //const val OBJECTID_KEY = "objectId"
    }
}