///200&30&exitosamente
class Init{
    static void main(){
        var x = 10;
        var y = 20;

        var a = true ? (x * y) : (x + y);
        debugPrint(a);

        var b = false ? (x * y) : (x + y);
        debugPrint(b);
    }
}