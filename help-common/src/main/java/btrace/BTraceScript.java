package btrace;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

/**
 * Created by fitz.li on 2018/3/16.
 */
@BTrace
public class BTraceScript {

    @OnMethod(clazz="com.qunar.flight.inter.twell.service.price.FlightListPriceProcessor", method="/.*/", location=@Location(Kind.RETURN))
    public static void run(@ProbeClassName String className, @ProbeMethodName String methodName, @Duration long time) {
        BTraceUtils.println("Btrace----------------:" + className + "." + methodName + ":" + time/1000000);
    }
}
