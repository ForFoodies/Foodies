package com.codepath.peterhe.foodies.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.codepath.peterhe.foodies.Group
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.YelpRestaurant
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseUser
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class DialogueFragment : Fragment() {
    private lateinit var restaurant: YelpRestaurant
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var timeText : TextView
    private lateinit var dateText : TextView
    private var month: Int = 0
    private var year: Int = 0
    private var day:Int =0
    private var date:String = ""
   private lateinit var datePickerDialog : DatePickerDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle("Create a new Group")
        view.setBackgroundColor(getResources().getColor(R.color.orange))
        val bundle = this.arguments
        if(bundle != null){
            restaurant = bundle.getParcelable<YelpRestaurant>("RestaurantDialog")!!
        }
        view.findViewById<TextView>(R.id.tvDialogRestaurantName).text = restaurant.name
        val address = "${restaurant.location.address1}, ${restaurant.location.city}, ${restaurant.location.state}, ${restaurant.location.country}, ${restaurant.location.zip_code}"
        view.findViewById<TextView>(R.id.tvDialogAddress).text = address
        timeText = view.findViewById<TextView>(R.id.tvChooseTime)
        dateText = view.findViewById<TextView>(R.id.tvChooseDate)
        view.findViewById<ImageButton>(R.id.btn_chooseTime).setOnClickListener {
            popTimePicker(view)
        }
        initDatePicker()
        view.findViewById<ImageButton>(R.id.btn_chooseDate).setOnClickListener {
            openDatePicker(view)
        }

        view.findViewById<ImageButton>(R.id.btn_cancel_dialog).setOnClickListener {
            val fm = getFragmentManager()
            fm?.popBackStack()
        }

        view.findViewById<ImageButton>(R.id.btn_confirm_dialog).setOnClickListener {
            view.findViewById<TextInputLayout>(R.id.text_input_layout_groupName_Dialog)?.setError(null)
            view.findViewById<TextInputLayout>(R.id.text_input_layout_groupName_Dialog)?.setErrorEnabled(false)
            view.findViewById<TextInputLayout>(R.id.text_input_layout_interest_Dialog)?.setError(null)
            view.findViewById<TextInputLayout>(R.id.text_input_layout_interest_Dialog)?.setErrorEnabled(false)
            view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setError(null)
            view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setErrorEnabled(false)
            view.findViewById<TextView>(R.id.tvChooseDate).setTextColor(getResources().getColor(R.color.white))
            view.findViewById<TextView>(R.id.tvChooseTime).setTextColor(getResources().getColor(R.color.white))
            val name = view.findViewById<EditText>(R.id.et_groupname_dialog).text.toString()
            val description = view.findViewById<EditText>(R.id.et_interests_dialog).text.toString()
            val number = view.findViewById<EditText>(R.id.et_number_dialog).text.toString()
            val time = view.findViewById<TextView>(R.id.tvChooseTime).text.toString()
            val date = view.findViewById<TextView>(R.id.tvChooseDate).text.toString()
            if (name != "" && description != "" && number != "" && number.toDouble()%1 == 0.0 && time != "Choose Time" && date != "Choose Date") {
                    val num = number.toInt()
                    val founder = ParseUser.getCurrentUser()
                    val memberList : JSONArray = JSONArray(listOf<ParseUser>(founder))
                    val restaurantId = restaurant.id
                    submitGroup(name,description,num,time,date,founder,memberList,restaurantId)
            } else {
                if (name == "") {
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_groupName_Dialog)?.setErrorEnabled(true)
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_groupName_Dialog)?.setError("Group name is required for registration.")
                }
                if (description == "") {
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_interest_Dialog)?.setErrorEnabled(true)
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_interest_Dialog)?.setError("A description is required for group registration.")
                }
                if (number == "") {
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setErrorEnabled(true)
                    view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setError("Please specify max number of members")
                } else {
                    if (number.toDouble()%1 != 0.0) {
                        view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setErrorEnabled(true)
                        view.findViewById<TextInputLayout>(R.id.text_input_layout_number_Dialog)?.setError("Please enter interger number")
                    }
                }
                if (date == "Choose Date") {
                    view.findViewById<TextView>(R.id.tvChooseDate).setTextColor(getResources().getColor(R.color.red))
                }
                if (time == "Choose Time") {
                    view.findViewById<TextView>(R.id.tvChooseTime).setTextColor(getResources().getColor(R.color.red))
                }
            }


        }

    }

    private fun submitGroup(
        name: String,
        description: String,
        number: Int,
        time: String,
        date: String,
        founder: ParseUser?,
        memberList: JSONArray,
        restaurantId: String
    ) {
        val group = Group()
        group.setName(name)
        group.setDescription(description)
        group.setMax(number)
        group.setTime(time)
        group.setDate(date)
        group.setFounder(founder!!)
        group.setMemberList(memberList)
        group.setRestaurant(restaurantId)
        group.saveInBackground { exception ->
            if (exception != null) {
                //Log.e(TAG, "ERROR submitting a post")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Error registering a group", Toast.LENGTH_SHORT).show()
            } else {
                //Log.i(TAG, "Successfully submitted a post")
                Toast.makeText(requireContext(), "Successfully registered a post", Toast.LENGTH_SHORT).show()
                view?.findViewById<EditText>(R.id.et_interests_dialog)?.text?.clear()
                view?.findViewById<EditText>(R.id.et_groupname_dialog)?.text?.clear()
                view?.findViewById<EditText>(R.id.et_number_dialog)?.text?.clear()

                val fm = getFragmentManager()
                fm?.popBackStack()
            }
        }
    }

    private fun popTimePicker(view: View) {
        val onTimeSetListner = OnTimeSetListener() { timePicker: TimePicker, i: Int, i1: Int ->
            hour = i
            minute = i1
            timeText.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute))
        }
        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(requireContext(),style,onTimeSetListner,hour,minute,true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }

    private fun initDatePicker() {
        val dateSetListner = DatePickerDialog.OnDateSetListener() {datePicker: DatePicker, i:Int, i1:Int, i2:Int ->
            date = makeDateString(i,i1+1,i2)
            dateText.setText(date)
        }
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(requireContext(),style,dateSetListner,year,month,day)
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis())
        datePickerDialog.setTitle("Select Date")
    }

    private fun makeDateString(i: Int, i1: Int, i2: Int): String {
        return "${getMonthFormat(i1)} $i2 $i"
    }

    private fun getMonthFormat(i1: Int): String {
        if (i1 == 1) {
            return "JAN"
        }
        if (i1 == 2) {
            return "FEB"
        }
        if (i1 == 3) {
            return "MAR"
        }
        if (i1 == 4) {
            return "APR"
        }
        if (i1 == 5) {
            return "MAY"
        }
        if (i1 == 6) {
            return "JUN"
        }
        if (i1 == 7) {
            return "JUL"
        }
        if (i1 == 8) {
            return "AUG"
        }
        if (i1 == 9) {
            return "SEP"
        }
        if (i1 == 10) {
            return "OCT"
        }
        if (i1 == 11) {
            return "NOV"
        }
        if (i1 == 12) {
            return "DEC"
        }
        return "JAN"
    }

    private fun openDatePicker(view: View) {
        datePickerDialog.show()
    }


}