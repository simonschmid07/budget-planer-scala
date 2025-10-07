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
      case "1" =>
        val amount = readBigDecimal("Betrag (Einnahme + / Ausgabe -): ")
        val cat    = readCategory()
        val date   = readDate("Datum (YYYY-MM-DD): ")
        val note   = prompt("Notiz: ")
        val s2     = add(state, amount, cat, date, note)
        println("✓ Hinzugefuegt.")
        loop(s2)

      case "2" =>
        renderTxns(state.txns)
        loop(state)

      case "3" =>
        val from = readDate("Von (YYYY-MM-DD): ")
        val to   = readDate("Bis (YYYY-MM-DD): ")
        val xs   = filterByDate(state, from, to)
        renderTxns(xs)
        println(s"Summe im Zeitraum: ${total(xs)}")
        loop(state)

      case "4" =>
        val ym = readYearMonth()
        val m  = monthlySummary(state, ym)
        renderTotals(m)
        println(s"Gesamt: ${m.values.foldLeft(BigDecimal(0))(_ + _)}")
        loop(state)

      case "5" =>
        println(s"Aktueller Saldo: ${balance(state)}")
        loop(state)

      case "6" =>
        val id   = prompt("ID: ").toIntOption.getOrElse(-1)
        val note = prompt("Neue Notiz: ")
        loop(updateNote(state, id, note))

      case "7" =>
        val id = prompt("ID: ").toIntOption.getOrElse(-1)
        loop(remove(state, id))

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

  private def renderTxns(xs: Iterable[Txn]): Unit =
    if xs.isEmpty then println("(keine)")
    else
      println(f"${"ID"}%-4s ${"Datum"}%-10s ${"Kat."}%-10s ${"Betrag"}%10s  Notiz")
      xs.toVector.sortBy(_.date).foreach { t =>
        println(f"${t.id}%-4d ${t.date}%10s ${t.category}%-10s ${t.amount}%10.2f  ${t.note}")
      }

  private def renderTotals(m: Map[Category, BigDecimal]): Unit =
    if m.isEmpty then println("(keine)")
    else m.toVector.sortBy(_._1.toString).foreach { case (c, s) =>
      println(f"$c%-10s $s%10.2f")
    }
