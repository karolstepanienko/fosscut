package com.fosscut;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class FossCutTest {
    @Test public void appHasAGreeting() {
        FossCut classUnderTest = new FossCut();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
