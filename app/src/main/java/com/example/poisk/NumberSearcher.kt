package com.example.poisk

object NumberSearcher {

    const val ARRAY_SIZE = 1000
    const val MIN_VALUE = 1
    const val MAX_VALUE = 10000

    // Генерируем массив из 1000 случайных чисел
    fun generateArray(): IntArray {
        return IntArray(ARRAY_SIZE) { (MIN_VALUE..MAX_VALUE).random() }
    }

    // Подготавливаем отсортированные данные с сохранением исходных индексов.
    // Это нужно чтобы потом делать быстрый поиск (бинарный), но при равном
    // расстоянии выбирать то число, которое стоит раньше в исходном массиве.
    fun prepareSortedData(array: IntArray): Array<Pair<Int, Int>> {
        // Pair(значение, исходный_индекс)
        return Array(array.size) { i -> Pair(array[i], i) }
            .sortedBy { it.first }
            .toTypedArray()
    }

    // Ищем ближайшее число бинарным поиском — O(log n), не перебираем всё
    fun findClosest(target: Int, sortedData: Array<Pair<Int, Int>>): Int {
        if (sortedData.isEmpty()) return -1

        var left = 0
        var right = sortedData.size - 1

        // Бинарный поиск: ищем позицию куда вставился бы target
        while (left < right) {
            val mid = (left + right) / 2
            if (sortedData[mid].first < target) {
                left = mid + 1
            } else {
                right = mid
            }
        }

        // После поиска left — это первый элемент >= target
        // Кандидаты: left (первый >= target) и left-1 (последний < target)
        val candidates = mutableListOf<Pair<Int, Int>>()
        if (left < sortedData.size) {
            candidates.add(sortedData[left])
        }
        if (left > 0) {
            candidates.add(sortedData[left - 1])
        }

        // Выбираем кандидата с наименьшим расстоянием.
        // При равном расстоянии — тот, у кого меньше исходный индекс (встретился раньше).
        val best = candidates.minWith(
            compareBy({ Math.abs(it.first - target) }, { it.second })
        )

        return best?.first ?: -1
    }
}
