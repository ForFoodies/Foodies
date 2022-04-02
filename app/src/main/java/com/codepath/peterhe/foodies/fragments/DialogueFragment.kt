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
import com.codepath.peterhe.foodies.R
import com.codepath.peterhe.foodies.YelpRestaurant
import java.util.*

class DialogueFragment : Fragment() {
    private lateinit var restaurant: YelpRestaurant
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var timeText : TextView
    private lateinit var dateText : TextView
    private var month: Int = 0
    private var year: Int = 0
    private var day:Int =0
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
            val date = makeDateString(i,i1+1,i2)
            dateText.setText(date)
        }
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(requireContext(),style,dateSetListner,year,month,day)
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