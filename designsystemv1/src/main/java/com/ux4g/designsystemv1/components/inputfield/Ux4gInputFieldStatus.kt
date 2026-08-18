package com.ux4g.designsystemv1.components.inputfield

/** Defines the validation status variants for the Ux4gInputField component. */
enum class Ux4gInputFieldStatus {
    Default,
    Success,
    Warning,
    Error;

    companion object {
        val defaultStatus = Default
        val default = Default
        val success = Success
        val warning = Warning
        val error = Error
    }
}
