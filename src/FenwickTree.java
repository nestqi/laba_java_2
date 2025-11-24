// Реализация дерева Фенвика (Fenwick Tree) для int.
// Поддерживает:
//  - build(int[] arr)               — построение по массиву
//  - update(int index, int delta)   — добавить delta к элементу
//  - prefixSum(int index)           — сумма [0..index]
//  - rangeSum(int left, int right)  — сумма [left..right]
//
// Внутри всё хранится в массиве tree с 1-based индексацией.

public class FenwickTree {

    // Внутренний массив с частичными суммами.
    private int[] tree;

    // Размер логического массива.
    private int n;

    // Строим дерево по массиву arr.
    public void build(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array must not be null");
        }

        n = arr.length;
        tree = new int[n + 1]; // индекс 0 не используем

        // Строим через последовательные обновления.
        for (int i = 0; i < n; i++) {
            update(i, arr[i]);
        }
    }

    // Прибавляем delta к элементу с индексом
    public void update(int index, int delta) {
        checkIndex(index);

        int i = index + 1; // переходим на 1-based индекс
        while (i <= n) {
            tree[i] += delta;
            i += lsb(i); // добавляем младший значащий бит
        }
    }

    // Сумма на префиксе [0..index].
    public int prefixSum(int index) {
        checkIndex(index);

        int result = 0;
        int i = index + 1; // 1-based

        while (i > 0) {
            result += tree[i];
            i -= lsb(i); // вычитаем младший значащий бит
        }
        return result;
    }

    // Сумма на отрезке [left..right].
    public int rangeSum(int left, int right) {
        if (left < 0 || right < 0 || left >= n || right >= n) {
            throw new IndexOutOfBoundsException("Indices must be in [0, " + (n - 1) + "]");
        }
        if (left > right) {
            throw new IllegalArgumentException("left must be <= right");
        }

        if (left == 0) {
            return prefixSum(right);
        } else {
            return prefixSum(right) - prefixSum(left - 1);
        }
    }

    // Младший значащий бит числа.
    private int lsb(int x) {
        return x & -x;
    }

    // Проверка, что индекс в диапазоне [0..n-1].
    private void checkIndex(int index) {
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " is out of bounds [0, " + (n - 1) + "]"
            );
        }
    }

    // Для удобства можно посмотреть внутреннее состояние дерева.
    @Override
    public String toString() {
        if (tree == null) {
            return "FenwickTree{not built}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("FenwickTree{tree=[");
        for (int i = 1; i <= n; i++) {
            sb.append(tree[i]);
            if (i < n) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
