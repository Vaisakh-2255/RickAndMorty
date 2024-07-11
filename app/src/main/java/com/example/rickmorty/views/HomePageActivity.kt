package com.example.rickmorty.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.example.rickmorty.R
import com.example.rickmorty.adapter.CharacterListAdapter
import com.example.rickmorty.api.APIRequest
import com.example.rickmorty.databinding.ActivityHomePageBinding
import com.example.rickmorty.model.Character
import com.example.rickmorty.utils.Constants
import com.example.rickmorty.utils.Utils
import com.example.rickymorty.utils.BaseActivity
import org.json.JSONObject

class HomePageActivity : BaseActivity(), CharacterListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var adapter: CharacterListAdapter
    private val characterList: MutableList<Character> = ArrayList()
    private var nextUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        title = "Home"
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))


        binding.listCharacter.layoutManager = GridLayoutManager(this, 2)
        adapter = CharacterListAdapter(this, characterList, this)
        binding.listCharacter.adapter = adapter

        getCharacters(Constants.BASE_URL)

        binding.listCharacter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == characterList.size - 1) {
                    nextUrl?.let {
                        getCharacters(it)
                    }
                }
            }
        })
    }

    private fun getCharacters(url: String) {
        showProgressWheel()
        val apiRequest = APIRequest(this)
        apiRequest.setMethod(Request.Method.GET)
        apiRequest.setCustomUrl(url)

        apiRequest.executeRequest(object : APIRequest.VolleyCallback {
            override fun getSuccessResponse(response: JSONObject, message: String) {
                hideProgressWheel()
                try {
                    val results = response.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val characterObject = results.getJSONObject(i)
                        val character = Character(
                            characterObject.getString("id"),
                            characterObject.getString("name"),
                            characterObject.getString("status"),
                            characterObject.getString("image"),
                            characterObject.getJSONObject("origin").getString("name"),
                            characterObject.getJSONObject("location").getString("name")
                        )
                        characterList.add(character)
                    }
                    val info = response.getJSONObject("info")
                    nextUrl = info.optString("next", null)
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun getErrorResponse(error: String) {
                hideProgressWheel()

            }
        })
    }

    override fun onItemClick(character: Character) {
        val intent = Intent(this, CharacterDetailsActivity::class.java)
        intent.putExtra("character", character)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (characterList.isEmpty()) {
            showProgressWheel()
            getCharacters(Constants.BASE_URL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {

                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {

        Utils.getSharedPreference().edit().clear().apply()
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
