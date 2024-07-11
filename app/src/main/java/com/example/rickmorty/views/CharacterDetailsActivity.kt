package com.example.rickmorty.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.android.volley.Request
import com.bumptech.glide.Glide
import com.example.rickmorty.R
import com.example.rickmorty.api.APIRequest
import com.example.rickmorty.databinding.ActivityCharacterListingBinding
import com.example.rickmorty.model.Character
import com.example.rickymorty.utils.BaseActivity
import org.json.JSONException
import org.json.JSONObject

class CharacterDetailsActivity : BaseActivity() {

    private var binding: ActivityCharacterListingBinding? = null
    private var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityCharacterListingBinding.inflate(layoutInflater)
        setContentView(binding?.root ?: return)


        character = intent.getParcelableExtra("character")

        character?.id?.let { fetchCharacterDetails(it) }
    }

    private fun fetchCharacterDetails(characterId: String) {
        showProgressWheel()
        val apiRequest = APIRequest(this)
        apiRequest.setMethod(Request.Method.GET)
        apiRequest.updatePath("/$characterId")
        apiRequest.executeRequest(object : APIRequest.VolleyCallback {
            override fun getSuccessResponse(response: JSONObject, message: String) {
                hideProgressWheel()
                try {
                    val name = response.getString("name")
                    val species = response.getString("species")
                    val type =
                        response.optString("type", "N/A")
                    val gender = response.getString("gender")
                    val origin = response.getJSONObject("origin").getString("name")
                    val location = response.getJSONObject("location").getString("name")
                    val imageUrl = response.getString("image")

                    binding?.apply {
                        characterName.text = name
                        characterSpecies.text = species
                        characterType.text = type
                        characterGender.text = gender
                        characterOrigin.text = origin
                        characterAddress.text = location
                        Glide.with(this@CharacterDetailsActivity)
                            .load(imageUrl)
                            .error(R.drawable.img)
                            .into(profileImage)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    showAlert("Error", "Failed to parse character details")
                }
            }

            override fun getErrorResponse(error: String) {
                hideProgressWheel()
                showAlert("Error", error)
            }
        })
    }
}
