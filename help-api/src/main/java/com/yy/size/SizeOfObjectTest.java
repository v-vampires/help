package com.yy.size;

import java.io.File;

import static com.yy.size.SizeOfObject.fullSizeOf;
import static com.yy.size.SizeOfObject.sizeOf;

/**
 * 命令行下执行：
 * java -javaagent:target/help-api.jar com.yy.size.SizeOfObjectTest
 * @author tianmai.fh 
 * @date 2014-03-18 20:17 
 */  
public class SizeOfObjectTest {  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 = 16 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 4 + padding/4 = 24 
     */  
    static class A {  
        int a;  
    }  
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 + 4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 4 + 4 = 24 
     */  
    static class B {  
        int a;  
        int b;  
    }  
  
    /** 
     * -XX:+UseCompressedOops: mark/4 + metedata/8 + 4 + 4 + padding/4 = 24 
     * -XX:-UseCompressedOops: mark/8 + metedata/8 + 8 + 4 + padding/4 = 32 
     */  
    static class B2 {  
        int b2a;  
        Integer b2b;  
    }  
  
    /** 
     * 不考虑对象头： 
     * 4 + 4 + 4 * 3 + 3 * sizeOf(B) 
     */  
    static class C extends A {  
        int ba;  
        B[] as = new B[3];  
  
        C() {  
            for (int i = 0; i < as.length; i++) {  
                as[i] = new B();  
            }  
        }  
    }  
  
    static class D extends B {  
        int da;  
        Integer[] di = new Integer[3];  
    }  
  
    /** 
     * 会算上A的实例字段 
     */  
    static class E extends A {  
        int ea;  
        int eb;  
    }

    static class Person1{
        private String name;
        private String age;
        public void setName(String name) {
            this.name = name;
        }
        public void setAge(String age) {
            this.age = age;
        }
    }

    static class Person2{
        private int name;
        private int age;

        public void setName(int name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) throws IllegalAccessException {  
        System.out.println(new File("./target/classes").getAbsolutePath());
        System.out.println("sizeOf(new Object())=" + sizeOf(new Object()));  
        System.out.println("sizeOf(new A())=" + sizeOf(new A()));  
        System.out.println("sizeOf(new B())=" + sizeOf(new B()));  
        System.out.println("sizeOf(new B2())=" + sizeOf(new B2()));  
        System.out.println("sizeOf(new B[3])=" + sizeOf(new B[3]));  
        System.out.println("sizeOf(new C())=" + sizeOf(new C()));  
        System.out.println("fullSizeOf(new C())=" + fullSizeOf(new C()));  
        System.out.println("sizeOf(new D())=" + sizeOf(new D()));  
        System.out.println("fullSizeOf(new D())=" + fullSizeOf(new D()));  
        System.out.println("sizeOf(new int[3])=" + sizeOf(new int[3]));  
        System.out.println("sizeOf(new Integer(1)=" + sizeOf(new Integer(1)));  
        System.out.println("sizeOf(new Integer[0])=" + sizeOf(new Integer[0]));  
        System.out.println("sizeOf(new Integer[1])=" + sizeOf(new Integer[1]));  
        System.out.println("sizeOf(new Integer[2])=" + sizeOf(new Integer[2]));  
        System.out.println("sizeOf(new Integer[3])=" + sizeOf(new Integer[3]));  
        System.out.println("sizeOf(new Integer[4])=" + sizeOf(new Integer[4]));  
        System.out.println("sizeOf(new A[3])=" + sizeOf(new A[3]));  
        System.out.println("sizeOf(new E())=" + sizeOf(new E()));
        Person1 person1 = new Person1();
        person1.setName("ABC");
        //person1.setAge("20");
        Person2 person2 = new Person2();
        person2.setName(100);
        person2.setAge(20);
        System.out.println("fullSizeOf(person1)=" + fullSizeOf(person1));
        System.out.println("fullSizeOf(person2)=" + fullSizeOf(person2));
        System.out.println("fullSizeOf(new String())=" + fullSizeOf(new String("12")));
    }  
}  