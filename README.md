# Budget Planner – Scala

Simon Schmid & Kai Imseng

---

## Funktionen

- Transaktionen hinzufügen, anzeigen, löschen
- Filtern nach Zeitraum
- Summen pro Kategorie und Monat
- Automatische Saldo-Berechnung
- Notizen hinzufügen oder ändern
- Menüführung über Tastatureingabe

---

## Rekursive Funktionen

**Tail-rekursive Hauptschleife**
(`Main.scala`)
```scala
@annotation.tailrec
def loop(state: State): Unit =
  println("[1] Hinzufügen ... [0] Beenden")
  readLine() match
    case "0" => println("Bye!")
    case _   => loop(state)
```

**Eingabevalidierung mit Rekursion**
```scala
private def readDate(msg: String): LocalDate =
  scala.util.Try(LocalDate.parse(prompt(msg))).getOrElse {
    println("Ungültig."); readDate(msg)
  }
```

---

## Aufbau

**Main.scala**
- Menülogik und Benutzerinteraktion
- Rekursive Eingabeprüfungen

**Model.scala**
- Datenmodell (`Txn`, `Category`, `State`)
- Funktionen zum Filtern, Summieren und Aktualisieren

---

## Starten

```bash
sbt compile
sbt run
```
