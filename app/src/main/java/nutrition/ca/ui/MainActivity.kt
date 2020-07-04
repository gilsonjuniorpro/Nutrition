package nutrition.ca.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import nutrition.ca.R
import nutrition.ca.components.DialogInput
import nutrition.ca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        setSupportActionBar(toolbar)

        //binding.progressBar1.setValue(66)

        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> refresh()
            else -> {
                DialogInput().apply {
                    dialogListener = {
                        refresh()
                    }
                    show(supportFragmentManager, item.title.toString())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        binding.progressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("datas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val value = document.data["value"] as Long
                    val goal = document.data["goal"] as Long

                    val res = value.toFloat() * 100 / goal.toFloat()
                    val text = "$value / $goal"

                    when(document.id){
                        getString(R.string.calories) -> {
                            binding.tvValue1.text = text
                            binding.progressBar1.setValue(res.toInt())
                        }
                        getString(R.string.carbohydrates) -> {
                            binding.tvValue2.text = text
                            binding.progressBar2.setValue(res.toInt())
                        }
                        getString(R.string.proteins) -> {
                            binding.tvValue3.text = text
                            binding.progressBar3.setValue(res.toInt())
                        }
                    }
                }
            }
            .addOnFailureListener{ exception ->
                Log.e("Teste", exception.message, exception)
            }
            .addOnCompleteListener{
                binding.progressBar.visibility = View.GONE
            }
    }


    /*
    [data] =
        calories {
            value,
            goal
        },
        carb {
            value,
            goal
        },
        protein {
            value,
            goal
        }
    */
}