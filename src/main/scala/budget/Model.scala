package budget

import java.time.{LocalDate, YearMonth}
import java.time.format.DateTimeFormatter

enum Category:
  case Income, Rent, Food, Transport, Leisure, Health, Utilities, Other

case class Txn(
  id: Int,
  amount: BigDecimal,
  category: Category,
  date: LocalDate,
  note: String
)

case class State(txns: Vector[Txn], nextId: Int = 1)

object Model:
  private val df = DateTimeFormatter.ISO_LOCAL_DATE

  val empty: State = State(Vector.empty, 1)

  def add(s: State, amount: BigDecimal, cat: Category, date: LocalDate, note: String): State =
    val t = Txn(s.nextId, amount, cat, date, note)
    s.copy(txns = s.txns :+ t, nextId = s.nextId + 1)

  def remove(s: State, id: Int): State =
    s.copy(txns = s.txns.filterNot(_.id == id))

  def updateNote(s: State, id: Int, note: String): State =
    s.copy(txns = s.txns.map(t => if t.id == id then t.copy(note = note) else t))

  def filterByDate(s: State, from: LocalDate, to: LocalDate): Vector[Txn] =
    s.txns.filter(t => !t.date.isBefore(from) && !t.date.isAfter(to))

  def filterByCategory(s: State, cat: Category): Vector[Txn] =
    s.txns.filter(_.category == cat)

  def total(txns: Iterable[Txn]): BigDecimal =
    txns.foldLeft(BigDecimal(0))((acc, t) => acc + t.amount)

  def totalsByCategory(txns: Iterable[Txn]): Map[Category, BigDecimal] =
    txns.groupBy(_.category).view.mapValues(total).toMap

  def balance(s: State): BigDecimal = total(s.txns)

  def monthlySummary(s: State, ym: YearMonth): Map[Category, BigDecimal] =
    val monthTxns = s.txns.filter(t => YearMonth.from(t.date) == ym)
    totalsByCategory(monthTxns)
