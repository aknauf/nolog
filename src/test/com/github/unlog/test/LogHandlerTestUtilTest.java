/*
 * Copyright 2013 Aaron Knauf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.unlog.test;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandlerTestUtilTest {

    @Rule
    public JUnitRuleMockery mockery = new JUnitRuleMockery();

    @Test
    public void shouldWriteJavaUtilLogOutputToLogDestination() {
        final LoggerFixture loggerFixture = LoggerFixture.createLoggerFixture(mockery, "shouldWriteJavaUtilLogOutputToLogDestination");

        loggerFixture.expectLogStatement(Level.SEVERE, "Some log message", (Object[]) null);

        Logger logger = Logger.getLogger("shouldWriteJavaUtilLogOutputToLogDestination");
        logger.severe("Some log message");
    }

}
