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
package org.chjdev.simplemapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.chjdev.simplemapper.annotations.Mapped;
import org.chjdev.simplemapper.impl.EmptyTransformer;
import org.chjdev.simplemapper.util.Setter;

public abstract class Mapper<T> {

    private final IAccessor<T> _accessor;

    protected Mapper(IAccessor<T> accessor) {
        _accessor = accessor;
    }

    private static Object transform(final Object value, final Class<? extends ITransformer>... transformers) throws InstantiationException, IllegalAccessException {
        Object ret = value;
        for (Class<? extends ITransformer> tc : transformers) {
            if (tc.equals(EmptyTransformer.class)) {
                continue;
            }
            ITransformer t = tc.newInstance();
            ret = t.transform(ret);
        }
        return ret;
    }

    public <S> S map(final T input, final Class<? extends S> c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        S ret = c.newInstance();
        if (c.isAnnotationPresent(Mapped.class)) {
            Mapped m = c.getAnnotation(Mapped.class);
            String[] rootPath = m.value();
            T root = _accessor.access(input, (Class<T>) input.getClass(), rootPath);
            if (root == null) {
                if (m.nullable()) {
                    return ret;
                } else {
                    throw new NullPointerException(String.format("{%s} NULL found for top level at path %s", c.getName(), Arrays.toString(rootPath)));
                }
            }

            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Mapped.class)) {
                    Mapped fm = field.getAnnotation(Mapped.class);
                    String[] subPath = fm.value();
                    Class<?> targetClass;
                    if (fm.lenient() || m.lenient()) {
                        targetClass = Object.class;
                    } else {
                        targetClass = field.getType();
                    }
                    Object value = _accessor.access(root, targetClass, subPath);
                    if (value == null && !fm.nullable() && !m.nullable()) {
                        throw new NullPointerException(String.format("{%s} NULL found for %s at path %s", c.getName(), field.getName(), Arrays.toString(subPath)));
                    }
                    if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                        field.set(ret, transform(value, fm.transformers()));
                    } else {
                        Method set = Setter.getSetter(c, field);
                        if (Setter.validateSetter(set)) {
                            set.invoke(ret, transform(value, fm.transformers()));
                        } else {
                            throw new IllegalArgumentException(String.format("{%s} Provided class is not mappable. Setter %s is not valid!", c.getCanonicalName(), set.toGenericString()));
                        }
                    }
                }
            }
            return ret;
        } else {
            throw new IllegalArgumentException(String.format("{%s} Provided class is not mappable", c.getCanonicalName()));
        }
    }
}
