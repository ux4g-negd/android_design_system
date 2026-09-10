import React from 'react';

interface MIconProps {
  name: string;
  size?: number;
}

/**
 * Renders a Google Material Symbols Outlined icon inline in MDX content.
 * Usage in MDX: <MIcon name="rocket_launch" />
 */
export function MIcon({ name, size = 22 }: MIconProps) {
  return (
    <span
      className="material-symbols-outlined"
      style={{
        fontSize: size,
        verticalAlign: 'middle',
        marginRight: 6,
        lineHeight: 1,
      }}
      aria-hidden="true"
    >
      {name}
    </span>
  );
}
