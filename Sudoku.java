package sudokusolver;

public class Sudoku {
    int[][] value;

    Sudoku() {
        this.value = freshValue();
    }

    Sudoku(Sudoku that) {
        this.value = that.cloneValue();
    }

    Sudoku(String s) {
        this();

        s = sanitise(s);

        int k = 0;
        while (k < size() * size()) {
            int i = k / size();
            int j = k % size();
            this.value[i][j] = Integer.parseInt("" + s.charAt(k));
            k++;
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println(new Sudoku(args[0]).answer());
        } else {
            System.out.println("usage: java Sudoku <puzzle>");
        }
    }

    public Sudoku answer() {
        if (isValid()) {
            int[] a = emptyIndex();
            if (a == null) {
                return this;
            }

            Sudoku ret;
            do {
                if (value[a[0]][a[1]]++ >= 9) {
                    value[a[0]][a[1]] = 0;
                    return null;
                }
                ret = answer();
            } while (ret == null);

            return ret;
        } else {
            return null;
        }
    }

    int[] emptyIndex() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (value[i][j] == 0) {
                    int[] a = {i, j};
                    return a;
                }
            }
        }

        return null;
    }

    public boolean isValid() {
        return uniqueRows() && uniqueColumns() && unique3x3s();
    }

    boolean uniqueRows() {
        int[] x = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                for (int k = j - 1; k >= 0; k--) {
                    if (x[k] == value[i][j] &&  0 != value[i][j]) {
                        return false;
                    }
                }
                x[j] = value[i][j];
            }
        }

        return true;
    }

    boolean uniqueColumns() {
        int[] x = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int j = 0; j < size(); j++) {
            for (int i = 0; i < size(); i++) {
                for (int k = i - 1; k >= 0; k--) {
                    if (x[k] == value[i][j] && 0 != value[i][j]) {
                        return false;
                    }
                }
                x[i] = value[i][j];
            }
        }

        return true;
    }

    boolean unique3x3s() {
        for (int a = 0; a < size(); a += subSize()) {
            for (int b = 0; b < size(); b += subSize()) {
                int[] x = {0, 0, 0, 0, 0, 0, 0, 0, 0};

                for (int i = 0; i < subSize(); i++) {
                    for (int j = 0; j < subSize(); j++) {
                        x[i + (j * 3)] = value[a + i][b + j];
                    }
                }

                for (int i = 0; i < x.length; i++) {
                    if (x[i] != 0) {
                        for (int j = i + 1; j < x.length; j++) {
                            if (x[j] == x[i]) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    String sanitise(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }

    final int size() {
        return value.length;
    }

    int subSize() {
        return 3;
    }

    int[][] freshValue() {
        int[][] ret = {{0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 3, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0}};
        return ret;
    }

    int[][] cloneValue() {
        int[][] ret = new int[9][9];

        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[i].length; j++) {
                ret[i][j] = this.value[i][j];
            }
        }

        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size(); i++) {
            if (i % subSize() == 0) {
                sb.append("+---+---+---+\n");
            }
            for (int j = 0; j < size(); j++) {
                if (j % subSize() == 0) {
                    sb.append("|");
                }
                sb.append(value[i][j]);
            }
            sb.append("|\n");
        }
        sb.append("+---+---+---+\n");

        return sb.toString();
    }
}
