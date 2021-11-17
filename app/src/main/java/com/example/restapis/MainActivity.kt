package com.example.restapis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.MessageQueue
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etPokemonName = findViewById<EditText>(R.id.et_pokemon_to_search_for)
        val btnSearch = findViewById<Button>(R.id.btn_search)

        queue = Volley.newRequestQueue(this)

        btnSearch.setOnClickListener(){
            getPokemon(etPokemonName.text.toString())
            etPokemonName.text.clear()
        }
    }

    fun getPokemon(pokemonName: String){
        val url ="https://pokeapi.co/api/v2/pokemon/${pokemonName}"
        val pokemonInfo = findViewById<TextView>(R.id.tv_pokemon_info)
        val jsonRequest = JsonObjectRequest(url,
            Response.Listener<JSONObject>{ response->
                val name = response.getString("name")
                val id = response.getString("id")
                // hp,ataque,defesa,velocidad, peso, tipo
                val tipo = response.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name")
                val  hp  = response.getJSONArray("stats").getJSONObject(0).getString("base_stat")
                val attack  = response.getJSONArray("stats").getJSONObject(1).getString("base_stat")
                val deffense  = response.getJSONArray("stats").getJSONObject(2).getString("base_stat")
                val speed  = response.getJSONArray("stats").getJSONObject(5).getString("base_stat")
                val infoString = "Nombre: ${name.replaceFirstChar { it.uppercase() }} \n#: $id \nTipo: $tipo \nPuntos de Salud: $hp \nAtaque: $attack \nDefensa: $deffense  \nVelocidad: $speed"
                pokemonInfo.setText(infoString)
            },
            Response.ErrorListener { errorMessage->
                pokemonInfo.setText("404 Pokemon Not found")
                Log.d("JSONResponse","Error: $errorMessage")
            }
            )
        queue.add(jsonRequest)
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll("stopped")
    }
}