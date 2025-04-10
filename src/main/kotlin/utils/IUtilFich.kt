package utils

import java.io.File

interface IUtilFich {
    fun crearDirectorio(dir: File)
    fun guardarFichero(dir: File, contenido: String)
}