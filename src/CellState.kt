enum class CellState {
    EMPTY,
    SHIP,
    HIT,
    MISS;

    fun toChar(): Char {
        return when (this) {
            CellState.EMPTY -> '.'
            CellState.SHIP -> '#'
            CellState.HIT -> 'X'
            CellState.MISS -> 'O'
        }
    }
}