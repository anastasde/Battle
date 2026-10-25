import kotlin.random.Random

fun printField(player: Player, hideShips: Boolean) {
    print("  ")
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
    val playerShips = player.countShips()
    val enemyShips = enemy.countShips()
    println("=== Поле ${player.name} (Кораблей: $playerShips) ===      === Поле ${enemy.name} (Кораблей: $enemyShips) ===")
    print("  ")
    for (c in 0..9) print("$c ")
    print("   ")
    print("  ")
    for (c in 0..9) print("$c ")
    println()
    for (r in 0..9) {
        print("$r ")
        for (c in 0..9) print("${player.field()[r][c].toChar()} ")
        print("   ")
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
    println()
    println("Правила расстановки:")
    println(" • Корабли не могут выходить за границы поля.")
    println(" • Корабли не могут пересекаться.")
    println(" • Корабли не могут касаться друг друга даже углами.")
    println()
    println("Как вводить корабль:")
    println(" 1. Введите координату НАЧАЛЬНОЙ палубы: row col")
    println("    Например: 3 4 – строка 3, столбец 4.")
    println(" 2. Введите тип корабля: 1, 2, 3 или 4 (число палуб).")
    println(" 3. Введите направление:")
    println("    h – горизонтально вправо (→)")
    println("    v – вертикально вниз (↓)")
    println()
    println("Пример: корабль из 3 палуб, начало (2, 5), горизонтально.")
    println(" Ввод: 2 5, затем 3, затем h.")
    println(" Займёт клетки: (2,5), (2,6), (2,7).")
    println()
    println("После каждого корабля поле перерисовывается.")
    println("=========================================")
    println()
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
        if (expectedSize != 1) {
            println("Введите направление (h – вправо, v – вниз):")
            val dir = readln().trim().lowercase()
            if (dir != "h" && dir != "v") {
                println("Ошибка: направление должно быть h или v.")
                continue
            }
            val horizontal = (dir == "h")
            val ok = player.placeShip(startRow, startCol, expectedSize, horizontal)
            if (ok) {
                println("Корабль поставлен!")
                index++
            } else {
                println("Нельзя поставить корабль здесь. Попробуйте снова.")
            }
        } else {
            val horizontal = true
            val ok = player.placeShip(startRow, startCol, expectedSize, horizontal)
            if (ok) {
                println("Корабль поставлен!")
                index++
            } else {
                println("Нельзя поставить корабль здесь. Попробуйте снова.")
            }
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
    var playerTurn = true
    var turns = 0
    while (player.hasShips() && enemy.hasShips()) {
        printBothFields(player, enemy)
        println()
        if (playerTurn) {
            println("Ваш ход. Введите координаты выстрела (row col) или 'quit' для выхода:")
            val line = readln().trim()
            if (line.lowercase() == "quit") {
                println("Вы вышли из игры")
                break
            }
            val input = line.split("\\s+".toRegex())
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
            if (r !in 0..9 || c !in 0..9) {
                println("Ошибка: координаты вне диапазона 0..9.")
                continue
            }
            val targetCell = enemy.field()[r][c]
            if (targetCell == CellState.HIT || targetCell == CellState.MISS) {
                println("Ошибка: вы уже стреляли в эту клетку!")
                continue
            }
            val hit = enemy.shoot(r, c)
            if (hit) {
                turns++
                print("Попадание!")
            } else {
                turns++
                print("Промах.")
                playerTurn = false
            }
        } else {
            println("Ход компьютера...")
            var r: Int
            var c: Int
            do {
                r = Random.nextInt(0, 10)
                c = Random.nextInt(0, 10)
            } while (player.field()[r][c] == CellState.HIT || player.field()[r][c] == CellState.MISS)
            val hit = player.shoot(r, c)
            if (hit) {
                println("Компьютер попал в ($r, $c)!")
            } else {
                println("Компьютер промахнулся в ($r, $c).")
                playerTurn = true
            }
        }
        println()
    }
    println("Игра длилась $turns ходов.")
    if (player.hasShips()) {
        println("Вы победили!")
    } else {
        println("Победил компьютер!")
    }
}
