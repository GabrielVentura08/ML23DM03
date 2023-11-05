package br.edu.mouralacerda.ml23dm03

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Pessoa(
    val nome: String,
    val idade: Int,
    val curso: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {

    override fun toString(): String {
//        return "ID: " + id + " / NOME: " + nome
        return "ID: $id / NOME: $nome"
    }

}

