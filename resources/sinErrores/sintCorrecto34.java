///[SinErrores]

interface Test {}

interface Test<T> {}

interface Test extends Test {}

interface Test extends Test<T> {}

interface Test<T> extends Test {}

interface Test<T> extends Test<T> {}


class TestClass implements Test {}

class TestClass<T> implements Test<T> {}

class TestClass implements Test<T> {}

class TestClass<T> implements Test {}

