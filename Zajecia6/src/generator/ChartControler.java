package generator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.DecimalFormat;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ChartControler {

    @FXML
    private ScatterChart randomChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            generateData();
        }
    };
    private Thread generatorThread = new Thread(runnable);

    private ReentrantLock lock = new ReentrantLock();
    private Condition generatorCondition = lock.newCondition();

    private boolean generate = false;


    XYChart.Series series, series1;

    @FXML
    public void initialize() {
        series = new XYChart.Series();
        series1 = new XYChart.Series();

        randomChart.setAnimated(false);
        randomChart.setLegendVisible(true);

        randomChart.getData().addAll(series);

        generatorThread.setDaemon(true);
        generatorThread.start();
    }

    public void startGenerator() {
        lock.lock();
        generate = true;
        generatorCondition.signal();
        lock.unlock();
    }

    public void stopGenerator() {
        lock.lock();
        generate = false;
        lock.unlock();
    }

    public double x2(Data data){
        double sum = 0;

        for(int i=0;i<data.getNoBins();i++){
            double s = data.getDataNorm()[i] - data.getBinX(i);
            sum+=s*s;
        }

        return sum;
    }

    public void generateData() {
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();

        Data data = new Data(100, 0, 1);
        Data data1 = new Data(100,0,1);

        do {
            lock.lock();
            try {
                if (!generate)
                    generatorCondition.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            for (int i = 0; i < 10; i++) {

                data.fill(Generator.gen2(), 1);
                data1.fill(Generator.gen2(), 1);

            }

            data.normalize();
            data1.normalize();

            Double x2_gen1_number = x2(data);
            Double x2_gen2_number = x2(data1);

            DecimalFormat df2 = new DecimalFormat("#.##");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    series.getData().clear();
                    series1.getData().clear();

                    for (int i = 0; i < data.getNoBins(); i++) {

                        XYChart.Data point = new XYChart.Data(data.getBinX(i), data.getDataNorm()[i]/data.getBinX(i));
                        point.setNode(new Circle(2, Color.ORANGE));

                        series.getData().add(point);
                    }

                    for (int i = 0; i < data1.getNoBins(); i++) {

                        XYChart.Data point = new XYChart.Data(data1.getBinX(i), data1.getDataNorm()[i]/data1.getBinX(i));
                        point.setNode(new Circle(2, Color.TEAL));

                        series1.getData().add(point);
                    }

                    randomChart.getData().clear();

                    randomChart.getData().add(series);
                    randomChart.getData().add(series1);

                    series.setName("gen 1:"+df2.format(x2_gen1_number));
                    series1.setName("gen 2:"+df2.format(x2_gen2_number));


                }
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);

    }


}
