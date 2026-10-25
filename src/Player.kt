class Player(val name: String) {
    private val _field: Array<CharArray> = Array(10) { CharArray(10) { '.' } }

    fun field(): Array<CharArray> = _field

    private var _shots: Int = 0
    private var _hits: Int = 0

    val shots: Int
        get() = _shots
    val hits: Int
        get() = _hits

    fun accuracy(): Double =
        if (_shots == 0) 0.0 else _hits * 100.0 / _shots

    fun registerShot(hit: Boolean) {
        _shots++
        if (hit) _hits++
    }
    fun placeShip(row: Int, col: Int) {
        if (row in 0..9 && col in 0..9) {
            _field[row][col] = '#'
        }
    }
    fun markShot(row: Int, col: Int, hit: Boolean) {
        if (row in 0..9 && col in 0..9) {
            _field[row][col] = if (hit) 'X' else 'O'
        }
    }
    fun reset() {
        _shots = 0
        _hits = 0
        for (r in 0..9) {
            for (c in 0..9) {
                _field[r][c] = '.'
            }
        }
    }
    fun status(): String {
        val acc = String.format("%.1f", accuracy())
        return "Игрок $name: выстрелов $_shots, попаданий $_hits, точность $acc%"
    }
}
