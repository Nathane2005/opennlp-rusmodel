package com.petrpopov.opennlprus.main;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 0:10
 *
 * This is a fucking dirty trick to get access to Spring Context from legacy code
 * Done for the fucking stupid hardcoded crawler4j
 * http://www.objectpartners.com/2010/08/23/gaining-access-to-the-spring-context-in-non-spring-managed-classes/
 */

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
