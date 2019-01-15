package org.jeasy.rules.utils

import java.lang.annotation.Inherited

@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class Mock{}



class KotlinMockRunner