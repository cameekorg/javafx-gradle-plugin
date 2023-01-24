/*
 * Copyright (c) 2018, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openjfx.gradle;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class Utils {

    private static final Logger LOGGER = Logging.getLogger(Utils.class);

    public static <K,V> Map<K, V> mapOf(K k1, V v1) {
        return Collections.unmodifiableMap(
                new HashMap<>(
                        Collections.singletonMap(k1, v1)
                )
        );
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... args) {
        return Collections.unmodifiableList(
                Arrays.asList(args)
        );
    }


    @SuppressWarnings("all")
    public static <T> Stream<T> optionalStream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
    }

    public static Class<?> classRunModuleOptions() {
        final String name = "org.javamodularity.moduleplugin.extensions.RunModuleOptions";
        try {
            return Class.forName(name);
        } catch (Throwable t) {
            String msg = "Cannot retrieve Class object for class name: " + name + " " + t.getMessage();
            LOGGER.error(msg, t);
            throw new IllegalStateException(msg, t);
        }
    }

    @SuppressWarnings("all")
    public static void runModuleOptionsAddModulesAdd(Object runModuleOptions, Class<?> classRunModuleOptions, String module) {
        try {
            Method getAddModules = classRunModuleOptions.getSuperclass().getSuperclass().getDeclaredMethod("getAddModules");
            List<String> addModules = (List<String>) getAddModules.invoke(runModuleOptions);
            addModules.add(module);
        } catch (Throwable t) {
            String msg = "Cannot call runModuleOptions.getAddModules().add(module) for runModuleOptions: " + runModuleOptions
                    + " classRunModuleOptions: " + classRunModuleOptions
                    + " module: " + module
                    + " exception: " + t.getMessage();
            LOGGER.error(msg, t);
            throw new IllegalStateException(msg, t);
        }
    }
}
