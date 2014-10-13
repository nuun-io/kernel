/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.kernel.api.plugin;

import io.nuun.kernel.api.plugin.request.annotations.InjectedPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Collection;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class LinkTest
{

    static @interface RequestDef
    {
    }

    static interface Payload
    {
        Object payload();
    }

    static Object specProvider()
    {
        return "I'm the secret message 2";
    }

    @RequestDef
    @Retention(RetentionPolicy.RUNTIME)
     @interface Request
    {
        MyEnum value();
    }

    public enum MyEnum implements Payload
    {

        one(//
                "I'm the secret message 1"),

        two( //
                specProvider() 
//                one.payload() + "2"

        );

        private Object payload;

        MyEnum(Object payload)
        {
            this.payload = payload;
        }

        @Override
        public Object payload()
        {
            return payload;
        }
    }


    static class MyPlugin
    {
        @Request(MyEnum.one)
        Collection<Class<?>> blahStuff;
    }

    @Test
    public void testLink() throws NoSuchFieldException, SecurityException
    {
        InjectedPlugin ip = new InjectedPlugin();
        
        ip.test();
        
//        MyPlugin h = new MyPlugin();
//        Field declaredField = h.getClass().getDeclaredField("blahStuff");
//        Request annotation = declaredField.getAnnotation(Request.class);
//        MyEnum value = annotation.value();
//        Object o = value.payload();
//        
//        
//        Assertions.assertThat(o).isEqualTo("I'm the secret message 1");

    }

}
