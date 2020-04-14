package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingwithmitch.cleannotes.core.business.state.*
import com.codingwithmitch.cleannotes.core.framework.DialogInputCaptureCallback
import com.codingwithmitch.cleannotes.core.framework.TopSpacingItemDecoration
import com.codingwithmitch.cleannotes.core.framework.hideKeyboard
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote.Companion.DELETE_NOTE_SUCCESS
import com.codingwithmitch.cleannotes.notes.business.interactors.use_cases.DeleteNote.Companion.DELETE_UNDO
import com.codingwithmitch.cleannotes.notes.framework.presentation.BaseNoteFragment
import com.codingwithmitch.cleannotes.notes.framework.presentation.notedetail.NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListStateEvent.*
import com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state.NoteListViewState
import com.codingwithmitch.notes.R
import kotlinx.android.synthetic.main.fragment_note_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


const val NOTE_LIST_STATE_BUNDLE_KEY = "com.codingwithmitch.cleannotes.notes.framework.presentation.notelist.state"

@FlowPreview
@ExperimentalCoroutinesApi
class NoteListFragment : BaseNoteFragment(R.layout.fragment_note_list),
    NoteListAdapter.Interaction,
    ItemTouchHelperAdapter
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: NoteListViewModel by viewModels {
        viewModelFactory
    }

    private var listAdapter: NoteListAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
        arguments?.let { args ->
            args.getParcelable<Note>(NOTE_PENDING_DELETE_BUNDLE_KEY)?.let { note ->
                viewModel.setNotePendingDelete(note)
                viewModel.beginPendingDelete()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        subscribeObservers()
        initSearchView()

        add_new_note_fab.setOnClickListener {
            uiController.displayInputCaptureDialog(
                getString(com.codingwithmitch.cleannotes.R.string.text_enter_a_title),
                object: DialogInputCaptureCallback{
                    override fun onTextCaptured(text: String) {
                        val newNote = viewModel.createNewNote(title = text)
                        viewModel.setStateEvent(
                            InsertNewNoteEvent(
                                title = newNote.title,
                                body = ""
                            )
                        )
                    }
                }
            )
        }

        countNumNotesInCache()
        restoreInstanceState(savedInstanceState)
    }


    private fun initSearchView(){

//        // THIS WILL NOT WORK.....?
        val searchPlate: SearchView.SearchAutoComplete? = search_view.findViewById(R.id.search_src_text)


        // So I made this stupid hack
        for((index1, child) in search_view.children.withIndex()){
            printLogD("ListFragment", "${index1}, ${child}")
            for((index2, child2) in (child as ViewGroup).children.withIndex()){
                printLogD("ListFragment", "T2: ${index2}, ${child2}")
                if(child2 is ViewGroup){
                    for((index3, child3) in (child2 as ViewGroup).children.withIndex()){
                        printLogD("ListFragment", "T3: ${index3}, ${child3}")
                        if(child3 is ViewGroup){
                            for((index4, child4) in (child3 as ViewGroup).children.withIndex()){
                                printLogD("ListFragment", "T4: ${index4}, ${child4}")
                                if(child4 is SearchView.SearchAutoComplete){
                                    child4.setOnEditorActionListener { v, actionId, event ->
                                        if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                                            || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                                            val searchQuery = v.text.toString()
                                            printLogD("NoteList", "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
                                            viewModel.setQuery(searchQuery).let{
                                                viewModel.loadFirstPage()
                                            }
                                        }
                                        true
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }


//        searchPlate?.setOnEditorActionListener { v, actionId, event ->
//
//            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
//                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
//                val searchQuery = v.text.toString()
//                printLogD("NoteList", "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
//                viewModel.setQuery(searchQuery).let{
//                    viewModel.loadFirstPage()
//                }
//            }
//            true
//        }
    }

    private fun countNumNotesInCache(){
        viewModel.setStateEvent(GetNumNotesInCacheEvent())
    }

    override fun onResume() {
        super.onResume()
        viewModel.restoreFromCache()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    // return true only if state is restored
    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[NOTE_LIST_STATE_BUNDLE_KEY] as NoteListViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.noteList =  ArrayList()

        outState.putParcelable(
            NOTE_LIST_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.layoutManagerState?.let { lmState ->
            recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    private fun saveLayoutManagerState(){
        recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    private fun setupRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            itemTouchHelper = ItemTouchHelper(
                NoteItemTouchHelperCallback(this@NoteListFragment)
            )
            listAdapter = NoteListAdapter(
                this@NoteListFragment,
                lifecycleScope,
                itemTouchHelper
            )
            itemTouchHelper?.attachToRecyclerView(this)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == listAdapter?.itemCount?.minus(1)) {
                        viewModel.nextPage()
                    }
                }
            })
            adapter = listAdapter
        }
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->

            if(viewState != null){
                viewState.noteList?.let { noteList ->
                    if(viewModel.isPaginationExhausted()
                        && !viewModel.isQueryExhausted()){
                        viewModel.setQueryExhausted(true)
                    }
                    listAdapter?.submitList(noteList)
                    listAdapter?.notifyDataSetChanged()
                }

                // a note been inserted or selected
                viewState.newNote?.let { newNote ->
                    navigateToDetailFragment(newNote)
                }

                viewState.notePendingDelete?.let { note ->
                    viewModel.removePendingNoteFromList()
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->
                if(message.response.message?.equals(DELETE_UNDO) == true){
                    viewModel.undoDelete()
                }
                if(message.response.message?.equals(DELETE_NOTE_SUCCESS) == true){
                    onCompleteDelete()
                }
                uiController.onResponseReceived(
                    response = message.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    private fun onCompleteDelete(){
        clearDeleteArgs()
        viewModel.onCompleteDelete()
    }

    private fun clearDeleteArgs(){
        arguments?.remove(NOTE_PENDING_DELETE_BUNDLE_KEY)
    }

    private fun navigateToDetailFragment(selectedNote: Note){
        val bundle = bundleOf(NOTE_DETAIL_SELECTED_NOTE_BUNDLE_KEY to selectedNote)
        findNavController().navigate(
            R.id.action_note_list_fragment_to_noteDetailFragment,
            bundle
        )
        viewModel.setNote(null)
    }

    private fun setupUI(){
        view?.hideKeyboard()
        uiController.checkBottomNav(getString(com.codingwithmitch.cleannotes.R.string.module_notes_name))
        uiController.displayBottomNav(true)
    }

    override fun inject() {
        getNoteComponent()?.inject(this)
    }

    override fun onItemSelected(position: Int, item: Note) {
        viewModel.setNote(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null // can leak memory
        itemTouchHelper = null // can leak memory
    }

    override fun onItemSwiped(position: Int) {
        if(!viewModel.isDeletePending()){
            viewModel.setNotePendingDelete(listAdapter?.getNote(position))
            viewModel.beginPendingDelete()
        }
        else{
            listAdapter?.notifyDataSetChanged()
        }
    }
}































