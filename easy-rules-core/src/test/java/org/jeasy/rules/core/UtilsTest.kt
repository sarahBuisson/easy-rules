/**
 * The MIT License
 *
 * Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jeasy.rules.core

import kotlin.test.Test


import kotlin.reflect.KClass
import kotlin.test.*

class UtilsTest {

    @Test
    fun findAnnotationWithClassWhereAnnotationIsPresent() {
        val foo: Annotation = Utils.findAnnotation(Foo::class, AnnotationIsPresent::class) as Annotation

        assertCorrectAnnotationIsFound(Foo::class, foo)
    }

    @Test
    fun findAnnotationWithClassWhereAnnotationIsPresentViaMetaAnnotation() {
        val foo: Annotation = Utils.findAnnotation(Foo::class, AnnotationIsPresentViaMetaAnnotation::class) as Annotation

        assertCorrectAnnotationIsFound(Foo::class, foo)
    }

    @Test
    fun findAnnotationWithClassWhereAnnotationIsNotPresent() {
        val foo = Utils.findAnnotation(Foo::class, Object::class)

        assertNull(foo)
    }

    @Test
    fun isAnnotationPresentWithClassWhereAnnotationIsPresent() {
        assertTrue(Utils.isAnnotationPresent(Foo::class, AnnotationIsPresent::class))
    }

    @Test
    fun isAnnotationPresentWithClassWhereAnnotationIsPresentViaMetaAnnotation() {
        assertTrue(Utils.isAnnotationPresent(Foo::class, AnnotationIsPresentViaMetaAnnotation::class))
    }

    @Test
    fun isAnnotationPresentWithClassWhereAnnotationIsNotPresent() {
        assertFalse(Utils.isAnnotationPresent(Foo::class, Object::class))
    }

    private fun assertCorrectAnnotationIsFound(
            expectedAnnotationType: KClass<*>, actualAnnotation: Annotation) {

        assertNotNull(actualAnnotation)
        assertEquals(expectedAnnotationType, actualAnnotation.annotationClass)
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    private annotation class Foo

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    @Foo
    private annotation class MetaFoo

    @Foo
    private class AnnotationIsPresent

    @MetaFoo
    private class AnnotationIsPresentViaMetaAnnotation
}
