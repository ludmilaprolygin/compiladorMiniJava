package main;

public class TestMain {
    public static void main(String[] args) {
        var i = 0;
        var suma = 1;
        System.out.println(i);
        System.out.println(suma);
        suma = 0;

        while(i < 5) {
            suma = suma + i;
            i = i + 1;
            System.out.println(i);
        }

        System.out.println(suma); // Debería imprimir 0+1+2+3+4 = 10
    }
}
