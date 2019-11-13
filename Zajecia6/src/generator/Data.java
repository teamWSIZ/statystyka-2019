package generator;

public class Data {
    private double data[];
    private double dataNorm[];

    int noBins = 100;

    double min = 0, max = 1;

    public Data() {
        data = new double[noBins];
        dataNorm = new double[noBins];
    }

    public Data(int number, double min, double max) {
        noBins = number;

        data = new double[noBins];
        dataNorm = new double[noBins];

        this.min = min;
        this.max = max;
    }

    public void fill(double number, double weight) {
        if (number <= min || number > max)
            return;

        double dx = (max - min) / noBins;
        int no = (int) Math.ceil((number + min) / dx) - 1;

        data[no] += weight;
    }

    public int getNoBins() {
        return noBins;
    }

    public double getBinX(int bin) {
        double dx = (max - min) / noBins;

        return min + dx * bin + 0.5 * dx;
    }

    public double getMax() {
        double max = data[0];

        for (double value : data) {
            if (value > max)
                max = value;
        }

        return max;
    }

    public void normalize() {
        double max = getMax();

        if (max == 0)
            max = 1;

        for (int i = 0; i < noBins; i++)
            dataNorm[i] = data[i] / max;

    }

    public double[] getDataNorm() {

        return dataNorm;
    }

    public double[] getData() {
        return data;
    }
}
