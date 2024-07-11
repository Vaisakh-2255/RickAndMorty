package com.example.rickmorty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickmorty.R
import com.example.rickmorty.model.Character
import com.example.rickmorty.utils.FavoriteManager

class CharacterListAdapter(
    private val context: Context,
    private val characterList: List<Character>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    private val favoriteManager: FavoriteManager = FavoriteManager(context)

    interface OnItemClickListener {
        fun onItemClick(character: Character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_list, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]

        val favoriteIconResId = if (character.id != null && favoriteManager.isFavorite(character.id)) {
            R.drawable.baseline_favorite_red_24
        } else {
            R.drawable.baseline_favorite_border_24
        }
        holder.favoriteIcon.setImageResource(favoriteIconResId)


        Glide.with(context)
            .load(character.imageUrl)
            .error(R.drawable.img)
            .into(holder.characterImage)

       
        holder.characterName.text = character.name
        holder.characterStatus.text = character.status

       
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(character)
        }
    }

    override fun getItemCount(): Int = characterList.size

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val characterImage: ImageView = itemView.findViewById(R.id.character_img)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite_icon)
        val characterName: TextView = itemView.findViewById(R.id.character_name)
        val characterStatus: TextView = itemView.findViewById(R.id.character_status)
    }
}
