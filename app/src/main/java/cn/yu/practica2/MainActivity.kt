package cn.yu.practica2

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var botonLanzar : Button
    private lateinit var dados : List<ImageView>
    private lateinit var _puntuacion : TextView
    private lateinit var _ronda : TextView
    private var puntuacion = 0
    private var ronda = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inicialComponentes()
        if (savedInstanceState != null) {
            puntuacion = savedInstanceState.getInt("puntuacion")
            ronda = savedInstanceState.getInt("ronda")
            actualizarPuntuacion()
            actualizarRonda()
        }else {
            hasDialog(
                "Bienvenidos", "Bienvenido a este juego!!\nSi ganas más o igual de 80 puntos" +
                        " en cinco rondas, ganarás, de lo contrario, perderás", "Empezar"
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("puntuacion", puntuacion)
        outState.putInt("ronda", ronda)
    }

    private fun lanzarDados(){
        ronda++
        dados.forEach { dado ->
            val resultado = Random.nextInt(1, 7) // Generar número entre 1 y 6
            actualizarDado(dado, resultado)
            puntuacion += resultado // Sumar a la puntuación
        }
        actualizarPuntuacion()
        actualizarRonda()
        if(ronda>=5&&puntuacion<80){
            val des = "Ganas "+puntuacion.toString()+" puntos, tienes menos de 80 puntos :("
            hasDialog("Perdido",des,"Reiniciar")
        }
        else if(puntuacion>=80){
            val des = "Ganas "+puntuacion.toString()+" puntos, tienes más o igual de 80 puntos! XD"
            hasDialog("Ganas",des,"Reiniciar")
        }
    }

    private fun actualizarDado(dado: ImageView, resultado: Int) {
        val resourceId = when (resultado) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1 // Valor por defecto
        }
        dado.setImageResource(resourceId) // Actualizar la imagen del dado
    }

    private fun actualizarPuntuacion(){
        _puntuacion.setText(puntuacion.toString())
    }

    private fun actualizarRonda(){
        _ronda.setText(("Ronda: "+ronda))
    }

    private fun inicialComponentes(){
        var temp = mutableListOf<ImageView>()
        temp.add(findViewById(R.id.dado1))
        temp.add(findViewById(R.id.dado2))
        temp.add(findViewById(R.id.dado3))
        temp.add(findViewById(R.id.dado4))
        botonLanzar = findViewById(R.id.button3)
        botonLanzar.setOnClickListener { lanzarDados() }
        dados = temp
        _puntuacion = findViewById(R.id.Puntuacion)
        _ronda = findViewById(R.id.Ronda)
        _ronda.setText(("Ronda: "+ronda))
    }
    private fun reiniciar(){
        puntuacion=0
        ronda=0
        actualizarPuntuacion()
        actualizarRonda()
    }
    private fun hasDialog(title:String,description:String,textBoton:String){
        val dialog = AlertDialog.Builder(this)
            .setIcon(R.drawable.dice_6)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(textBoton) { _, _ ->
                reiniciar() }

        dialog.create()
        dialog.setCancelable(false)
        dialog.show()

    }
}