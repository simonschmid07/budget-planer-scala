name := "BudgetPlanner"
version := "0.1.0"
scalaVersion := "3.3.1"

// ENTWEDER: forken + Input weiterreichen
Compile / run / fork := true
Compile / run / connectInput := true

// (Optional) UTF-8 erzwingen, siehe Punkt 2
Compile / run / javaOptions += "-Dfile.encoding=UTF-8"
