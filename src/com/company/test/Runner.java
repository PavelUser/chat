package com.company.test;

import com.company.test.a.ClassA;
import com.company.test.b.ClassB;
import com.company.test.c.ClassC;

public class Runner {
    public static void main(String[] args) {
        ClassA classA = new ClassA();
        ClassB classB = new ClassB();
        ClassC classC = new ClassC();
        classA.testA();
        classB.testB();
        classC.testC();
    }
}
