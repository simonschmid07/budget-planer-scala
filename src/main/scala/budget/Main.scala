package budget

import budget.Model.*
import java.time.{LocalDate, YearMonth}
import scala.io.StdIn.readLine

object Main:
  def main(args: Array[String]): Unit =
    println("== Budget Planner ==")
    loop(Model.empty)

  @annotation.tailrec
  def loop(state: State): Unit =
    println(
      """
        |[1] Transaktion hinzufuegen
        |[2] Liste anzeigen (alle)
        |[3] Filtern: Zeitraum
        |[4] Summen pro Kategorie (Monat)
        |[5] Saldo anzeigen
        |[6] Notiz aendern
        |[7] Transaktion loeschen
        |[0] Beenden
        |""".stripMargin)
    print("> ")
    readLine() match
      case "0" =>
        println("Bye!")
      case _ =>
        println("Ungueltig.")
        loop(state)

  private def prompt(msg: String): String =
    print(msg); readLine().nn

  private def readBigDecimal(msg: String): BigDecimal =
    scala.util.Try(BigDecimal(prompt(msg))).getOrElse {
      println("Ungueltiger Betrag."); readBigDecimal(msg)
    }

  private def readDate(msg: String): java.time.LocalDate =
    val s = prompt(msg)
    scala.util.Try(java.time.LocalDate.parse(s)).getOrElse {
      println("Ungueltiges Datum."); readDate(msg)
    }

  private def readYearMonth(): java.time.YearMonth =
    val s = prompt("Monat (YYYY-MM): ")
    scala.util.Try(java.time.YearMonth.parse(s)).getOrElse {
      println("Ungueltig."); readYearMonth()
    }

  private def readCategory(): Category =
    println("Kategorie auswaehlen:")
    Category.values.zipWithIndex.foreach { case (c, i) => println(s"[$i] $c") }
    val idx = prompt("Index: ").toIntOption.getOrElse(-1)
    Category.values.lift(idx).getOrElse {
      println("Ungueltig."); readCategory()
    }
