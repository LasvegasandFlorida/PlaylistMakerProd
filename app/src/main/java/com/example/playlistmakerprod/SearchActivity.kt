package com.example.playlistmakerprod

import android.content.Context
import android.content.res.Configuration
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    var userText: String? = null
    var currentNightMode = android.content.res.Configuration.UI_MODE_NIGHT_NO
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var listOfSongs:ArrayList<Track>
    private lateinit var placeholderTextView:TextView
    private lateinit var placeholderImageView:ImageView
    private lateinit var updateButton:Button
    private lateinit var iTunesApiService:ITunesApi
    private lateinit var inputEditText:EditText
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private lateinit var songsAdapter:SongsAdapter
    init {
        instance = this
    }
    companion object {
        private var instance: SearchActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_SAVED_INPUT",userText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        inputEditText.setText(savedInstanceState.getString("USER_SAVED_INPUT",""))
    }
    override fun onConfigurationChanged ( configuration:Configuration)
    {
        super.onConfigurationChanged(configuration)
        currentNightMode = configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backBtn = findViewById<ImageView>(R.id.search_back_button)
        backBtn.setOnClickListener {
            this.finish()
        }

        iTunesApiService = retrofit.create<ITunesApi>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputEditText = findViewById<EditText>(R.id.search_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        listOfSongs = arrayListOf()
        updateButton = findViewById<Button>(R.id.update_button)
        placeholderTextView = findViewById<TextView>(R.id.placeholderText)
        placeholderImageView = findViewById<ImageView>(R.id.placeholderIcon)
        songsAdapter =SongsAdapter(listOfSongs)
        recyclerView.adapter = songsAdapter
        updateButton.setOnClickListener {
            onUpdateConnClick()
        }
        clearButton.setOnClickListener {
            onClearClick(inputEditText,inputMethodManager)
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onUpdateConnClick()
                true
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
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    private fun onUpdateConnClick()
    {
        iTunesApiService.search(inputEditText.text.toString()).enqueue(object :Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                if (response.code() == 200) {
                    listOfSongs.clear()
                    if (response.body()?.results?.isNotEmpty() == true)
                    {
                        placeholderImageView.visibility = View.GONE
                        placeholderTextView.visibility = View.GONE
                        updateButton.visibility = View.GONE
                        listOfSongs.addAll(response.body()?.results!!)
                        songsAdapter.notifyDataSetChanged()
                    }
                    if (listOfSongs.isEmpty())
                    {
                        when (currentNightMode) {
                            Configuration.UI_MODE_NIGHT_NO ->  placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_nothing_found_light))
                            Configuration.UI_MODE_NIGHT_YES -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_nothing_found_night))
                        }
                        when (AppCompatDelegate.getDefaultNightMode()) {
                            AppCompatDelegate.MODE_NIGHT_YES -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_nothing_found_night))
                            AppCompatDelegate.MODE_NIGHT_NO ->  placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_nothing_found_light))
                        }

                        placeholderImageView.visibility = View.VISIBLE
                        placeholderTextView.visibility = View.VISIBLE
                        updateButton.visibility = View.GONE
                        placeholderTextView.setText(R.string.nothing_found)
                        listOfSongs.clear()
                        songsAdapter.notifyDataSetChanged()
                    }
                } else {
                    when (currentNightMode) {
                        Configuration.UI_MODE_NIGHT_NO ->  placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_light))
                        Configuration.UI_MODE_NIGHT_YES ->placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_night))
                    }
                    when (AppCompatDelegate.getDefaultNightMode()) {
                        AppCompatDelegate.MODE_NIGHT_YES -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_night))
                        AppCompatDelegate.MODE_NIGHT_NO -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_light))
                    }
                    placeholderTextView.setText(R.string.no_internet)
                    updateButton.visibility = View.VISIBLE
                    placeholderImageView.visibility = View.VISIBLE
                    placeholderTextView.visibility = View.VISIBLE
                    listOfSongs.clear()
                    songsAdapter.notifyDataSetChanged()

                }
            }


            override fun onFailure(call: Call<ITunesResponse>, t: Throwable)
            {
                when (currentNightMode) {
                    Configuration.UI_MODE_NIGHT_NO ->  placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_light))
                    Configuration.UI_MODE_NIGHT_YES ->placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_night))
                }
                when (AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.MODE_NIGHT_YES -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_night))
                    AppCompatDelegate.MODE_NIGHT_NO -> placeholderImageView.setImageDrawable(getDrawable(R.drawable.placeholder_internet_light))
                }
                placeholderTextView.setText(R.string.no_internet)
                placeholderImageView.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
                placeholderTextView.visibility = View.VISIBLE
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
