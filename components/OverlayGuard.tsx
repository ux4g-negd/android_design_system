'use client';

import { useEffect } from 'react';

/**
 * OverlayGuard — Prevents stuck dialog/search backdrops from blocking clicks.
 *
 * Root cause: Tailwind v4 transitions include `display` and `pointer-events`
 * with `transition-behavior: allow-discrete`. During rapid client-side
 * navigation, React 19's concurrent transitions can leave Base UI Dialog
 * backdrops in a "transitioning" state where they cover the full viewport
 * at z-index 50 and capture all pointer events.
 *
 * This component uses a MutationObserver to instantly neutralize any
 * closed/hidden overlay before CSS transitions can keep it interactive.
 */
export function OverlayGuard() {
  useEffect(() => {
    function neutralizeStuckOverlays() {
      // Find all closed/hidden presentation overlays (dialog/search backdrops)
      const overlays = document.querySelectorAll(
        '[role="presentation"][data-closed], [role="presentation"][hidden]'
      );
      overlays.forEach((el) => {
        const htmlEl = el as HTMLElement;
        if (htmlEl.style.display !== 'none') {
          htmlEl.style.setProperty('display', 'none', 'important');
          htmlEl.style.setProperty('pointer-events', 'none', 'important');
          htmlEl.style.setProperty('visibility', 'hidden', 'important');
          htmlEl.style.setProperty('transition', 'none', 'important');
        }
      });

      // Ensure body always accepts pointer events
      if (document.body.style.pointerEvents === 'none') {
        document.body.style.pointerEvents = 'auto';
      }
    }

    // Run immediately on mount
    neutralizeStuckOverlays();

    // Watch for attribute changes that signal a backdrop getting stuck
    const observer = new MutationObserver((mutations) => {
      let needsCheck = false;
      for (const mutation of mutations) {
        if (
          mutation.type === 'attributes' &&
          (mutation.attributeName === 'data-closed' ||
            mutation.attributeName === 'hidden' ||
            mutation.attributeName === 'data-state' ||
            mutation.attributeName === 'style')
        ) {
          needsCheck = true;
          break;
        }
        // Also check for added nodes (portaled overlays)
        if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
          needsCheck = true;
          break;
        }
      }
      if (needsCheck) {
        neutralizeStuckOverlays();
      }
    });

    observer.observe(document.documentElement, {
      attributes: true,
      subtree: true,
      childList: true,
      attributeFilter: ['data-closed', 'hidden', 'data-state', 'style'],
    });

    // Also run on every popstate (back/forward navigation)
    window.addEventListener('popstate', neutralizeStuckOverlays);

    // Periodic safety check every 500ms as ultimate fallback
    const interval = setInterval(neutralizeStuckOverlays, 500);

    return () => {
      observer.disconnect();
      window.removeEventListener('popstate', neutralizeStuckOverlays);
      clearInterval(interval);
    };
  }, []);

  return null;
}
