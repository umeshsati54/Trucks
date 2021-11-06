package com.usati.trucks.ui

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.usati.trucks.R
import com.usati.trucks.adapter.TruckAdapter
import com.usati.trucks.databinding.FragmentListBinding
import com.usati.trucks.models.Data
import com.usati.trucks.utils.Resource
import com.usati.trucks.viewmodel.TrucksViewModel

class ListFragment : Fragment(R.layout.fragment_list) {
    lateinit var viewModel: TrucksViewModel
    lateinit var truckAdapter: TruckAdapter
    lateinit var binding: FragmentListBinding
    private var dataList: ArrayList<Data>? = arrayListOf()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var searchView: SearchView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        viewModel.trucks.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { trucksResponse ->
                        dataList?.addAll(trucksResponse?.data!!)
                    }
                    setupRecyclerView()
                    //truckAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    private fun hideProgressBar() {
        binding.pb.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.pb.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false

    private fun setupRecyclerView() {
        truckAdapter = TruckAdapter(dataList!!)
        binding.rv.apply {
            adapter = truckAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu)

        val searchManager =
            activity?.application?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(requireActivity().componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                truckAdapter.filter.filter(query)
                return false
            }
        })
        setupRecyclerView()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_map -> {
                findNavController().navigate(R.id.action_listFragment_to_mapsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}