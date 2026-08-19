package com.ux4g.designsystemv1.components.textarea

enum class Ux4gTextAreaSize {
    Small,
    Large;

    companion object {
        @Deprecated("Use Small instead", ReplaceWith("Small"))
        val Medium = Small
    }
}
