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
