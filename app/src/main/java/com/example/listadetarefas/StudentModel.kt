package com.example.listadetarefas

import java.util.*

data class StudentModel(
    var id: Int = -1,
    var tarefa: String = "",
    var descricao: String = "",
)
//{
//   companion object{
//       fun getAutoId():Int{
//           val random = Random()
//            return random.nextInt(100)
//        }
//    }
//
//}
