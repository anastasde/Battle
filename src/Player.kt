class Player(val name: String) {
    private var _field: Array<Array<CellState>> =
        Array(size = 10) { Array(10) { CellState.EMPTY} }

    fun field(): Array<Array<CellState>> = _field

    private var _shots: Int = 0
    private var _hits: Int = 0

    val shots: Int
    get() = _shots
    val hits: Int
    get() = _hits

    fun accuracy(): Double =
        if (_shots == 0) 0.0 else _hits * 100.0 / _shots

    fun registerShot(hit: Boolean)
    {
        _shots++
        if (hit) _hits++
    }

    fun markShot(row: Int, col: Int, hit: Boolean) {
        if (row in 0 .. 9 && col in 0 .. 9){
            _field[row][col] =
                if (hit) CellState.HIT else CellState.MISS
        }
    }

    fun reset() {
        _shots = 0
        _hits = 0
        for (r in 0..9) {
            for (c in 0..9) {
                _field[r][c] = CellState.EMPTY
            }
        }
    }
    fun status(): String {
        val acc = String.format("%.1f", accuracy())
        return "Игрок $name: выстрелов $shots, попаданий $hits, точность $acc%"
    }

    fun isInside(row: Int, col: Int): Boolean {
        return row in 0 .. 9 && col in 0 .. 9
    }

    fun countShips(): Int {
        var count = 0
        for (r in 0..9) {
            for (c in 0..9) {
                if (_field[r][c] == CellState.SHIP) {
                    val hasLeft = c > 0 && _field[r][c - 1] == CellState.SHIP
                    val hasTop = r > 0 && _field[r - 1][c] == CellState.SHIP
                    if (!hasLeft && !hasTop) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun canPlaceShip(startRow: Int, startCol: Int, size: Int, horizontal: Boolean): Boolean {
        for (i in 0..<size) {
            val r = if (horizontal) startRow else startRow + i
            val c = if (horizontal) startCol + i else startCol

            if (!isInside(r, c)) return false

            if (_field[r][c] != CellState.EMPTY) return false

            for (dr in -1 .. 1) {
                for (dc in -1 .. 1) {
                    if (dr == 0 && dc == 0) continue
                    val nr = r + dr
                    val nc = c + dc
                    if (isInside(nr, nc) && _field[nr][nc] == CellState.SHIP) return false
                }
            }
        }
        return true
    }

    fun hasShips(): Boolean {
        for (r in 0..9) {
            for (c in 0..9) {
                if (_field[r][c] == CellState.SHIP) return true
            }
        }
        return false
    }

    fun placeShip(startRow: Int, startCol: Int, size: Int, horizontal: Boolean): Boolean {
        if (!canPlaceShip(startRow, startCol, size, horizontal))
        {
            return false
        }
        for (i in 0..<size) {
            val r = if (horizontal) startRow else startRow + i
            val c = if (horizontal) startCol + i else startCol
            _field[r][c] = CellState.SHIP
        }
        return true
    }

    fun shoot(row: Int, col: Int): Boolean {
        if (!isInside(row, col)) {
            println("Выстрел за границей поля!")
            return false
        }
        val current = _field[row][col]
        if (current == CellState.HIT || current == CellState.MISS) {
            println("Сюда ужу стреляли!")
            return false
        }
        val hit = (current == CellState.SHIP)
        _field[row][col] = if (hit) CellState.HIT else CellState.MISS
        registerShot(hit)
        return hit
    }
}