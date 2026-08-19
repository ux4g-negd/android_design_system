package com.ux4g.ux4gdesignsystem

import androidx.compose.ui.unit.dp
import com.ux4g.designsystemv1.components.progress.Ux4gHalfCircleProgressSize
import org.junit.Assert.assertEquals
import org.junit.Test

class Ux4gHalfCircleProgressTest {

    @Test
    fun testHalfCircleProgressSizeWidths() {
        assertEquals(80.dp, Ux4gHalfCircleProgressSize.S.width)
        assertEquals(140.dp, Ux4gHalfCircleProgressSize.M.width)
        assertEquals(200.dp, Ux4gHalfCircleProgressSize.L.width)
        assertEquals(280.dp, Ux4gHalfCircleProgressSize.XL.width)
    }

    @Test
    fun testHalfCircleProgressSizeAliases() {
        assertEquals(Ux4gHalfCircleProgressSize.S, Ux4gHalfCircleProgressSize.s)
        assertEquals(Ux4gHalfCircleProgressSize.M, Ux4gHalfCircleProgressSize.m)
        assertEquals(Ux4gHalfCircleProgressSize.L, Ux4gHalfCircleProgressSize.l)
        assertEquals(Ux4gHalfCircleProgressSize.XL, Ux4gHalfCircleProgressSize.xl)
    }
}
