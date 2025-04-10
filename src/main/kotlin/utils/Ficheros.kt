package utils

import ui.Consola
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Ficheros: IUtilFich {

    private val ui = Consola()

    override fun crearDirectorio(dir: File) {
        val mkdir = dir.mkdirs()

        if (mkdir) {
            ui.mostrar("Ruta ${dir.name} creada")

        } else {
            val files = dir.listFiles()

            if (files != null) {
                if(files.isNotEmpty()) {
                    mostrarLineas(files.sortedBy { it.lastModified() }[0])

                } else {
                    ui.mostrar("No existen ficheros de Log")
                }
            }
        }
    }

    override fun guardarFichero(dir: File, contenido: String) {
        val nombreFich = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val fich = File(dir, "log$nombreFich.txt")

        fich.writeText(contenido)
    }

    private fun mostrarLineas(file: File) {
        val lineas = file.readText().split("\n")

        for (linea in lineas) ui.mostrar(linea)
    }
}