package com.revature.jank_unit;

import com.revature.jank_unit.util.Describe;
import com.revature.jank_unit.util.Test;

@Describe
public class AnotherFakeTestSuite {

    @Test(testDescription = "none given")
    public void test_this() {
        System.out.println("test_this invoked!");
    }

}
