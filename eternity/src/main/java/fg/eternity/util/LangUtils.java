package fg.eternity.util;

import fg.eternity.exception.AppException;
import fg.eternity.plan.IPlan;
import fg.eternity.validator.ValidationException;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Helper class to share often used methods
 */
public class LangUtils {

    /**
     * Load resource from classpath
     * @param src Source file path
     * @return
     */
    public static String getResource(String src) {
        return getResource(LangUtils.class, src);
    }

    /**
     * Load resource from classpath
     * @param clazz Class used to relativly define resource path
     * @param src Source file path
     * @return
     */
    public static String getResource(Class clazz, String src) {
        return sneakyThrows(() -> {
            try (InputStream is = clazz.getResourceAsStream(src)) {
                return IOUtils.toString(is, "UTF8");
            }
        });
    }

    /**
     * Callback Interface which drops (checked/non-checked) exception
     * @param <T>
     */
    public interface CallBack<T> {
        T call() throws Exception;
    }

    /**
     * Callback Interface which drops (checked/non-checked) exception
     */
    public interface CallBackV {
        void call() throws Exception;
    }


    /**
     * Method is used to catch checked/non-checked exceptions on standard way for all application
     * @param callBack  Usually Lambda expression to call back
     * @param <T> Return type
     * @return Returned value by callback
     */
    public static <T> T sneakyThrows(CallBack<T> callBack) {
        try {
            //Callback
            return callBack.call();
        } catch (ValidationException|AppException e) {
            //Do not transfer any application exception
            throw e;
        } catch (Exception e) {
            //Transfer unknown exception to unchecked AppException
            throw new AppException(e);
        }
    }

    /**
     * Method is used to catch checked/non-checked exceptions on standard way for all application
     * @param callBack  Usually Lambda expression to call back
     */
    public static void sneakyThrows( CallBackV callBack) {
        try {
            //Callback
            callBack.call();
        } catch (ValidationException|AppException e) {
            //Do not transfer any application exception
            throw e;
        } catch (Exception e) {
            //Transfer unknown exception to unchecked AppException
            throw new AppException(e);
        }
    }

    public static <T> T deepCopy(T toCopy){
        final byte[] serializedObject = sneakyThrows(() -> {
            try(ByteArrayOutputStream os=new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(os)){
                objectOutputStream.writeObject(toCopy);
                return os.toByteArray();
            }
        });

        return sneakyThrows(() -> {
            try(ByteArrayInputStream is=new ByteArrayInputStream(serializedObject); ObjectInputStream objectInputStream = new ObjectInputStream(is);  ){
                return (T)objectInputStream.readObject();
            }
        });
    }

}
