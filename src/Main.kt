fun printBothFields(player: Player, enemy: Player) {
    println("=== Поле игрока ${player.name} ===      === Поле игрока ${enemy.name} ===")
    print("  ")
    for (c in 0..9) print("$c ")
    print("   ")
    print("  ")
    for (c in 0..9) print("$c ")
    println()
    for (r in 0..9) {
        print("$r ")
        for (c in 0..9) print("${player.field()[r][c]} ")
        print("   ")
        print("$r ")
        for (c in 0..9) print("${enemy.field()[r][c]} ")
        println()
    }
}
fun main() {
    val player = Player("Вы")
    val enemy = Player("Компьютер")

    player.placeShip(3, 2)
    player.placeShip(3, 3)
    player.placeShip(3, 4)
    player.placeShip(3, 5)

    enemy.placeShip(5, 7)
    enemy.placeShip(6, 7)
    enemy.placeShip(7, 7)

    player.registerShot(true)
    player.registerShot(false)
    player.registerShot(true)

    player.markShot(3, 3, true)
    printBothFields(player, enemy)
    println(player.status())

    println("Выстрелов: ${player.shots}")
    println("Попаданий: ${player.hits}")
    player.reset()
    println(player.status())
}
