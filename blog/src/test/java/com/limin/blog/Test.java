package com.limin.blog;

import org.springframework.test.context.TestExecutionListeners;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by devlimin on 2017/6/29.
 */
public class Test {
    @org.junit.Test
    public void testDate() {
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println(now.minusWeeks(1));
        Date from = Date.from(Instant.now().minusSeconds(3600 * 24 * 7));
        System.out.println(from);
    }
}
