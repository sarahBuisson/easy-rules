package org.jeasy.rules.core

import kotlin.jvm.java

class UtilsTest {
    @org.junit.Test
    fun findAnnotationWithClassWhereAnnotationIsPresent() {
        val foo: Annotation? = Utils.findAnnotation(Foo::class, AnnotationIsPresent::class.java)
        assertCorrectAnnotationIsFound(Foo::class.java, foo)
    }

    @org.junit.Test
    fun findAnnotationWithClassWhereAnnotationIsPresentViaMetaAnnotation() {
        val foo: Annotation? = Utils.findAnnotation(Foo::class, AnnotationIsPresentViaMetaAnnotation::class.java)
        assertCorrectAnnotationIsFound(Foo::class.java, foo)
    }

    @org.junit.Test
    fun findAnnotationWithClassWhereAnnotationIsNotPresent() {
        val foo: Annotation? = Utils.findAnnotation(Foo::class, Any::class.java)
        org.junit.Assert.assertNull(foo)
    }

    @org.junit.Test
    fun isAnnotationPresentWithClassWhereAnnotationIsPresent() {
        org.junit.Assert.assertTrue(Utils.isAnnotationPresent(Foo::class, AnnotationIsPresent::class.java))
    }

    @org.junit.Test
    fun isAnnotationPresentWithClassWhereAnnotationIsPresentViaMetaAnnotation() {
        org.junit.Assert.assertTrue(
            Utils.isAnnotationPresent(
                Foo::class,
                AnnotationIsPresentViaMetaAnnotation::class.java
            )
        )
    }

    @org.junit.Test
    fun isAnnotationPresentWithClassWhereAnnotationIsNotPresent() {
        org.junit.Assert.assertFalse(Utils.isAnnotationPresent(Foo::class.java, Any::class.java))
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    private inner annotation class Foo

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    @Foo
    private inner annotation class MetaFoo

    @Foo
    private class AnnotationIsPresent

    @MetaFoo
    private class AnnotationIsPresentViaMetaAnnotation
    companion object {
        private fun assertCorrectAnnotationIsFound(
            expectedAnnotationType: Class<*>?, actualAnnotation: Annotation?
        ) {
            org.junit.Assert.assertNotNull(actualAnnotation)
            org.junit.Assert.assertEquals(expectedAnnotationType, actualAnnotation.annotationType())
        }
    }
}
