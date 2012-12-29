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
package org.chjdev.simplemapper.example;

import org.chjdev.simplemapper.ITransformer;
import org.chjdev.simplemapper.annotations.Mapped;

@Mapped("toplevel")
public class ExampleMapped {

    public static class PlusOne implements ITransformer<Integer, Integer> {

        @Override
        public Integer transform(final Integer obj) {
            return obj + 1;
        }
    }

    public static class ToUpper implements ITransformer<String, String> {

        @Override
        public String transform(final String obj) {
            return obj.toUpperCase();
        }
    }

    public static class SpellOut implements ITransformer<Integer, String> {

        @Override
        public String transform(final Integer obj) {
            StringBuilder sb = new StringBuilder();
            for (int i = obj; i > 0; i--) {
                sb.append("a").append(i);
            }
            return sb.toString();
        }
    }
    @Mapped({"sublevel", "field"})
    private String testvalue;
    @Mapped(value = {"sublevel", "field2"}, transformers = PlusOne.class)
    public Integer testvalue2;
    @Mapped(value = {"sublevel", "field"}, transformers = ToUpper.class)
    public String testvalue3;
    @Mapped(value = {"sublevel", "field2"}, lenient = true, transformers = {PlusOne.class, SpellOut.class, ToUpper.class})
    public String testvalue4;
    @Mapped(value = {"non", "existant"}, nullable = true)
    public Integer testvalue5;

    public String getTestvalue() {
        return testvalue;
    }

    public void setTestvalue(String testvalue) {
        this.testvalue = testvalue;
    }

    @Override
    public String toString() {
        return String.format("toString() {testvalue=%s, testvalue2=%d, testvalue3=%s, testvalue4=%s, testvalue5=%s}", getTestvalue(), testvalue2, testvalue3, testvalue4, testvalue5);
    }
}
