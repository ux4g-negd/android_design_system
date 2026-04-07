package com.ux4g.designsystemv1.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// 1. Primitive Colors (The Palette)
// Exposed so users can use specific shades if needed, maintaining the system structure.
object Ux4gPalette {

        val gray100 = Color(0xFFF5F5F5)
        val gray200 = Color(0xFFEEEEEE)
        val gray800 = Color(0xFF333333)
        val gray900 = Color(0xFF121212)

        // primary color
        val primary = Color(0xFF6A4EFF)
        val primary50 = Color(0xFFF2EFFF)
        val primary100 = Color(0xFFDCD4FF)
        val primary200 = Color(0xFFC0B3FF)
        val primary300 = Color(0xFFA391FF)
        val primary400 = Color(0xFF8670FF)
        val primary500 = Color(0xFF6A4EFF)
        val primary600 = Color(0xFF4A2BC2)
        val primary700 = Color(0xFF3D239F)
        val primary800 = Color(0xFF301C7D)
        val primary900 = Color(0xFF24145C)
        val primary950 = Color(0xFF1A0E3D)

        val primary50A = Color(0x40F2EFFF)
        val primary100A = Color(0x40DCD4FF)
        val primary200A = Color(0x40C0B3FF)
        val primary300A = Color(0x40A391FF)
        val primary400A = Color(0x408670FF)
        val primary500A = Color(0x406A4EFF)
        val primary600A = Color(0x404A2BC2)
        val primary700A = Color(0x403D239F)
        val primary800A = Color(0x40301C7D)
        val primary900A = Color(0x4024145C)
        val primary950A = Color(0x401A0E3D)

        // secondary color
        val secondary = Color(0xFFFFA827)
        val secondary50 = Color(0xFFFFF7E6)
        val secondary100 = Color(0xFFFFE7BF)
        val secondary200 = Color(0xFFFFD899)
        val secondary300 = Color(0xFFFFC973)
        val secondary400 = Color(0xFFFFBA4D)
        val secondary500 = Color(0xFFFFA827)
        val secondary600 = Color(0xFFFF8F1F)
        val secondary700 = Color(0xFFD46F13)
        val secondary800 = Color(0xFFAD530A)
        val secondary900 = Color(0xFF873C05)
        val secondary950 = Color(0xFF612800)

        val secondary50A = Color(0x40FFF7E6)
        val secondary100A = Color(0x40FFE7BF)
        val secondary200A = Color(0x40FFD899)
        val secondary300A = Color(0x40FFC973)
        val secondary400A = Color(0x40FFBA4D)
        val secondary500A = Color(0x40FFA827)
        val secondary600A = Color(0x40FF8F1F)
        val secondary700A = Color(0x40D46F13)
        val secondary800A = Color(0x40AD530A)
        val secondary900A = Color(0x40873C05)
        val secondary950A = Color(0x40612800)

        // Tertiary color
        val tertiary = Color(0xFFA66ACC)
        val tertiary50 = Color(0xFFF6EFFB)
        val tertiary100 = Color(0xFFE9DAF3)
        val tertiary200 = Color(0xFFD9BFEA)
        val tertiary300 = Color(0xFFC8A3E0)
        val tertiary400 = Color(0xFFB686D6)
        val tertiary500 = Color(0xFFA66ACC)
        val tertiary600 = Color(0xFF8E55B3)
        val tertiary700 = Color(0xFF75419A)
        val tertiary800 = Color(0xFF5D2F80)
        val tertiary900 = Color(0xFF462166)
        val tertiary950 = Color(0xFF32174A)

        val tertiary50A = Color(0x40F6EFFB)
        val tertiary100A = Color(0x40E9DAF3)
        val tertiary200A = Color(0x40D9BFEA)
        val tertiary300A = Color(0x40C8A3E0)
        val tertiary400A = Color(0x40B686D6)
        val tertiary500A = Color(0x40A66ACC)
        val tertiary600A = Color(0x408E55B3)
        val tertiary700A = Color(0x4075419A)
        val tertiary800A = Color(0x405D2F80)
        val tertiary900A = Color(0x40462166)
        val tertiary950A = Color(0x4032174A)

        // red color
        val red = Color(0xFFF55E57)
        val red50 = Color(0xFFFFF8F8)
        val red100 = Color(0xFFFFECEE)
        val red200 = Color(0xFFFFDADC)
        val red300 = Color(0xFFFFB3AE)
        val red400 = Color(0xFFFF8983)
        val red500 = Color(0xFFF55E57)
        val red600 = Color(0xFFDB372D)
        val red700 = Color(0xFFB3251E)
        val red800 = Color(0xFF8A1A16)
        val red900 = Color(0xFF60150F)
        val red950 = Color(0xFF3A0907)

        // blue color
        val blue = Color(0xFF4E8FF8)
        val blue50 = Color(0xFFF5FAFF)
        val blue100 = Color(0xFFE7F2FF)
        val blue200 = Color(0xFFD0E4FF)
        val blue300 = Color(0xFFA1C9FF)
        val blue400 = Color(0xFF76ACFF)
        val blue500 = Color(0xFF4E8FF8)
        val blue600 = Color(0xFF3271EA)
        val blue700 = Color(0xFF1157CE)
        val blue800 = Color(0xFF04409F)
        val blue900 = Color(0xFF012C6F)
        val blue1100 = Color(0xFF001944)

        // skyblue color
        val skyblue = Color(0xFF009DC9)
        val skyblue50 = Color(0xFFF4FAFF)
        val skyblue100 = Color(0xFFE0F4FF)
        val skyblue200 = Color(0xFFBDE9FF)
        val skyblue300 = Color(0xFF67D4FF)
        val skyblue400 = Color(0xFF00BBEA)
        val skyblue500 = Color(0xFF009DC9)
        val skyblue600 = Color(0xFF0081A8)
        val skyblue700 = Color(0xFF006788)
        val skyblue800 = Color(0xFF004D68)
        val skyblue900 = Color(0xFF003549)
        val skyblue950 = Color(0xFF001F2D)

        // cyan color
        val cyan = Color(0xFF59D8CE)
        val cyan50 = Color(0xFFE6FFFB)
        val cyan100 = Color(0xFFC9F7F2)
        val cyan200 = Color(0xFFADF0E9)
        val cyan300 = Color(0xFF91E8E0)
        val cyan400 = Color(0xFF75E0D7)
        val cyan500 = Color(0xFF59D8CE)
        val cyan600 = Color(0xFF13C2C2)
        val cyan700 = Color(0xFF08979C)
        val cyan800 = Color(0xFF006D75)
        val cyan900 = Color(0xFF00474F)
        val cyan950 = Color(0xFF002329)

        // green color
        val green = Color(0xFF1AA64A)
        val green50 = Color(0xFFF2FCEF)
        val green100 = Color(0xFFDDF8D8)
        val green200 = Color(0xFFBEEFBB)
        val green300 = Color(0xFF80DA88)
        val green400 = Color(0xFF44C265)
        val green500 = Color(0xFF1AA64A)
        val green600 = Color(0xFF128937)
        val green700 = Color(0xFF006C35)
        val green800 = Color(0xFF00522C)
        val green900 = Color(0xFF00381F)
        val green950 = Color(0xFF002110)

        // lime color
        val lime = Color(0xFFCAE827)
        val lime50 = Color(0xFFFCFFE6)
        val lime100 = Color(0xFFF2FFBF)
        val lime200 = Color(0xFFE8FF99)
        val lime300 = Color(0xFFDEFF73)
        val lime400 = Color(0xFFD4F24D)
        val lime500 = Color(0xFFCAE827)
        val lime600 = Color(0xFFA0D911)
        val lime700 = Color(0xFF7CB305)
        val lime800 = Color(0xFF5B8C00)
        val lime900 = Color(0xFF3F6600)
        val lime950 = Color(0xFF254000)

        // yellow color
        val yellow = Color(0xFFFFD72A)
        val yellow50 = Color(0xFFFEFFE6)
        val yellow100 = Color(0xFFFFFBC2)
        val yellow200 = Color(0xFFFFF29C)
        val yellow300 = Color(0xFFFFE976)
        val yellow400 = Color(0xFFFFE050)
        val yellow500 = Color(0xFFFFD72A)
        val yellow600 = Color(0xFFFADB14)
        val yellow700 = Color(0xFFD4B106)
        val yellow800 = Color(0xFFAD8B00)
        val yellow900 = Color(0xFF876800)
        val yellow950 = Color(0xFF614700)

        // gold color
        val gold = Color(0xFFFFC327)
        val gold50 = Color(0xFFFFFBE6)
        val gold100 = Color(0xFFFFF2BF)
        val gold200 = Color(0xFFFFE799)
        val gold300 = Color(0xFFFFDB73)
        val gold400 = Color(0xFFFFCF4D)
        val gold500 = Color(0xFFFFC327)
        val gold600 = Color(0xFFF2A90F)
        val gold700 = Color(0xFFD98A00)
        val gold800 = Color(0xFFB36B00)
        val gold900 = Color(0xFF8C4D00)
        val gold950 = Color(0xFF613400)

        // orange color
        val orange = Color(0xFFFFAB27)
        val orange50 = Color(0xFFFFF7E6)
        val orange100 = Color(0xFFFFE7BF)
        val orange200 = Color(0xFFFFD899)
        val orange300 = Color(0xFFFFC973)
        val orange400 = Color(0xFFFFBA4D)
        val orange500 = Color(0xFFFFAB27)
        val orange600 = Color(0xFFFA8C16)
        val orange700 = Color(0xFFD46B08)
        val orange800 = Color(0xFFAD4E00)
        val orange900 = Color(0xFF873800)
        val orange950 = Color(0xFF612500)

        // purple color
        val purple = Color(0xFFAD72FF)
        val purple50 = Color(0xFFFDF8FF)
        val purple100 = Color(0xFFF7ECFE)
        val purple200 = Color(0xFFEEDCFE)
        val purple300 = Color(0xFFD9BAFD)
        val purple400 = Color(0xFFC597FF)
        val purple500 = Color(0xFFAD72FF)
        val purple600 = Color(0xFF9254EA)
        val purple700 = Color(0xFF7438D2)
        val purple800 = Color(0xFF5629A4)
        val purple900 = Color(0xFF400B84)
        val purple950 = Color(0xFF280255)

        // pink color
        val pink = Color(0xFFF94AAB)
        val pink50 = Color(0xFFFFF7FC)
        val pink100 = Color(0xFFFFECF6)
        val pink200 = Color(0xFFFFD8EF)
        val pink300 = Color(0xFFFFAEE4)
        val pink400 = Color(0xFFFF7DD2)
        val pink500 = Color(0xFFF94AAB)
        val pink600 = Color(0xFFDC258D)
        val pink700 = Color(0xFFB60D6E)
        val pink800 = Color(0xFF8D0053)
        val pink900 = Color(0xFF620438)
        val pink950 = Color(0xFF3D0023)

        // neutral color
        val neutral = Color(0xFF737373)
        val neutral50 = Color(0xFFFAFAFA)
        val neutral100 = Color(0xFFF5F5F5)
        val neutral200 = Color(0xFFE5E5E5)
        val neutral300 = Color(0xFFD9D9D9)
        val neutral400 = Color(0xFFA1A1A1)
        val neutral500 = Color(0xFF737373)
        val neutral600 = Color(0xFF525252)
        val neutral700 = Color(0xFF404040)
        val neutral800 = Color(0xFF262626)
        val neutral900 = Color(0xFF171717)
        val neutral950 = Color(0xFF0A0A0A)
        val neutral1000black = Color(0xFF000000)
        val white = Color(0xFFFFFFFF)
        val transparent = Color(0x00000000)

        val neutral50A = Color(0x40FAFAFA)
        val neutral100A = Color(0x40F5F5F5)
        val neutral200A = Color(0x40E5E5E5)
        val neutral300A = Color(0x40D9D9D9)
        val neutral400A = Color(0x40A1A1A1)
        val neutral500A = Color(0x40737373)
        val neutral600A = Color(0x40525252)
        val neutral700A = Color(0x40404040)
        val neutral800A = Color(0xFF262626)
        val neutral900A = Color(0x40171717)
        val neutral950A = Color(0x400A0A0A)
        val neutral950B = Color(0xB30A0A0A)
        val neutral1000A = Color(0x40000000)
        val whiteA = Color(0x40FFFFFF)
        val whiteB = Color(0xB3FFFFFF)
}

// 2. Semantic Colors (The System)
// Components use these.
@Immutable
data class Ux4gColors(
        val primary: Color,
        val onPrimary: Color,
        val secondary: Color,
        val onSecondary: Color,
        val background: Color,
        val onBackground: Color,
        val surface: Color,
        val onSurface: Color,
        val error: Color,
        val onError: Color
)

// light theme colors
val lightColors =
        Ux4gColors(
                primary = Ux4gPalette.primary,
                onPrimary = Ux4gPalette.white,
                secondary = Ux4gPalette.secondary,
                onSecondary = Ux4gPalette.gray900,
                background = Ux4gPalette.gray100,
                onBackground = Ux4gPalette.gray900,
                surface = Ux4gPalette.white,
                onSurface = Ux4gPalette.gray900,
                error = Ux4gPalette.red,
                onError = Ux4gPalette.white
        )

// dark theme colors
val darkColors =
        Ux4gColors(
                primary = Ux4gPalette.primary,
                onPrimary = Ux4gPalette.gray900,
                secondary = Ux4gPalette.blue700,
                onSecondary = Ux4gPalette.white,
                background = Ux4gPalette.gray900,
                onBackground = Ux4gPalette.white,
                surface = Ux4gPalette.gray800,
                onSurface = Ux4gPalette.white,
                error = Ux4gPalette.red400,
                onError = Ux4gPalette.gray900
        )

// 3. Accessibility
val LocalUx4gColors = staticCompositionLocalOf { lightColors }
