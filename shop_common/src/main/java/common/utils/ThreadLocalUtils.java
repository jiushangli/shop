package common.utils;

public class ThreadLocalUtils {
    private ThreadLocalUtils() {

    }

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    /**
     * 对当前线程中的map域设置属性值，不会对其他线程产生影响  Thread.currentThread()
     * @param str
     */
    public static void set(String str){

        threadLocal.set(str);
    }

    public static String get(){

        return threadLocal.get();
    }

    /**
     * 本质上是调用当前线程的remove，不会对其他线程产生影响
     */
    public static void remove(){

        threadLocal.remove();
    }
}
