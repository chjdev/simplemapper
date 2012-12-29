/**
 * Copyright 2012 Christian Junker <chris_git@gmx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chjdev.simplemapper.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Setter {

    public static boolean validateSetter(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        return true;
    }

    public static Method getSetter(final Class<?> c, final Field field) throws NoSuchMethodException {
        final String name = field.getName();
        final String methodName = String.format("set%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
        Method method = c.getMethod(methodName, field.getType());
        return method;
    }
}
