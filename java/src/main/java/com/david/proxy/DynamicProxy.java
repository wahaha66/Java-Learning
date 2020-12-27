package com.david.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {

    public static void main(String[] args) {
        Person person = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(),new Class[] {Person.class},new MyInvocationHandler());
        person.say("Hello World!");
    }

}

class MyInvocationHandler implements InvocationHandler{
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("say")){
            System.out.println("xiaomin say : " + args[0]);
        }
        return null;
    }
}

interface Person{
    String say(String content);
}
