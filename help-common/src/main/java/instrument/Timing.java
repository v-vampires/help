package instrument;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Created by fitz.li on 2017/3/9.
 */
public class Timing implements ClassFileTransformer {

    public Timing(String methodName) {
        this.methodName = methodName;
    }

    private String methodName;

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            ClassParser cp = new ClassParser(new ByteArrayInputStream(classfileBuffer), className + ".java");
            JavaClass javaClass = cp.parse();
            ClassGen classGen = new ClassGen(javaClass);
            Method[] methods = javaClass.getMethods();
            int index;
            for (index = 0; index < methods.length; index++) {
                if (methods[index].getName().equals(methodName)) {
                    break;
                }
            }
            if (index < methods.length) {
                addTimer(classGen, methods[index]);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                classGen.getJavaClass().dump(bos);
                return bos.toByteArray();
            }
            System.err.println("Method " + methodName + " not found in " + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addTimer(ClassGen classGen, Method method) {
        InstructionFactory ifact = new InstructionFactory(classGen);
        InstructionList ilist = new InstructionList();
        String className = classGen.getClassName();
        ConstantPoolGen constantPool = classGen.getConstantPool();
        MethodGen wrapGen = new MethodGen(method, className, constantPool);
        wrapGen.setInstructionList(ilist);

        MethodGen methodGen = new MethodGen(method, className, constantPool);
        classGen.removeMethod(method);
        String iname = method.getName() + "_timing";
        methodGen.setName(iname);
        classGen.addMethod(methodGen.getMethod());
        Type returnType = methodGen.getReturnType();

        Type[] argumentTypes = methodGen.getArgumentTypes();
        int stackIndex = methodGen.isStatic() ? 0 : 1;
        for (int i = 0; i < argumentTypes.length; i++) {
            stackIndex += argumentTypes[i].getSize();
        }

        ilist.append(ifact.createInvoke("java.lang.System", "currentTimeMillis", Type.LONG, Type.NO_ARGS, Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.createStore(Type.LONG, stackIndex));

        int offset = 0;
        short invoke = Constants.INVOKESTATIC;
        if (!methodGen.isStatic()) {
            ilist.append(InstructionFactory.createLoad(Type.OBJECT, 0));
            offset = 1;
            invoke = Constants.INVOKEVIRTUAL;
        }
        for (Type argumentType : argumentTypes) {
            ilist.append(InstructionFactory.createLoad(argumentType, offset));
            offset += argumentType.getSize();
        }
        ilist.append(ifact.createInvoke(className, iname, returnType, argumentTypes, invoke));
        if (returnType != Type.VOID) {
            ilist.append(InstructionFactory.createStore(returnType, stackIndex + 2));
        }

        // print time required for method call
        ilist.append(ifact.createFieldAccess("java.lang.System",
                "out", new ObjectType("java.io.PrintStream"),
                Constants.GETSTATIC));

        ilist.append(InstructionConstants.DUP);
        ilist.append(InstructionConstants.DUP);
        String text = "Call to method " + methodGen.getName() +
                " took ";
        ilist.append(new PUSH(constantPool, text));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
                "print", Type.VOID, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
        ilist.append(ifact.createInvoke("java.lang.System",
                "currentTimeMillis", Type.LONG, Type.NO_ARGS,
                Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.
                createLoad(Type.LONG, stackIndex));
        ilist.append(InstructionConstants.LSUB);
        ilist.append(ifact.createInvoke("java.io.PrintStream",
                "print", Type.VOID, new Type[]{Type.LONG},
                Constants.INVOKEVIRTUAL));
        ilist.append(new PUSH(constantPool, " ms."));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
                "println", Type.VOID, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
        // return result from wrapped method call
        if (returnType != Type.VOID) {
            ilist.append(InstructionFactory.
                    createLoad(returnType, stackIndex + 2));
        }
        ilist.append(InstructionFactory.createReturn(returnType));

        // finalize the constructed method
        wrapGen.stripAttributes(true);
        wrapGen.setMaxStack();
        wrapGen.setMaxLocals();
        classGen.addMethod(wrapGen.getMethod());
        ilist.dispose();

    }

    public static void premain(String options, Instrumentation ins) {
        if (options != null) {
            ins.addTransformer(new Timing(options));
        } else {
            System.out.println("Usage: java -javaagent:Timing.jar=\"class:method\"");
            System.exit(0);
        }

    }

}
