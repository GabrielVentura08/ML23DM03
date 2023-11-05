package br.edu.mouralacerda.ml23dm03

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class PessoaAdapter(context: Context, private val resource: Int, items: List<Pessoa>, private val onDelete: (Pessoa) -> Unit) : ArrayAdapter<Pessoa>(context, resource, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(resource, parent, false)

        val nomeTextView = view.findViewById<TextView>(R.id.txtNome)
        val idadeTextView = view.findViewById<TextView>(R.id.txtIdade)
        val cursoTextView = view.findViewById<TextView>(R.id.txtCurso)
        val deleteIcon = view.findViewById<ImageView>(R.id.btnDelete) // Encontrando o ImageView do ícone de lixeira.

        val pessoa = getItem(position)

        nomeTextView.text = pessoa?.nome
        idadeTextView.text = pessoa?.idade.toString()
        cursoTextView.text = pessoa?.curso

        deleteIcon.setOnClickListener {
            // Usar o callback onDelete para informar que um item deve ser deletado.
            pessoa?.let { it -> onDelete(it) }
        }

        return view
    }
}
