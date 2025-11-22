///10&15&exitosamente
class Init{
    static void m1(int p1){
        p1 = 15;
        debugPrint(p1);
    }
    static void main(){
        var x = 5;
        x = 10;
        debugPrint(x);
        System.println();
        m1(x);
    }
}