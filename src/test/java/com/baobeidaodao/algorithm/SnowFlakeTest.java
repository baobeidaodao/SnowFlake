package com.baobeidaodao.algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class SnowFlakeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test() {
        long dataCenterId = 2L;
        long workerId = 3L;
        SnowFlake snowFlake = new SnowFlake(dataCenterId, workerId);
        for (int i = 0; i <= (1L << 10L); i++) {
            System.out.println(snowFlake.nextId());
        }
    }

    @Test
    public void test1() {
        Long id = 24487868877778974L;
        Map<String, Object> map = SnowFlakeUtil.decrypt(id);
    }

}