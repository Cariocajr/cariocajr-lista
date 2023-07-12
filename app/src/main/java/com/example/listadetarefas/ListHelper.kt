package com.example.listadetarefas

import android.content.ContentValues
import android.content.Context
import android.content.LocusId
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class ListHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "studant.db"
        private const val TBL_STUDENT = "tbl_studant"
        private const val ID = "id"
        private const val TAREFA = "tarefa"
        private const val DESCRICAO = "descricao"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblStudent = ("CREATE TABLE " + TBL_STUDENT + "("
                + ID + " INTEGER PRIMARY KEY," + TAREFA + " TEXT, "
                + DESCRICAO + " TEXT" + ")")
        db?.execSQL(createTblStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }

    fun insertStudent(std: StudentModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
//      contentValues.put(ID, std.id)
        contentValues.put(TAREFA, std.tarefa)
        contentValues.put(DESCRICAO, std.descricao)

        val success = db.insert(TBL_STUDENT, null, contentValues)
        db.close()
        return success
    }

    fun getAllStudent(): ArrayList<StudentModel> {
        val stdList: ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var tarefa: String
        var descricao: String

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(ID)
            val tarefaIndex = cursor.getColumnIndex(TAREFA)
            val descricaoIndex = cursor.getColumnIndex(DESCRICAO)

            do {
                id = cursor.getInt(idIndex)
                tarefa = cursor.getString(tarefaIndex)
                descricao = cursor.getString(descricaoIndex)

                val std = StudentModel(id = id, tarefa = tarefa, descricao = descricao)
                stdList.add(std)
            } while (cursor.moveToNext())
        }

        return stdList
    }

    fun updateStudent(std: StudentModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
//        contentValues.put(ID, std.id)
        contentValues.put(TAREFA, std.tarefa)
        contentValues.put(DESCRICAO, std.descricao)

        val success = db.update(TBL_STUDENT, contentValues, "id=" + std.id, null)
        db.close()
        return success
    }

    fun deleteStudentById(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TBL_STUDENT, "id=$id", null)
        db.close()

        if (success > 0) {
            // Reordenar IDs após a exclusão
            val remainingTasks = getAllStudent().sortedBy { it.id }
            for ((index, task) in remainingTasks.withIndex()) {
                updateTaskId(task.id, index + 1)
            }
        }

        return success

    }

    private fun updateTaskId(currentId: Int, newId: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, newId)
        db.update(TBL_STUDENT, contentValues, "$ID=$currentId", null)
        db.close()
    }

}