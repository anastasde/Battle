fun createEmptyField(size: Int = 10): Array<CharArray> {
    return Array(size) { CharArray(size) { '.' } }
}
fun printField(field: Array<CharArray>, title: String, showShips: Boolean = true, debug: Boolean = false) {
    println("===$title===")
    val size = field.size
    if (debug) {
        val horizontal = "+---".repeat(size) + "+"
        println("    " + field.indices.joinToString("   ")) // Индексы столбцов
        println("  $horizontal")
        for (r in field.indices) {
            print("$r | ")
            for (c in field.indices) {
                val cell = field[r][c]
                val char = if (cell == '#' && !showShips) '.' else cell
                print("$char | ")
            }
            println()
            println("  $horizontal")
        }
    } else {
        print("  ")
        for (c in field.indices) print("$c ")
        println()
        for (r in field.indices) {
            print("$r ")
            for (c in field.indices) {
                val cell = field[r][c]
                val charToPrint = if (cell == '#' && !showShips) '.' else cell
                print("$charToPrint ")
            }
            println()
        }
    }
}
fun printBothFields(player: Array<CharArray>, enemy: Array<CharArray>, showEnemyShips: Boolean = false) {
    if (player.size != enemy.size || player[0].size != enemy[0].size) {
        println("Ошибка: Поля должны быть одного размера!")
        return
    }
    val size = player.size
    println("=== Ваше поле ===        === Поле противника ===")
    print("  ")
    for (c in 0..<size) print("$c ")
    print("        ")
    print("  ")
    for (c in 0..<size) print("$c ")
    println()
    for (r in 0..<size) {
        print("$r ")
        for (c in 0..<size) {
            print("${player[r][c]} ")
        }
        print("        ")
        print("$r ")
        for (c in 0..<size) {
            val cell = enemy[r][c]
            val charToPrint = if (cell == '#' && !showEnemyShips) '.' else cell
            print("$charToPrint ")
        }
        println()
    }
}
fun printFields(fields: Array<Array<CharArray>>, titles: Array<String>, showShips: BooleanArray) {
    if (fields.size != titles.size || fields.size != showShips.size) {
        println("Ошибка: Размеры массивов аргументов не совпадают!")
        return
    }
    if (fields.isEmpty()) return
    val size = fields[0].size
    for (f in fields) {
        if (f.size != size || f[0].size != size) {
            println("Ошибка: Поля имеют разный размер!")
            return
        }
    }
    for (i in fields.indices) {
        val titleText = "=== ${titles[i]} ==="
        // Рассчитываем отступ, чтобы поля визуально не слипались
        print(titleText.padEnd(size * 2 + 6))
    }
    println()
    for (i in fields.indices) {
        print("  ")
        for (c in 0..<size) print("$c ")
        print("    ")
    }
    println()
    for (r in 0..<size) {
        for (i in fields.indices) {
            print("$r ")
            for (c in 0..<size) {
                val cell = fields[i][r][c]
                val charToPrint = if (cell == '#' && !showShips[i]) '.' else cell
                print("$charToPrint ")
            }
            print("    ")
        }
        println()
    }
}
fun main() {
    val playerField = createEmptyField()
    playerField[3][2] = '#'
    playerField[3][3] = '#'
    playerField[3][4] = '#'
    playerField[3][5] = '#'
    val enemyField = createEmptyField()
    enemyField[5][7] = '#'
    enemyField[6][7] = '#'
    enemyField[7][7] = '#'
    enemyField[5][5] = 'X'
    enemyField[5][6] = 'O'
    println("Часть 1.")
    printField(playerField, "Ваше поле")
    printField(enemyField, "Поле противника", showShips = false)
    println("Часть 2.")
    val smallField = createEmptyField(8)
    printField(smallField, "Маленькое поле 8×8")
    println("Часть 3.")
    playerField[3][2] = '#'
    playerField[3][3] = '#'
    enemyField[5][7] = '#'
    enemyField[5][5] = 'X'
    printBothFields(playerField, enemyField)
    println("Часть 4.")
    printField(playerField, "Ваше поле (debug)", debug = true)
    println("Часть 5.")
    val thirdField = createEmptyField()
    thirdField[1][1] = '#'
    thirdField[2][2] = 'X'
    val fields = arrayOf(playerField, enemyField, thirdField)
    val titles = arrayOf("Ваше поле", "Противник", "Третье")
    val show = booleanArrayOf(true, false, true)
    printFields(fields, titles, show)
}
