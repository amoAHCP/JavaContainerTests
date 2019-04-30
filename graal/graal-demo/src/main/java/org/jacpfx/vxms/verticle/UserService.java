package org.jacpfx.vxms.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.Pointer;

import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class UserService {

    public static String getAllUsers() {
        ByteBuffer res = getAllUsersBuffer();
        String s = StandardCharsets.UTF_8.decode(res).toString();
        System.out.println("FINAL RESULT: "+s);
        return s;
    }

    private static ByteBuffer getAllUsersBuffer() {
        /* Create a new isolate for the function evaluation and rendering. */
        IsolateThread renderingContext = Isolates.createIsolate(Isolates.CreateIsolateParameters.getDefault());



        /* Render the function. This call performs the transition from the Netty isolate the rendering isolate, triggered by the annotations of plotAsSVG. */
        ObjectHandle resultHandle = loadAll(renderingContext);
        System.out.println("ObjectHandle OK");
        /* Resolve and delete the resultHandle, now that execution is back in the Netty isolate. */
        ByteBuffer result = ObjectHandles.getGlobal().get(resultHandle);

        System.out.println("ResultBuffer OK");
        ObjectHandles.getGlobal().destroy(resultHandle);
        System.out.println("Destroy OK");
        /* Tear down the isolate, freeing all the temporary objects. */
        Isolates.tearDownIsolate(renderingContext);
        System.out.println("Tear Down OK");
        printMemoryUsage("Vertx isolate memory usage: ", 0);

        return result;
    }

    @CEntryPoint
    private static ObjectHandle loadAll(@CEntryPoint.IsolateThreadContext IsolateThread renderingContext) {
        long initialMemory = printMemoryUsage("Rendering isolate initial memory usage: ", 0);


        LoadUsers lookup = ImageSingletons.lookup(LoadUsers.class);
        System.out.println("LoadUsers : "+lookup);
        byte[] svgBytes = lookup.loadAll();

        ObjectHandle byteBufferHandle;
        try (PinnedObject pin = PinnedObject.create(svgBytes)) {
            byteBufferHandle = createByteBuffer(renderingContext,pin.addressOfArrayElement(0), svgBytes.length);
        }

        printMemoryUsage("Rendering isolate final memory usage: ", initialMemory);

        return byteBufferHandle;
    }


    @CEntryPoint
    private static ObjectHandle createByteBuffer(@CEntryPoint.IsolateThreadContext IsolateThread renderingContext,Pointer address, int length) {
        ByteBuffer direct = CTypeConversion.asByteBuffer(address, length);
        ByteBuffer copy = ByteBuffer.allocate(length);
        copy.put(direct).rewind();
        return ObjectHandles.getGlobal().create(copy);
    }

    private static long printMemoryUsage(String message, long initialMemory) {
        long currentMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        System.out.println(message + currentMemory / 1024 + " KByte" + (initialMemory == 0 ? "" : "  (difference: " + (currentMemory - initialMemory) / 1024 + " KByte)"));
        return currentMemory;
    }



}
