package app

import model.Operadores
import ui.IEntradaSalida
import utils.IUtilFich
import java.io.File

class Calculadora(private val ui: IEntradaSalida, private val ficheros: IUtilFich) {

    private fun pedirNumero(msj: String, msjError: String = "Número no válido!"): Double {
        return ui.pedirDouble(msj) ?: throw InfoCalcException(msjError)
    }

    private fun pedirInfo() = Triple(
        pedirNumero("Introduce el primer número: ", "El primer número no es válido!"),
        Operadores.getOperador(ui.pedirInfo("Introduce el operador (+, -, *, /): ").firstOrNull())
            ?: throw InfoCalcException("El operador no es válido!"),
        pedirNumero("Introduce el segundo número: ", "El segundo número no es válido!"))

    private fun realizarCalculo(numero1: Double, operador: Operadores, numero2: Double) =
        when (operador) {
            Operadores.SUMA -> numero1 + numero2
            Operadores.RESTA -> numero1 - numero2
            Operadores.MULTIPLICACION -> numero1 * numero2
            Operadores.DIVISION -> numero1 / numero2
        }

    fun iniciar(args: Array<String>) {
        var contenido = ""
        var dir = File("./log")

        when (args.size) {
            0 -> ficheros.crearDirectorio(dir)

            1 -> {
                dir = File(args[0])
                ficheros.crearDirectorio(dir)
            }

            4 -> {
                try {
                    dir = File(args[0])
                    ficheros.crearDirectorio(dir)

                    val num1 = args[1].toDoubleOrNull() ?: throw InfoCalcException("El primer número no es válido!")
                    val op = Operadores.getOperador(args[2].firstOrNull()) ?: throw InfoCalcException("El operador no es válido!")
                    val num2 = args[3].toDoubleOrNull() ?: throw InfoCalcException("El segundo número no es válido!")

                    val resultado = realizarCalculo(num1, op, num2)
                    val resStr = "$num1 $op $num2 = ${".2f".format(resultado)}"

                    ui.mostrar(resStr)

                    contenido += resStr + "\n"

                } catch (e: InfoCalcException) {
                    ui.mostrarError(e.message ?: "Se ha producido un error al leer los argumentos!!!")

                    contenido += (e.message ?: "Se ha producido un error al leer los argumentos!!!") + "\n"
                }
            }

            else -> {
                ui.mostrarError("Número de argumentos invalido!!!")
                return
            }
        }

        Thread.sleep(3000)

        do {
            try {
                ui.limpiarPantalla()
                val (numero1, operador, numero2) = pedirInfo()
                val resultado = realizarCalculo(numero1, operador, numero2)

                val resStr = "$numero1 $operador $numero2 = ${"%.2f".format(resultado)}"
                ui.mostrar(resStr)

                contenido += resStr + "\n"

            } catch (e: InfoCalcException) {
                ui.mostrarError(e.message ?: "Se ha producido un error!")

                contenido += (e.message ?: "Se ha producido un error!") + "\n"
            }
        } while (ui.preguntar())
        ui.limpiarPantalla()

        ficheros.guardarFichero(dir, contenido)
    }

}