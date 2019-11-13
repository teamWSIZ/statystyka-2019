package generator;

public class Generator {
    public static double simple(){
        return Math.random();
    }
    public static double gen0(){
        return 0.5;
    }

    public static double gen1(){
        double number = Math.random();

        if(number>=0&&number<=0.2)
            return 0.1;
        if(number>=0.2&&number<=0.4)
            return 0.3;
        if(number>=0.4&&number<=0.6)
            return 0.5;
        if(number>=0.6&&number<=0.8)
            return 0.7;
        if(number>=0.8&&number<=1.0)
            return 0.9;

        return 0;
    }

    public static double f(double x){
        return x;
    }

    public static double gen2(){
        double number,y,x;

        do {
            number = Math.random();
            x = Math.random();
            y = Generator.f(x);
        }while(number<y);

        return number;

    }

    public static double gen2a(){

        double u = Math.random();

        return -Math.log(1-u);
    }
}
