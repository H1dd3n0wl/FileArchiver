package com.company;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessDataTest {

    @Test
    public void testEval() {
        String[] tests = ("""
                534+2453
                153-56
                435845*4635
                2845/452
                55+56+555
                45^4
                -3-3
                -4.5-2.3
                4.5-2
                5-(6-7)*3""").split("\n");
        double[] expected = {2987, 97, 2020141575, 6.29424778761062, 666, 4100625, -6, -6.8, 2.5, 8};

        for (int i = 0; i < tests.length; ++i) {
            Assertions.assertEquals(expected[i], ProcessData.eval(tests[i]));
        }
    }

}