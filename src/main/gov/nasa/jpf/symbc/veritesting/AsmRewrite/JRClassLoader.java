package gov.nasa.jpf.symbc.veritesting.AsmRewrite;


//creates a new class dynamically.
public class JRClassLoader extends ClassLoader{
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

    public static Class<?> createClass(String name, byte[] b){
        return new JRClassLoader().defineClass(name, b);
    }
}

