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
package org.chjdev.simplemapper.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.chjdev.simplemapper.IAccessor;

public class SimpleMapAccessor implements IAccessor<Map<String, ?>> {

    private static final Logger _logger = Logger.getLogger(SimpleMapAccessor.class.getName());
    private static SimpleMapAccessor _instance;

    public static SimpleMapAccessor getInstance() {
        if (_instance == null) {
            _instance = new SimpleMapAccessor();
        }
        return _instance;
    }

    @Override
    public <S> S access(final Map<String, ?> source, final Class<S> c, final String... path) {
        Map<String, ?> traverse = source;

        int last = path.length - 1;
        for (int i = 0; i < last; i++) {
            try {
                traverse = (Map<String, ?>) traverse.get(path[i]);
                if (traverse == null) {
                    throw new NullPointerException();
                }
            } catch (NullPointerException | ClassCastException ex) {
                _logger.log(Level.FINE, "Could not traverse input map, invalid path component: {0}, Exception: {1}", new Object[]{path[i], ex});
                return null;
            }
        }

        Object obj = traverse.get(path[last]);
        if (obj == null) {
            return null;
        }
        try {
            return c.cast(obj);
        } catch (ClassCastException ex) {
            _logger.log(Level.FINE, "Incompatible target type: {0} found: {1}, Exception: {2}", new Object[]{c.getName(), obj.getClass().getName(), ex});
            return null;
        }
    }
}
