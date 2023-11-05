package br.edu.mouralacerda.ml23dm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listaDaTela: ListView
    private lateinit var pessoaAdapter: PessoaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaDaTela = findViewById(R.id.lstNomes)
        val botaoNovoNome = findViewById<FloatingActionButton>(R.id.btnNovoNome)

        botaoNovoNome.setOnClickListener {
            val intent = Intent(this, CadastroNomes::class.java)
            startActivity(intent)
        }



        listaDaTela.setOnItemClickListener { adapterView, view, position, id ->
            val pessoa = pessoaAdapter.getItem(position)
            val intent = Intent(this@MainActivity, CadastroNomes::class.java)
            intent.putExtra(
                "ID_PESSOA",
                pessoa?.id
            ) // Passar o ID da pessoa para a atividade de edição.
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLista("id")
    }

    private fun atualizarLista(ordem: String) {
        val lista = when (ordem) {
            "id" -> Database.getInstance(this)!!.pessoaDAO().listarPorId()
            "nome" -> Database.getInstance(this)!!.pessoaDAO().listarPorNome()
            else -> emptyList()
        }

        pessoaAdapter = PessoaAdapter(this, R.layout.item_pessoa, lista, this::onItemDelete)
        listaDaTela.adapter = pessoaAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ordemNome -> atualizarLista("nome")
            R.id.ordemId -> atualizarLista("id")
        }
        return super.onOptionsItemSelected(item)
    }


    fun onItemDelete(pessoa: Pessoa) {
        // Mostrar um AlertDialog para confirmar a exclusão.
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja excluir o item selecionado?")
            .setPositiveButton("Sim") { dialog, id ->
                // Chame a função de exclusão do DAO aqui
                Database.getInstance(this)?.pessoaDAO()?.apagar(pessoa)
                atualizarLista("id") // Atualize a lista após a exclusão.
            }
            .setNegativeButton("Não", null)
            .show()
    }






}
