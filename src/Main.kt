import kotlin.random.Random

fun printField(player: Player, hideShips: Boolean) {
    print("   ")
    for (c in 0..9) print("$c ")
    println()
    for (r in 0..9) {
        print("$r ")
        for (c in 0..9) {
            val state = player.field()[r][c]
            val shown = if (hideShips && state == CellState.SHIP) CellState.EMPTY else state
            print("${shown.toChar()} ")
        }
        println()
    }
}

fun printBothFields(player: Player, enemy: Player) {
    println("=== Поле игрока ${player.name} (осталось кораблей: ${player.countShips()}) ===         === Поле игрока ${enemy.name} (осталось кораблей: ${enemy.countShips()}) ===")
    print("   ")
    for (c in 0..9) print("$c ")
    print("      ")
    print("   ")
    for (c in 0..9) print("$c ")
    println()
    for (r in 0..9) {
        print("$r ")
        for (c in 0..9) print("${player.field()[r][c].toChar()} ")
        print("     ")
        print("$r ")
        for (c in 0..9) {
            val state = enemy.field()[r][c]
            val shown = if (state == CellState.SHIP) CellState.EMPTY else state
            print("${shown.toChar()} ")
        }
        println()
    }
}

fun printRules() {
    println("=========================================")
    println("        МОРСКОЙ БОЙ - ПРАВИЛА")
    println("=========================================")
    println("Поле 10×10. Координаты: сначала строка (0-9), потом столбец (0-9).")
    println("Для выхода из игры введите 'quit'.")
    println()
    println("Корабли, которые нужно расставить:")
    println("   1 корабль × 4 палубы")
    println("   2 корабль × 3 палубы")
    println("   3 корабль × 2 палубы")
    println("   4 корабль × 1 палубы")
    println("Итого: 10 кораблей, 20 палуб.")
    println("=========================================")
}

fun placePlayerShips(player: Player) {
    val plan = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    var index = 0

    while (index < plan.size) {
        val expectedSize = plan[index]
        println()
        println("Ставим корабль #${index + 1} из ${plan.size} (${expectedSize} палуб)")
        printField(player, false)

        println("Введите начальную координату (row col):")
        val inputLine = readln().trim()
        if (inputLine.lowercase() == "quit") {
            println("Вы вышли из игры")
            return
        }

        val input = inputLine.split(" ")
        if (input.size != 2) {
            println("Ошибка: нужно два числа через пробел.")
            continue
        }
        val startRow = input[0].toIntOrNull()
        val startCol = input[1].toIntOrNull()
        if (startRow == null || startCol == null) {
            println("Ошибка: координаты должны быть числами.")
            continue
        }

        println("Введите тип корабля (1, 2, 3 или 4):")
        val size = readln().trim().toIntOrNull()
        if (size == null || size !in 1..4) {
            println("Ошибка: тип корабля должен быть 1, 2, 3 или 4.")
            continue
        }
        if (size != expectedSize) {
            println("Сейчас нужно ставить корабль из $expectedSize палуб, а не $size")
            continue
        }

        println("Введите направление (h – вправо, v – вниз):")
        val dir = readln().trim().lowercase()
        if (dir != "h" && dir != "v") {
            println("Ошибка: направление должно быть h или v.")
            continue
        }
        val horizontal = (dir == "h")

        val ok = player.placeShip(startRow, startCol, size, horizontal)
        if (ok) {
            println("Корабль поставлен!")
            index++
        } else {
            println("Нельзя поставить корабль здесь. Попробуйте снова.")
        }
    }
    println()
    println("Все корабли расставлены!")
    printField(player, false)
}

fun placeEnemyShips(enemy: Player) {
    val plan = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    val maxAttempts = 1000

    var attempt = 0
    var index = 0
    while (index < plan.size) {
        val size = plan[index]
        val startRow = Random.nextInt(0, 10)
        val startCol = Random.nextInt(0, 10)
        val horizontal = Random.nextBoolean()

        val ok = enemy.placeShip(startRow, startCol, size, horizontal)
        if (ok) {
            index++
            attempt = 0
        } else {
            attempt++
            if (attempt > maxAttempts) {
                enemy.reset()
                index = 0
                attempt = 0
            }
        }
    }
    println("Компьютер расставил корабли.")
}

fun main() {
    val player = Player("Вы")
    val enemy = Player("Компьютер")

    printRules()

    println("=== РАССТАНОВКА ВАШИХ КОРАБЛЕЙ ===")
    placePlayerShips(player)
    if (!player.hasShips()) return

    println()
    println("=== КОМПЬЮТЕР РАССТАВЛЯЕТ КОРАБЛИ ===")
    placeEnemyShips(enemy)

    println()
    println("=== ИГРА НАЧАЛАСЬ ===")

    var turnsCount = 0

    while (player.hasShips() && enemy.hasShips()) {
        printBothFields(player, enemy)
        println()

        while (true) {
            println("Ваш ход. Введите координаты выстрела (row col) или 'quit' для выхода:")
            val inputLine = readln().trim()
            if (inputLine.lowercase() == "quit") {
                println("Вы вышли из игры")
                return
            }

            val input = inputLine.split(" ")
            if (input.size != 2) {
                println("Ошибка: нужно два числа через пробел.")
                continue
            }
            val r = input[0].toIntOrNull()
            val c = input[1].toIntOrNull()
            if (r == null || c == null) {
                println("Ошибка: координаты должны быть числами.")
                continue
            }

            if (!enemy.isInside(r, c)) {
                println("Ошибка: выстрел за границей поля! Попробуйте снова.")
                continue
            }
            if (enemy.field()[r][c] == CellState.HIT || enemy.field()[r][c] == CellState.MISS) {
                println("Ошибка: сюда уже стреляли! Попробуйте снова.")
                continue
            }

            turnsCount++
            val hit = enemy.shoot(r, c)
            if (hit) {
                println("Попадание!")
                if (!enemy.hasShips()) break
                printBothFields(player, enemy)
                println()
            } else {
                println("Промах!")
                break
            }
        }

        if (!enemy.hasShips()) break

        println("Ход компьютера...")
        var compRow: Int
        var compCol: Int
        do {
            compRow = Random.nextInt(0, 10)
            compCol = Random.nextInt(0, 10)
        } while (player.field()[compRow][compCol] == CellState.HIT || player.field()[compRow][compCol] == CellState.MISS)

        val compHit = player.shoot(compRow, compCol)
        if (compHit) {
            println("Компьютер попал в ($compRow, $compCol)!")
        } else {
            println("Компьютер промахнулся в ($compRow, $compCol).")
        }
        println()
    }

    printBothFields(player, enemy)
    println()
    println("Игра длилась $turnsCount ходов.")
    if (player.hasShips()) {
        println("Вы победили!")
    } else {
        println("Победил компьютер!")
    }
}
