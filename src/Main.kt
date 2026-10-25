fun main() {
    fun printBothFields(player: Player, enemy: Player) {
        println("=== Поле игрока ${player.name} ===      === Поле игрока ${enemy.name} ===")
        print(" ")
        for (c in 0..9) print("$c ")
        print("   ")
        print(" ")
        for (c in 0..9) print("$c ")
        println()
        for (r in 0..9) {
            print("$r ")
            for (c in 0..9) print("${player.field[r][c]} ")
            print("   ")
            print("$r ")
            for (c in 0..9) print("${enemy.field[r][c]} ")
            println()
        }
    }

    val player = Player()
    player.name = "Вы"

    player.registerShot(true)
    player.registerShot(false)
    player.registerShot(true)

    println(player.status())

    player.takeDamage()
    player.takeDamage()
    println(player.shipsLeft)
    println(player.isAlive())

    player.reset()
    println(player.status())
}