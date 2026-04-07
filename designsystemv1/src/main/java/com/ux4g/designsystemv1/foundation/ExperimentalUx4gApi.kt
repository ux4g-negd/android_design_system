package com.ux4g.designsystemv1.foundation

@RequiresOptIn(
        level = RequiresOptIn.Level.WARNING,
        message = "This is an experimental API for the Ux4G Design System and is likely to change."
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class ExperimentalUx4gApi
