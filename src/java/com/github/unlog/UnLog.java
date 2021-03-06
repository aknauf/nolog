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

package com.github.unlog;

import com.github.unlog.internal.format.LogFormatFactory;
import com.github.unlog.jul.JavaUtilLogWriter;
import com.github.unlog.spi.Arguments;
import com.github.unlog.spi.LogEvent;
import com.github.unlog.spi.LogWriter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UnLog {

    private static final LogWriter LOG_WRITER = new JavaUtilLogWriter();
    private static final LogFormatFactory logFormatFactory = new LogFormatFactory();

    public static <L> L createLogger(Class<L> loggerInterface) {
        return createLogger(loggerInterface, LogMessage.EMPTY);
    }

    private static <L> L createLogger(Class<L> loggerInterface, LogMessage context) {
        //noinspection unchecked
        return (L) Proxy.newProxyInstance(UnLog.class.getClassLoader(), new Class[]{loggerInterface},
                new LogInvocationHandler(LOG_WRITER, logFormatFactory, context));
    }

    private static LogCategory categoryName(Method method) {
        return new LogCategory(method.getDeclaringClass().getCanonicalName());
    }

    private static LogLevel determineLogLevel(Method method) {
        LogLevel logLevel;
        if (method.isAnnotationPresent(Log.class)) {
            logLevel = method.getAnnotation(Log.class).level();
        } else {
            logLevel = useDefaultLogLevel();
        }
        return logLevel;
    }

    private static LogLevel useDefaultLogLevel() {
        return LogLevel.DEBUG;
    }

    private static class LogInvocationHandler implements InvocationHandler {
        private final LogWriter logWriter;
        private final LogFormatFactory logFormatFactory;
        private LogMessage context;

        public LogInvocationHandler(LogWriter logWriter, LogFormatFactory logFormatFactory, LogMessage context) {
            this.logWriter = logWriter;
            this.logFormatFactory = logFormatFactory;
            this.context = context;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Arguments arguments = new Arguments(args);
            if (!Void.TYPE.equals(method.getReturnType())) {
                return createLogger(method.getReturnType(), context.extendWith(logFormatFactory.logFormat(method), arguments));
            } else {
                logWriter.writeLogEvent(LogEvent.createLogEvent(categoryName(method), determineLogLevel(method), context.extendWith(logFormatFactory.logFormat(method), arguments)));
            }

            return null;
        }

    }
}
