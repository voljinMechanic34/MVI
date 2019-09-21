package com.example.mvi.ui.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mvi.R
import com.example.mvi.ui.ui.DataStateListener
import com.example.mvi.ui.ui.MainViewModel
import com.example.mvi.ui.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , DataStateListener{


    lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showMainFragment()
    }
    fun showMainFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment(),"MainFragment")
            .commit()
    }
    override fun onDataStateChange(datastate: DataState<*>?) {
        handleDataStateChange(datastate)
    }

    private fun handleDataStateChange(datastate: DataState<*>?) {
            datastate?.let{
                //handle loading

                showProgressBar(it.loading)

                //handle message
                it.message?.let{ event ->
                    event.getContentIfNotHandled()?.let{message->
                        showToast(message)
                    }

                }

            }
    }
    fun showToast(message : String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    fun showProgressBar(isVisible : Boolean){
        if (isVisible){
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}
