///[SinErrores]

interface Test {
    void metodo();

    public void metodoPublico();

    int metodoConRetorno();

    public int metodoPublicoConRetorno();

    Integer metodoConRetornoObjeto();

    public Integer metodoPublicoConRetornoObjeto();

    Generic<T> metodoConRetornoGenerico();

    public Generic<T> metodoPublicoConRetornoGenerico();

    int argumentos(int a, char b, boolean c);

    void argumentos(int a, String b, Generic<T> c);
}

