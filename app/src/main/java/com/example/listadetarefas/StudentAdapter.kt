package com.example.listadetarefas

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
        private var stdList:ArrayList<StudentModel> = ArrayList()
        private var onClickItem:((StudentModel) -> Unit)? = null
        private var onClickDeleteItem: ((StudentModel) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<StudentModel>) {
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnclickItem(callback: (StudentModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (StudentModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std, parent, false)
    )

    override fun getItemCount(): Int {
        return stdList.size
    }

    fun removeItemById(id: Int) {
        val iterator = stdList.iterator()
        while (iterator.hasNext()) {
            val student = iterator.next()
            if (student.id == id) {
                iterator.remove()
                notifyItemRemoved(stdList.indexOf(student))
                break
            }
        }

    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(std) }
    }

    class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var tarefa = view.findViewById<TextView>(R.id.tvTarefa)
        private var descricao = view.findViewById<TextView>(R.id.tvDescricao)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)

        fun bindView(std: StudentModel) {
            id.text = std.id.toString()
            tarefa.text = std.tarefa
            descricao.text = std.descricao
        }

    }
}