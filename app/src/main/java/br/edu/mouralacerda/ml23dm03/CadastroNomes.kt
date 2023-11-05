package br.edu.mouralacerda.ml23dm03

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class CadastroNomes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_nomes)

        val botaoSalvar = findViewById<Button>(R.id.btnSalvar)
        val campoNome = findViewById<EditText>(R.id.edtNome)
        val campoIdade = findViewById<EditText>(R.id.edtIdade)
        val spinnerCurso = findViewById<Spinner>(R.id.spnCurso)


        ArrayAdapter.createFromResource(
            this,
            R.array.cursos_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCurso.adapter = adapter
        }


        botaoSalvar.setOnClickListener {

            val nome = campoNome.text.toString()
            val idade = campoIdade.text.toString().toIntOrNull() ?: 0 // default para 0 se nada for inserido
            val curso = spinnerCurso.selectedItem.toString()

            val pessoa = Pessoa(nome, idade, curso)

            Database.getInstance(this)?.pessoaDAO()?.salvar(pessoa)

            finish()
        }

        val pessoaId = intent.extras?.getInt("ID_PESSOA")
        var pessoa: Pessoa? = null

        if (pessoaId != null) {
            // Carregue a Pessoa do banco de dados usando o ID
            pessoa = Database.getInstance(this)?.pessoaDAO()?.buscarPorId(pessoaId)
            pessoa?.let {
                campoNome.setText(it.nome)
                campoIdade.setText(it.idade.toString())
                // Defina o spinner para o valor do curso.
                setSpinnerToValue(spinnerCurso, it.curso)
            }
        }

        botaoSalvar.setOnClickListener {
            val nome = campoNome.text.toString()
            val idade = campoIdade.text.toString().toIntOrNull() ?: 0
            val curso = spinnerCurso.selectedItem.toString()

            if (pessoa == null) {
                // Criando uma nova pessoa
                val novaPessoa = Pessoa(nome, idade, curso)
                Database.getInstance(this)?.pessoaDAO()?.salvar(novaPessoa)
            } else {
                // Atualizando uma pessoa existente
                val pessoaAtualizada = pessoa.copy(nome = nome, idade = idade, curso = curso)
                Database.getInstance(this)?.pessoaDAO()?.atualizar(pessoaAtualizada)
            }

            finish()
        }
    }

    private fun setSpinnerToValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter
        for (position in 0 until adapter.count) {
            if (adapter.getItem(position).toString() == value) {
                spinner.setSelection(position)
                return
            }
        }
    }
}