enum class CellState {
    EMPTY,
    SHIP,
    HIT,
    MISS;
    fun toChar(): Char {
        return when (this) {
            EMPTY -> '.'
            SHIP -> '#'
            HIT -> 'X'
            MISS -> 'O'
        }
    }
}