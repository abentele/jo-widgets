/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public final class ArrayUtils {

    private ArrayUtils() {}

    public static int getMin(final int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Parameter 'array' must not be null or empty.");
        }
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int getMax(final int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Parameter 'array' must not be null or empty.");
        }
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static int[] toArray(final Collection<Integer> collection) {
        if (collection == null || collection.isEmpty()) {
            return new int[0];
        }
        final int[] result = new int[collection.size()];
        int index = 0;
        for (final Integer integer : collection) {
            result[index] = integer.intValue();
            index++;
        }
        return result;
    }

    public static <TYPE> Set<TYPE> toSet(final TYPE[] array) {
        if (array == null) {
            return Collections.emptySet();
        }
        final Set<TYPE> result = new HashSet<TYPE>();
        for (int i = 0; i < array.length; i++) {
            result.add(array[i]);
        }
        return result;
    }

    public static <TYPE> Iterator<TYPE> toIterator(final TYPE[] array) {
        Assert.paramNotNull(array, "array");
        return new Iterator<TYPE>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                if (index < array.length) {
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            public TYPE next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements available");
                }
                else {
                    return array[index++];
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Cannot remove elements from the array");
            }

        };

    }

}
