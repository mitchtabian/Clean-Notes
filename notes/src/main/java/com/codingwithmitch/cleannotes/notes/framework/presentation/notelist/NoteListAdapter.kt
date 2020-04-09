package com.codingwithmitch.cleannotes.notes.framework.presentation.notelist

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.codingwithmitch.cleannotes.core.util.printLogD
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.notes.R
import kotlinx.android.synthetic.main.layout_note_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteListAdapter(
    private val interaction: Interaction? = null,
    private val lifeCycleScope: CoroutineScope,
    private val itemTouchHelper: ItemTouchHelper
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter
{

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_note_list_item,
                parent,
                false
            ),
            interaction,
            lifeCycleScope,
            itemTouchHelper
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Note>) {
        differ.submitList(list)
    }

    class NoteViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifeCycleScope: CoroutineScope,
        private val itemTouchHelper: ItemTouchHelper
    ) : RecyclerView.ViewHolder(itemView),
        GestureDetector.OnGestureListener,
        View.OnTouchListener

    {

        private var gestureDetector: GestureDetector
                = GestureDetector(itemView.context, this)

        fun bind(item: Note) = with(itemView) {
//            itemView.setOnClickListener {
//                onSelectChangeColor(it)
//                interaction?.onItemSelected(adapterPosition, item)
//            }
            itemView.setOnTouchListener(this@NoteViewHolder)
            note_title.text = item.title
            note_timestamp.text = item.updated_at
        }

        // change the color briefly when clicked
        private fun onSelectChangeColor(view: View){
            CoroutineScope(lifeCycleScope.coroutineContext).launch {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.codingwithmitch.cleannotes.R.color.grey1
                    )
                )
                delay(100)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        com.codingwithmitch.cleannotes.R.color.colorPrimary
                    )
                )
            }
        }

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return false
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            printLogD("ListAdapter", "${itemTouchHelper}")
            printLogD("ListAdapter", "${this}")
            itemTouchHelper.startSwipe(this@NoteViewHolder)
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(event)
            return true
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Note)
    }

    override fun onItemSwiped(position: Int) {
        differ.currentList.removeAt(position)
        notifyItemRemoved(position)
    }
}













