package br.edu.mouralacerda.ml23dm03

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@androidx.room.Database(entities = [Pessoa::class], version = 2)
abstract class Database : RoomDatabase() {

    abstract fun pessoaDAO(): PessoaDAO

    companion object {

        private var database: Database? = null
        private val DATABASE = "BDNOMES"

        // Declaração da migração
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Altera a tabela Pessoa adicionando as colunas 'idade' e 'curso'
                database.execSQL("ALTER TABLE Pessoa ADD COLUMN idade INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE Pessoa ADD COLUMN curso TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context): Database? {
            if (database == null) {
                synchronized(Database::class) {
                    if (database == null) {
                        database = Room.databaseBuilder(context.applicationContext,
                            Database::class.java, DATABASE)
                            .addMigrations(MIGRATION_1_2) // Aqui adiciona a migração
                            .allowMainThreadQueries() // Permitir consultas na thread principal (remover em produção)
                            .build()
                    }
                }
            }
            return database
        }
    }
}
