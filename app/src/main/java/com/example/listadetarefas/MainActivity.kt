package com.example.listadetarefas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.AccessControlProfileId
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var Tarefa: EditText
    private lateinit var Descricao: EditText
    private lateinit var Adicionar: Button
    private lateinit var Visualizar: Button
    private lateinit var Atualizar: Button
    private lateinit var imgv_carioca: ImageView

    private var isFirstView: Boolean = false

    private lateinit var listHelper: ListHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std:StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecycleVew()
        listHelper = ListHelper(this)
        fadeInImage()

        Adicionar.setOnClickListener { addStudent() }
        Visualizar.setOnClickListener { getStudent() }
        Atualizar.setOnClickListener { updateStudent() }

        adapter?.setOnclickItem {
            Toast.makeText(this, it.tarefa, Toast.LENGTH_SHORT).show()
            Tarefa.setText(it.tarefa)
            Descricao.setText(it.descricao)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun getStudent () {
        val stdList = listHelper.getAllStudent()

        if (stdList.isEmpty()) {
            Toast.makeText(this, "Adicione uma tarefa para poder visualizar",
                Toast.LENGTH_SHORT)
                .show()
            return
        }

        val adjustedStdList = stdList.mapIndexed { index, student ->
            student.copy(id = index + 1)
        }

        //display data in recyclerview
        adapter?.addItems(stdList)

        fadeOutImage()
    }

    private fun addStudent() {
        val tarefa = Tarefa.text.toString()
        val descricao = Descricao.text.toString()

        if (tarefa.isEmpty()) {
            Toast.makeText(
                this, "Por favor, preencha corretamente o primeiro campo",
                Toast.LENGTH_SHORT).show()
        } else {
            var std = StudentModel(tarefa = tarefa, descricao = descricao)
            val status = listHelper.insertStudent(std)

            if (status > -1){
                std = std.copy(id = status.toInt())
                Toast.makeText(this, "Tarefa Adicionada!", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudent()
            } else {
                Toast.makeText(this, "Erro em adicionar tarefa!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudent(){
        val tarefa = Tarefa.text.toString()
        val descricao = Descricao.text.toString()

        if (tarefa == std?.tarefa && descricao == std?.descricao){
            Toast.makeText(this, "Altere os dados para atualizar a tarefa",
                Toast.LENGTH_SHORT).show()
            return
        }

        if (std == null) {
            Toast.makeText(
                this,
                "Selecione uma tarefa primeiro!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //Verificar se os campos estão vazios
        if (tarefa.isEmpty()) {
            Toast.makeText(
                this,
                "O primeiro campo é obrigatório, preencha corretamente!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

            val std = StudentModel(id = std!!.id, tarefa = tarefa, descricao = descricao)
            val status = listHelper.updateStudent(std)
            if (status >-1 ) {
                clearEditText()
                getStudent()
                Toast.makeText(this, "Tarefa atualizada com sucesso!",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Falha na atualização", Toast.LENGTH_SHORT).show()
            }
        }
    private fun deleteStudent(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Tem certeza que quer deletar esse item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Sim") { dialog, _ ->
            listHelper.deleteStudentById(id)
            adapter?.removeItemById(id)
            getStudent()
            dialog.dismiss()
            Toast.makeText(this, "Tarefa excluída com sucesso!",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Não") { dialog, _  ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()

        val positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.start_color))
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.start_color))
    }

    private fun clearEditText() {
        Tarefa.setText("")
        Descricao.setText("")
        Tarefa.requestFocus()
    }

    private fun initRecycleVew(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView(){
        Tarefa = findViewById(R.id.Tarefa)
        Descricao = findViewById(R.id.Descricao)
        Adicionar = findViewById(R.id.Adicionar)
        Visualizar = findViewById(R.id.Visualizar)
        Atualizar = findViewById(R.id.Atualizar)
        recyclerView = findViewById(R.id.recyclerView)
        imgv_carioca = findViewById(R.id.imgv_carioca)
    }
    private fun fadeOutImage() {

        if (isFirstView) {
            return
        } else {
            isFirstView = true
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.duration = 300 // Tempo da animação em milissegundos
            fadeOut.fillAfter = true // Manter a imagem invisível após a animação

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    // Tornar a imagem invisível após a animação
                    imgv_carioca.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

            imgv_carioca.startAnimation(fadeOut)
        }

    }

    private fun fadeInImage() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1200 // Tempo da animação em milissegundos

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}
        })

        imgv_carioca.startAnimation(fadeIn)
    }

}