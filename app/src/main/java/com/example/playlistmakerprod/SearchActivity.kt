package com.example.playlistmakerprod

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchActivity : AppCompatActivity() {
    var userText: String? = null
    var listOfSongs= emptyList<Track>()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_SAVED_INPUT",userText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        inputEditText.setText(savedInstanceState.getString("USER_SAVED_INPUT",""))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backBtn = findViewById<ImageView>(R.id.search_back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
        listOfSongs= listOf<Track>(
            Track(getString(R.string.smells_like_teen_spirit_name),getString(R.string.smells_like_teen_spirit_artist),getString(
                R.string.smells_like_teen_spirit_length),getString(R.string.smells_like_teen_spirit_image)),
            Track(getString(R.string.billie_jean_name),getString(R.string.billie_jean_artist),getString(
                R.string.billie_jean_length),getString(R.string.billie_jean_image)),
            Track(getString(R.string.stayin_alive_name),getString(R.string.stayin_alive_artist),getString(
                R.string.stayin_alive_length),getString(R.string.stayin_alive_image)),
            Track(getString(R.string.whole_lotta_love_name),getString(R.string.whole_lotta_love_artist),getString(
                R.string.whole_lotta_love_length),getString(R.string.whole_lotta_love_image)),
            Track(getString(R.string.sweet_child_o_mine_name),getString(R.string.sweet_child_o_mine_artist),getString(
                R.string.sweet_child_o_mine_length),getString(R.string.sweet_child_o_mine_image))
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val songsAdapter =SongsAdapter(listOfSongs)
        recyclerView.adapter = songsAdapter
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            onClearClick(inputEditText,inputMethodManager)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    private fun onClearClick(inputEditText:EditText,inputMethodManager:InputMethodManager?)
    {
        inputEditText.setText("")
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        inputEditText.clearFocus()
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


}
class SongsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val songImage: ImageView = itemView.findViewById(R.id.songImage)
    private val songName: TextView = itemView.findViewById(R.id.songName)
    private val songAuthor: TextView = itemView.findViewById(R.id.songAuthor)
    private val songLength: TextView = itemView.findViewById(R.id.songLength)

    fun bind(model: Track) {
        songName.text = model.trackName
        songAuthor.text = model.artistName
        songLength.text = model.trackTime
        Glide.with(itemView).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(2)).placeholder(R.drawable.placeholder).into(songImage)
    }

}
class SongsAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<SongsViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_view, parent, false)
        return SongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}