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

package com.github.unlog.spi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Arguments {
    public static final Arguments NO_ARGS = new Arguments(new Object[0]);
    private final LinkedList<Object> args = new LinkedList<Object>();
    private Throwable throwable;

    public Arguments(Object[] args) {
        if (args != null) {
            this.args.addAll(Arrays.asList(args));
        }
        throwable = getThrowableFromArgsList();
        this.args.remove(throwable);
    }

    public static Arguments args(Object... args) {
        return new Arguments(args);
    }

    public List<Object> arguments() {
        return args;
    }

    public Throwable throwableArg() {
        return throwable;
    }

    private Throwable getThrowableFromArgsList() {
        return (Throwable) (hasThrowable() ? lastArg() : null);
    }

    private Object lastArg() {
        return args.getLast();
    }

    private boolean hasThrowable() {
        return hasArgs() && lastArg() instanceof Throwable;
    }

    private boolean hasArgs() {
        return !args.isEmpty();
    }

    int size() {
        return arguments().size();
    }

    public Object[] argsAsArray() {
        return arguments().toArray();
    }

    public void foreach(Iterator iterator) {
        for (Object arg : args) {
            iterator.element(arg);
        }
    }

    public boolean isEmpty() {
        return arguments().isEmpty();
    }

    public interface Iterator {
        Iterator element(Object el);
    }
}
