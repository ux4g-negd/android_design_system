package com.ux4g.designsystemv1.components

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ux4g.designsystemv1.components.buttons.Ux4gButton
import com.ux4g.designsystemv1.foundation.Ux4gColors
import com.ux4g.designsystemv1.foundation.Ux4gSpace
import com.ux4g.designsystemv1.foundation.defaultTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A robust image/widget carousel with optional auto-play and pagination,
 * matching Flutter's Ux4gCarousel component functionality, default colors, and typography.
 *
 * @param items The list of composable slide items to render.
 * @param modifier Modifier for the carousel root container.
 * @param autoPlay Whether the carousel should automatically cycle through items.
 * @param autoPlayIntervalMs Time in milliseconds between automatic slide transitions (default 3000ms).
 * @param showPagination Whether to display the dot pagination indicator at the bottom.
 * @param showPaginationArrows Whether to show left/right arrow buttons alongside pagination dots (default true).
 * @param height Height of the carousel container (default 240.dp).
 * @param viewportFraction Fraction of the viewport each page should occupy (default 1.0f).
 * @param contentPadding Padding applied around pages.
 * @param pageSpacing Spacing between consecutive pages.
 * @param activePaginationColor Color for the active elongated dot indicator (defaults to Ux4gColors.primary).
 * @param inactivePaginationColor Color for inactive dot indicators (defaults to Ux4gColors.primary100).
 * @param paginationArrowBackgroundColor Background color for arrow buttons (defaults to Ux4gColors.primary100).
 * @param paginationArrowColor Icon tint color for arrow buttons (defaults to Ux4gColors.primary).
 * @param paginationVariant Visual style of pagination indicator (Default or Capsule).
 * @param paginationSize Preset dot & arrow sizing (Small or Medium).
 * @param onPageChange Callback triggered whenever the current page changes.
 */
@Composable
fun Ux4gCarousel(
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    autoPlayIntervalMs: Long = 3000L,
    showPagination: Boolean = true,
    showPaginationArrows: Boolean = true,
    height: Dp = 240.dp,
    viewportFraction: Float = 1.0f,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSpacing: Dp = 0.dp,
    activePaginationColor: Color = Ux4gColors.primary,
    inactivePaginationColor: Color = Ux4gColors.primary100,
    paginationArrowBackgroundColor: Color = Color(0xFFDCD6FD),
    paginationArrowColor: Color = Ux4gColors.primary,
    paginationVariant: Ux4gPaginationVariant = Ux4gPaginationVariant.Default,
    paginationSize: Ux4gPaginationSize = Ux4gPaginationSize.Small,
    onPageChange: ((Int) -> Unit)? = null
) {
    if (items.isEmpty()) {
        Box(modifier = modifier.height(height))
        return
    }

    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    // AutoPlay timer matching Flutter Timer.periodic
    LaunchedEffect(autoPlay, autoPlayIntervalMs, pagerState.isScrollInProgress, items.size) {
        if (autoPlay && items.isNotEmpty()) {
            while (true) {
                delay(autoPlayIntervalMs)
                if (!pagerState.isScrollInProgress && items.isNotEmpty()) {
                    val nextPage = (pagerState.currentPage + 1) % items.size
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                }
            }
        }
    }

    // Page change callback
    LaunchedEffect(pagerState.currentPage) {
        onPageChange?.invoke(pagerState.currentPage)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            pageSpacing = pageSpacing,
            pageSize = if (viewportFraction == 1.0f) {
                PageSize.Fill
            } else {
                PageSize.Fixed(height * viewportFraction)
            }
        ) { pageIndex ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                items[pageIndex]()
            }
        }

        // Bottom Pagination Indicator with Elongated Pill Active Dot
        if (showPagination && items.size > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Ux4gSpace.Space16)
            ) {
                CarouselPagination(
                    totalPageCount = items.size,
                    currentPageIndex = pagerState.currentPage,
                    onPageChange = { targetPage ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = targetPage,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                    },
                    showArrows = showPaginationArrows,
                    variant = paginationVariant,
                    size = paginationSize,
                    activeColor = activePaginationColor,
                    inactiveColor = inactivePaginationColor,
                    arrowBackgroundColor = paginationArrowBackgroundColor,
                    arrowContentColor = paginationArrowColor
                )
            }
        }
    }
}

/**
 * Overload of [Ux4gCarousel] accepting an item count and an indexed item builder composable.
 */
@Composable
fun Ux4gCarousel(
    itemCount: Int,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    autoPlayIntervalMs: Long = 3000L,
    showPagination: Boolean = true,
    showPaginationArrows: Boolean = true,
    height: Dp = 240.dp,
    viewportFraction: Float = 1.0f,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSpacing: Dp = 0.dp,
    activePaginationColor: Color = Ux4gColors.primary,
    inactivePaginationColor: Color = Ux4gColors.primary100,
    paginationArrowBackgroundColor: Color = Color(0xFFDCD6FD),
    paginationArrowColor: Color = Ux4gColors.primary,
    paginationVariant: Ux4gPaginationVariant = Ux4gPaginationVariant.Default,
    paginationSize: Ux4gPaginationSize = Ux4gPaginationSize.Small,
    onPageChange: ((Int) -> Unit)? = null,
    itemContent: @Composable (page: Int) -> Unit
) {
    val items = List(itemCount) { index ->
        @Composable { itemContent(index) }
    }
    Ux4gCarousel(
        items = items,
        modifier = modifier,
        autoPlay = autoPlay,
        autoPlayIntervalMs = autoPlayIntervalMs,
        showPagination = showPagination,
        showPaginationArrows = showPaginationArrows,
        height = height,
        viewportFraction = viewportFraction,
        contentPadding = contentPadding,
        pageSpacing = pageSpacing,
        activePaginationColor = activePaginationColor,
        inactivePaginationColor = inactivePaginationColor,
        paginationArrowBackgroundColor = paginationArrowBackgroundColor,
        paginationArrowColor = paginationArrowColor,
        paginationVariant = paginationVariant,
        paginationSize = paginationSize,
        onPageChange = onPageChange
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Reusable Slide Builders Matching Flutter Design
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Hero banner slide with gradient background, pill badge, title, description, and action button.
 */
@Composable
fun Ux4gHeroCarouselSlide(
    gradientColors: List<Color>,
    badgeText: String,
    title: String,
    description: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    borderRadius: Dp = 16.dp,
    onButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(borderRadius))
            .background(
                Brush.linearGradient(gradientColors)
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    style = defaultTypography.lS_strong.copy(
                        color = Color.White,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            // Title & Description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = defaultTypography.hS_strong.copy(
                        color = Color.White,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = description,
                    style = defaultTypography.bS_default.copy(
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    ),
                    maxLines = 2
                )
            }

            // Action Button
            Ux4gButton(
                onClick = onButtonClick,
                backgroundColor = Color.White,
                contentColor = gradientColors.firstOrNull() ?: Ux4gColors.primary,
                text = buttonText,
                textStyle = defaultTypography.bM_strong.copy(fontSize = 13.sp)
            )
        }
    }
}

/**
 * Image gallery slide with background image, dark gradient overlay, top-left badge, and title/subtitle.
 */
@Composable
fun Ux4gImageCarouselSlide(
    imageUrl: String,
    badgeText: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    borderRadius: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(borderRadius))
    ) {
        // Image
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xDD000000))
                    )
                )
        )

        // Top-left badge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = badgeText,
                style = defaultTypography.lS_strong.copy(
                    color = Color.White,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }

        // Bottom title & subtitle
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = defaultTypography.hS_strong.copy(
                    color = Color.White,
                    fontSize = 20.sp
                )
            )
            Text(
                text = subtitle,
                style = defaultTypography.bS_default.copy(
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                ),
                maxLines = 2
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Internal Pagination Dotted Component for Carousel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CarouselPagination(
    totalPageCount: Int,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    showArrows: Boolean,
    variant: Ux4gPaginationVariant,
    size: Ux4gPaginationSize,
    activeColor: Color,
    inactiveColor: Color,
    arrowBackgroundColor: Color,
    arrowContentColor: Color
) {
    val containerModifier = if (variant == Ux4gPaginationVariant.Capsule) {
        Modifier
            .background(Ux4gColors.white, RoundedCornerShape(percent = 50))
            .border(1.dp, activeColor.copy(alpha = 0.12f), RoundedCornerShape(percent = 50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    } else {
        Modifier
    }

    Row(
        modifier = containerModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showArrows) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(arrowBackgroundColor)
                    .clickable(enabled = currentPageIndex > 0) {
                        onPageChange(currentPageIndex - 1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Previous",
                    tint = if (currentPageIndex > 0) arrowContentColor else arrowContentColor.copy(alpha = 0.38f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until totalPageCount) {
                val isSelected = i == currentPageIndex
                val dotWidth by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                    label = "dotWidth"
                )
                val backgroundColor = if (isSelected) activeColor else inactiveColor

                Box(
                    modifier = Modifier
                        .size(width = dotWidth, height = 8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(backgroundColor)
                        .clickable { onPageChange(i) }
                )
            }
        }

        if (showArrows) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(arrowBackgroundColor)
                    .clickable(enabled = currentPageIndex < totalPageCount - 1) {
                        onPageChange(currentPageIndex + 1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Next",
                    tint = if (currentPageIndex < totalPageCount - 1) arrowContentColor else arrowContentColor.copy(alpha = 0.38f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Carousel Preview", showBackground = true)
@Preview(
    name = "Carousel Preview Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Ux4gCarouselPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Hero Banner Carousel (matching user screenshot 1)
        Text(
            text = "1. Hero Banner Carousel",
            style = defaultTypography.tM_strong,
            color = Ux4gColors.neutral900
        )

        Ux4gCarousel(
            height = 240.dp,
            autoPlay = false,
            items = listOf(
                {
                    Ux4gHeroCarouselSlide(
                        gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)),
                        badgeText = "FEATURED",
                        title = "Digital Services Portal 2.0",
                        description = "Seamlessly access government citizen services with single sign-on and instant verification.",
                        buttonText = "Explore Services"
                    )
                },
                {
                    Ux4gHeroCarouselSlide(
                        gradientColors = listOf(Color(0xFF0D9488), Color(0xFF0284C7)),
                        badgeText = "NEW UPDATE",
                        title = "Biometric Identity Capture",
                        description = "Fast, secure face verification integration with real-time liveness checks.",
                        buttonText = "View Documentation"
                    )
                },
                {
                    Ux4gHeroCarouselSlide(
                        gradientColors = listOf(Color(0xFFD97706), Color(0xFFE11D48)),
                        badgeText = "POPULAR",
                        title = "Unified Design System",
                        description = "Production-ready accessible Android UI components built for digital infrastructure.",
                        buttonText = "Get Started"
                    )
                }
            )
        )

        // 2. Image Gallery Carousel (matching user screenshot 2)
        Text(
            text = "2. Image Gallery Carousel",
            style = defaultTypography.tM_strong,
            color = Ux4gColors.neutral900
        )

        Ux4gCarousel(
            height = 240.dp,
            autoPlay = false,
            activePaginationColor = Color(0xFF5E3CEE),
            inactivePaginationColor = Color.White.copy(alpha = 0.8f),
            paginationArrowBackgroundColor = Color(0xFFDCD6FD),
            paginationArrowColor = Color(0xFF5E3CEE),
            items = listOf(
                {
                    Ux4gImageCarouselSlide(
                        imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=800&q=80",
                        badgeText = "NETWORK",
                        title = "Global Cloud Infrastructure",
                        subtitle = "High availability and low latency cross-region deployment."
                    )
                },
                {
                    Ux4gImageCarouselSlide(
                        imageUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?auto=format&fit=crop&w=800&q=80",
                        badgeText = "DESIGN",
                        title = "Modern UI System Architecture",
                        subtitle = "Crafted with accessible design tokens & fluid responsive components."
                    )
                },
                {
                    Ux4gImageCarouselSlide(
                        imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80",
                        badgeText = "SECURITY",
                        title = "Cryptographic Hardware Modules",
                        subtitle = "Next-generation secure key storage and identity management."
                    )
                }
            )
        )
    }
}
