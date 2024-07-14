package com.example.test02;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Test02ApplicationTests {

    @Test
    void contextLoads() {
        int num = 0;
        for (int i = 0; i < 100; i++) {
            num+=i;
        }
        System.out.println(num);
    }

}
