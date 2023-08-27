package com.example.playlistmakerprod

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Locale

const val SEARCH_HISTORY_KEY = "key_for_search_history"
class SearchActivity : AppCompatActivity() {
    var userText: String? = null
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var listOfSongs:ArrayList<Track>
    private lateinit var placeholderTextView:TextView
    private lateinit var placeholderImageView:ImageView
    private lateinit var updateButton:Button
    private lateinit var iTunesApiService:ITunesApi
    private lateinit var inputEditText:EditText
    private lateinit var searchHistory: SearchHistory

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private lateinit var songsAdapter:SongsAdapter
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
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        iTunesApiService = retrofit.create<ITunesApi>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        updateButton = findViewById<Button>(R.id.update_button)
        placeholderTextView = findViewById<TextView>(R.id.placeholderText)
        placeholderImageView = findViewById<ImageView>(R.id.placeholderIcon)
        inputEditText = findViewById<EditText>(R.id.search_edit_text)
        listOfSongs = arrayListOf()
        songsAdapter =SongsAdapter(listOfSongs)
        recyclerView.adapter = songsAdapter
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) showHideError(ErrorsMesseges.SHOW_HISTORY.code)
            else {
                listOfSongs.clear()
                songsAdapter.notifyDataSetChanged()
                showHideError(ErrorsMesseges.ON_SUCCESS.code)
            }
        }
        updateButton.setOnClickListener {
            if (updateButton.text == getText(R.string.update)) onUpdateConnClick()
            else {
                searchHistory.clear()
                listOfSongs.clear()
                showHideError(ErrorsMesseges.ON_SUCCESS.code)
                songsAdapter.notifyDataSetChanged()
            }
        }
        clearButton.setOnClickListener {
            onClearClick(inputEditText,inputMethodManager)
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onUpdateConnClick()
            }
            false
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userText = s.toString()
                if (inputEditText.hasFocus() && inputEditText.text.isEmpty()) showHideError(ErrorsMesseges.SHOW_HISTORY.code)
                else {
                    listOfSongs.clear()
                    songsAdapter.notifyDataSetChanged()
                    showHideError(ErrorsMesseges.ON_SUCCESS.code)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        songsAdapter.onItemClick = { track ->
            searchHistory.add(track)
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    private fun showHideError(code: Int)
    {
        when (code) {
            ErrorsMesseges.ON_SUCCESS.code -> {
                placeholderImageView.visibility = View.GONE
                placeholderTextView.visibility = View.GONE
                updateButton.visibility = View.GONE
            }
            ErrorsMesseges.ON_NOTHING_FOUND.code -> {
                placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_nothing_found))
                placeholderImageView.visibility = View.VISIBLE
                placeholderTextView.visibility = View.VISIBLE
                updateButton.visibility = View.GONE
                placeholderTextView.setText(R.string.nothing_found)
            }
            ErrorsMesseges.ON_NO_CONNECTION.code -> {
                placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_no_internet))
                placeholderTextView.setText(R.string.no_internet)
                updateButton.visibility = View.VISIBLE
                updateButton.text = getText(R.string.update)
                placeholderImageView.visibility = View.VISIBLE
                placeholderTextView.visibility = View.VISIBLE
            }
            ErrorsMesseges.SHOW_HISTORY.code -> {
                placeholderImageView.visibility = View.GONE
                placeholderTextView.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
                updateButton.text = getText(R.string.clear_history_button)
                placeholderTextView.setText(R.string.history_header)
                listOfSongs.clear()
                searchHistory.getSongs(listOfSongs)
                if (listOfSongs.isEmpty()) showHideError(ErrorsMesseges.ON_SUCCESS.code)
                songsAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun onUpdateConnClick()
    {
        iTunesApiService.search(inputEditText.text.toString()).enqueue(object :Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                if (response.code() == 200){
                    listOfSongs.clear()
                    if (response.body()?.results?.isNotEmpty() == true){
                        showHideError(ErrorsMesseges.ON_SUCCESS.code)
                        listOfSongs.addAll(response.body()?.results!!)
                    }
                    if (listOfSongs.isEmpty()){
                        showHideError(ErrorsMesseges.ON_NOTHING_FOUND.code)
                        listOfSongs.clear()
                    }
                }
                else {
                   showHideError(ErrorsMesseges.ON_NO_CONNECTION.code)
                    listOfSongs.clear()
                }
                songsAdapter.notifyDataSetChanged()


            }
            override fun onFailure(call: Call<ITunesResponse>, t: Throwable)
            {
                showHideError(ErrorsMesseges.ON_NO_CONNECTION.code)
                listOfSongs.clear()
                songsAdapter.notifyDataSetChanged()
            }
        })

    }
    private fun onClearClick(inputEditText:EditText,inputMethodManager:InputMethodManager?)
    {
        placeholderImageView.visibility = View.GONE
        placeholderTextView.visibility = View.GONE
        updateButton.visibility = View.GONE
        listOfSongs.clear()
        songsAdapter.notifyDataSetChanged()
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


fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}
