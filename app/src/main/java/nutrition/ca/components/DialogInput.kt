package nutrition.ca.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_input.view.*
import nutrition.ca.R
import java.lang.IllegalArgumentException

class DialogInput: DialogFragment() {

    lateinit var dialogListener: () -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_input, null)
            builder.setView(view)
                .setPositiveButton(R.string.save){dialog, which ->
                    save(
                        view.etValue.text.toString().toInt(),
                        view.etGoal.text.toString().toInt()
                    )
                }
                .setNegativeButton(R.string.cancel){dialog, which ->
                    getDialog()?.cancel()
                }
            builder.create()
        } ?: throw IllegalArgumentException("error")
    }

    private fun save(value: Int, goal: Int) {
        val data: HashMap<String, Int> = hashMapOf(
            "value" to value,
            "goal" to goal
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("datas")
            .document(tag!!)
            .set(data)
            .addOnSuccessListener { documentReference ->
                dialogListener.invoke()
            }
            .addOnFailureListener { exception ->

            }
    }
}