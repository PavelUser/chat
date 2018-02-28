package com.company.test.b;

import com.company.test.a.ClassA;
import org.apache.log4j.Logger;

public class ClassB {
    private static final Logger log = Logger.getLogger(ClassB.class);
    public void testB(){
        log.info("testB");
    }
}
