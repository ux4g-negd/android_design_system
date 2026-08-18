package com.ux4g.designsystemv1.components.inputfield

/** Defines the type of content expected in the Ux4gInputField component. */
enum class Ux4gInputFieldType {
    Text,
    Password,
    Number,
    Email;

    companion object {
        val text = Text
        val password = Password
        val number = Number
        val email = Email
    }
}
