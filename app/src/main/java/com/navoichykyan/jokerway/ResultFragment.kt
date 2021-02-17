package com.navoichykyan.jokerway

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.win_fragment.*

class ResultFragment() : Fragment(){
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        var layout = R.layout.win_fragment
        if(arguments!!.getInt("result") == 1) layout = R.layout.lose_fragment
        return inflater.inflate(layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val eventInterface: EventInterface = activity as MainActivity
        replay.setOnClickListener{
            eventInterface.replayFunction()
            activity!!.onBackPressed()
        }
    }
}