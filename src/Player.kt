class Player {
    var name: String = ""
    var field: Array<CharArray> = Array(10) { CharArray(10) { '.' } }
    var shots: Int = 0
    var hits: Int = 0
    var shipsLeft: Int = 20

    fun isAlive(): Boolean {
        return shipsLeft > 0
    }
    fun accuracy(): Double {
        if (shots == 0) return 0.0
        return hits * 100.0 / shots
    }
    fun status(): String {
        val formattedAccuracy = String.format("%.1f", accuracy())
        return "Игрок $name: палуб осталось $shipsLeft, выстрелов $shots, попаданий $hits, точность $formattedAccuracy%"
    }
    fun takeDamage(): Boolean {
        shipsLeft--
        return isAlive()
    }
    fun registerShot(hit: Boolean) {
        shots++
        if (hit) {
            hits++
        }
    }
    fun reset() {
        shots = 0
        hits = 0
        shipsLeft = 20
        for (r in 0..9) {
            for (c in 0..9) {
                field[r][c] = '.'
            }
        }
    }
    fun printField() {
        println("=== Поле игрока $name ===")
        print(" ")
        for (c in 0..9) print("$c ")
        println()
        for (r in 0..9) {
            print("$r ")
            for (c in 0..9) print("${field[r][c]} ")
            println()
        }
    }
}