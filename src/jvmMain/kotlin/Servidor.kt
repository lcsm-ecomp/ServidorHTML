import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.response.*
import io.ktor.response.*

import kotlinx.html.*
import java.io.*

fun leInt(fileName:String) =
        File(fileName).inputStream().reader().readText().toInt()

fun gravaInt(fileName:String,v:Int)  {
    val wr = File(fileName).outputStream().writer()
    wr.append(v.toString())
    wr.close()
}

var nVezes = leInt("vezes.txt")

fun main() {
    embeddedServer(Netty, port = 8082, host = "127.0.0.1") {

        routing {
            get("/dinamico.html") {
                nVezes = leInt("vezes.txt")
                val par = call.parameters
                val nome = par["Nome"]
                val idade = par["idade"]?.toInt()?:0
                call.respondText("""
                <HTML>
                <TITLE></TITLE>
                <BODY>
                Bom dia $nome,<p>

                Voce deve ter nascido em ${2019-idade}.<P>
                Voce acessou esta página $nVezes vezes.
                </BODY>
                </HTML>
                """.trimIndent(), ContentType.Text.Html)
                nVezes++
                gravaInt("vezes.txt",nVezes)
            }

            static("/") {
                files("./build/web")
            }
        }
    }.start(wait = true)
}