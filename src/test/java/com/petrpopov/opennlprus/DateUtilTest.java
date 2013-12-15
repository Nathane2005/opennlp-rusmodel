package com.petrpopov.opennlprus;

import com.petrpopov.opennlprus.support.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by petrpopov on 15.12.13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class DateUtilTest {

    @Autowired
    private DateUtil dateUtil;

    @Test
    public void dateTest() {
        Date date = dateUtil.convertToDate("Sun, 15 Dec 2013 12:39:00 +0400");

        assertNotNull(date);
    }
}
