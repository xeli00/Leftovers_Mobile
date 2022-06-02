package com.example.leftovers

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.leftovers.database.RecipeDAO
import com.example.leftovers.database.StarredDAO


class FavoriteActivity : AppCompatActivity() {
    private lateinit var recipeDAO: RecipeDAO
    private lateinit var starredDAO: StarredDAO
    var favoriteRecipesList : ArrayList<Recipe> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_recipe)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "leftovers.db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        recipeDAO = db.recipeDao()
        starredDAO = db.starredDao()
        val userPid = intent.getStringExtra("EMAIL_PID")
        if (userPid != null) {
            Log.i("PID", userPid)
        }


        if (userPid != null) {
            getFavoriteRecipes(userPid)
            showFavorite()?.let { showFavoriteRecipes(it) }
        }


    }



    private fun showFavorite() : ArrayList<Recipe>? {

        if (favoriteRecipesList.isEmpty()) {
            showEmptyFavorite()
            return null
        } else {
            return favoriteRecipesList
        }



    }
    private fun showFavoriteRecipes(favoriteRecipes: ArrayList<Recipe>){
        val cards = findViewById<LinearLayout>(R.id.cards)

        for(i in favoriteRecipes){
            val newCard = CardView(this)
            newCard.background = ContextCompat.getDrawable(this, R.drawable.trasparent2)
            layoutInflater.inflate(R.layout.default_card, newCard)

            val t: TextView = newCard.findViewById(R.id.title)
            val s: TextView = newCard.findViewById(R.id.servants)
            val kcal: TextView = newCard.findViewById(R.id.kcal)


            t.text = "${i.name}"
            s.text ="Servings: ${i.serving}"
            if(i.calories!= "0") {
                kcal.text = "KCal: ${i.calories}"
            }else{
                kcal.text = "KCal: //"
            }
            var buttonState : Int = 0
            val heart = newCard.findViewById<ImageButton>(R.id.favoriteHeart)
            heart.setBackgroundResource(R.drawable.ic_favorite_red)

            heart.setOnClickListener(){
                val dialog = Dialog(this)
                val userPid = intent.getStringExtra("EMAIL_PID")

                var starred = Starred(i.uid,userPid,i.uid)
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

                alertDialogBuilder.setMessage("DO YOU WANT TO REMOVE THIS RECIPE?")
                alertDialogBuilder.setNegativeButton("YES",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("REMOVE FAVORITE", "Ok btn pressed")
                        starredDAO.deleteStarred(starred)
                        Log.i("DELETE", starred.toString())
                        buttonState = 0
                        cards.removeView(newCard)
                    })
                alertDialogBuilder.setPositiveButton("NO",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("REMOVE FAVORITE", "Cancel btn pressed")
                        onDestroy()

                    })

                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()





            }

            cards.addView(newCard)

        }


    }

    fun popupMessage()  {



    }

    private fun showEmptyFavorite(){
        var text = findViewById<TextView>(R.id.noRecipeFound)
        text.text = "NO FAVORITE RECIPE YET :("
        text.visibility
    }


    private fun getFavoriteRecipes(userPid : String) : ArrayList<Recipe>{
        var recipeCheck = starredDAO.getStarred(userPid) as ArrayList<Int>
        for(fr in recipeCheck){
            var recipe = recipeDAO.getRecipeById(fr)
            favoriteRecipesList.add(recipe)
        }
        return favoriteRecipesList
    }


}

