package com.example.mvi.ui.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mvi.R
import com.example.mvi.ui.models.BlogPost
import com.example.mvi.ui.models.User
import com.example.mvi.ui.ui.DataStateListener
import com.example.mvi.ui.ui.MainViewModel
import com.example.mvi.ui.ui.main.state.MainStateEvent.*
import com.example.mvi.ui.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException

class MainFragment : Fragment(),BlogListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG : CLICKED $position")
        println("DEBUG : CLICKED $item")
    }

    lateinit var viewModel : MainViewModel
    lateinit var dataStateHandler: DataStateListener
    lateinit var blogListAdapter: BlogListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel  = activity?.run{
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid activity")

        subcribeObservers()
        initRecyclerView()
    }
    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    fun subcribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            dataState ->
                println("DEBUG  : DataState : ${dataState}")
            //handle loading and message
            dataStateHandler.onDataStateChange(dataState)
            // handle data<T>
                dataState.data?.let {event ->
                    event.getContentIfNotHandled()?.let{ mainViewState ->
                        mainViewState.blogsPosts?.let { blogPosts ->
                            //set blog posts
                            viewModel.setBlogListData(blogPosts)
                        }
                        mainViewState.user?.let {user ->
                            //set user data
                            viewModel.setUser(user)
                        }
                    }

                }

        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState->
            viewState.blogsPosts?.let {list ->
                println("DEBUG  : setting blog posts to recyclerview : ${list}")
                blogListAdapter.submitList(list)
            }
            viewState.user?.let {
                println("DEBUG  : setting user data  to recyclerview : ${it}")
                setUserProperties(it)
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setUserProperties(user : User){
            email.text = user.email
            username.text = user.username

            view?.let{
                Glide.with(it.context)
                    .load(user.image)
                    .into(image)
            }
    }
    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            dataStateHandler = context as DataStateListener
        } catch (e : ClassCastException){
            println("debug : $context must implement dataStateListener")
        }
    }
}