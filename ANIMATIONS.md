# CarouselFX Built-in Animations

English | [中文](ANIMATIONS_zh.md)

> Back to [README](README.md)

## Column Legend

- **Direction**: "Yes" means FORWARD and BACKWARD produce different visual effects; "No" means the animation looks the same in both directions
- **Snapshot**: "Yes" means the animation captures a screenshot of the current page to create the effect — these animations are typically not resize-safe; "No" means nodes are manipulated directly without snapshots, naturally supporting resize

## Resize-safe Animations

These animations adapt smoothly to container size changes during playback.

| Animation | Description | Direction | Snapshot |
|-----------|-------------|:---------:|:--------:|
| AnimSlide | Horizontal / vertical slide | Yes | No |
| AnimFade | Cross-fade | No | No |
| AnimFlip | Card flip (scaleX/Y) | Yes | No |
| AnimBox | 3D box with PerspectiveTransform | Yes | No |
| AnimCube | 3D cube rotation (perspective projection) | Yes | No |
| AnimCube4 | Four-face cube (scaleX + translateX) | Yes | No |
| AnimFold | Folding with PerspectiveTransform | Yes | No |
| AnimSwing | Swing door with PerspectiveTransform | Yes | No |
| AnimConveyor | Conveyor belt (scale + slide) | Yes | No |
| AnimGallery | Gallery walk with depth scaling and fade | Yes | No |
| AnimParallax | Parallax slide with layered depth | Yes | No |
| AnimBounce | Elastic slide with overshoot spring-back | Yes | No |
| AnimCards | Tinder-style card swipe | Yes | No |
| AnimNewspaper | Newspaper spin (rotate + scale) | Yes | No |
| AnimStack | Two-phase stacking | Yes | No |
| AnimSlideIn | QQ Music style slide-in | Yes | No |
| AnimRotate | Z-axis rotation | Yes | No |
| AnimZoom | Scale + rotate + fade | No | No |
| AnimDoorway | Cinematic zoom-through push-in | No | No |
| AnimDipToColor | Fade through a solid color | No | No |
| AnimBlend | Blend mode fade | No | No |
| AnimGaussianBlur | Gaussian blur + fade | No | No |
| AnimMotionBlur | Motion blur + fade | No | No |
| AnimWipe | Single-line wipe | Yes | No |
| AnimSplitWipe | Split wipe from center | Yes | No |
| AnimBlinds | Venetian blinds | Yes | No |
| AnimRectangle | Rectangle clip expand | Yes | No |
| AnimCircle | Circle clip expand | Yes | No |
| AnimCircles | Multiple random circles | Yes | No |
| AnimCircleWave | Circle wave gather/expand | No | No |
| AnimCross | Cross shape expand | Yes | No |
| AnimCorner | Four corner rectangles | Yes | No |
| AnimSector | Arc/pie sweep | Yes | No |
| AnimShapeReveal | Shape clip with scale/rotate | Yes | No |
| AnimIris | Iris blades closing | Yes | No |
| AnimShutter | Camera shutter blades | Yes | No |
| AnimPeel | Page peel with fold-back | Yes | No |
| AnimCheckerboard | Checkerboard alternating reveal | Yes | No |
| AnimComb | Interlocking comb strips | Yes | No |
| AnimRipple | Concentric ripple wavefronts | No | No |
| AnimWedge | V-shaped scissors / curtains | Yes | No |
| AnimDiamond | Diamond (rhombus) reveal from center | Yes | No |
| AnimSerpentine | Grid serpentine reveal | Yes | No |
| AnimSpiralTiles | Spiral path grid reveal | Yes | No |
| AnimSwap | Arc-path position exchange | Yes | No |
| AnimDrain | Vortex shrink-spin into drain | Yes | No |
| AnimPinwheel | Windmill blade rotation wipe | Yes | No |
| AnimReveal | One page slides away, the other stays still | Yes | No |
| AnimWhipPan | Ultra-fast slide with motion blur | Yes | No |
| AnimCurtain | Theater curtain drop with overshoot bounce | Yes | No |
| AnimTiltSlide | Slide with perspective tilt on curved surface | Yes | No |
| AnimSqueeze | CRT TV off — squeeze to line then expand | No | No |
| AnimShrink | Shrink to center with fade | No | No |
| AnimFlash | Bright overlay flash, page switches at peak | No | No |
| AnimZigzagWipe | Sawtooth-edged wipe | Yes | No |
| AnimNone | No animation (instant cut) | No | No |

## Non-resize-safe Animations

If the container is resized mid-animation, these animations instantly jump
to their end state (the next page is displayed immediately). This is barely
noticeable in practice — transitions are typically short (< 1s) and window
resizing during a transition is uncommon.

| Animation | Description | Direction | Snapshot |
|-----------|-------------|:---------:|:--------:|
| AnimAround | 3-page around transition | Yes | No |
| AnimAccordion | Accordion fold with PerspectiveTransform | Yes | Yes |
| AnimDomino | Falling domino strips | Yes | Yes |
| AnimSpinStrips | Spinning strips | Yes | Yes |
| AnimWind | Wind-blown strips | Yes | Yes |
| AnimMosaic | Mosaic tile scatter | Yes | Yes |
| AnimRandomTiles | Tile scatter with rotation | No | Yes |
| AnimShatter | Triangular fragment explosion | Yes | Yes |
| AnimShatterRadial | Radial explosion from origin point | No | Yes |
| AnimHoneycomb | Honeycomb cell scatter | No | Yes |
| AnimShred | Paper shred strips | Yes | Yes |
| AnimGlitch | Digital glitch with RGB channel shift | Yes | Yes |
| AnimPixelate | Progressive mosaic pixelation | No | Yes |
| AnimDissolve | Random block fade-out dissolve | Yes | Yes |
| AnimBarn | 3D barn door split with perspective | No | Yes |
| AnimLouver | Split-flap louver strips with perspective | Yes | Yes |
| AnimMelt | Vertical strips drip down from center outward | Yes | Yes |
| AnimSplit | Strips slide out alternately like a zipper | Yes | Yes |
| AnimCascade | Sequential waterfall strip drop | Yes | Yes |

## AnimSelector — Dynamic Animation Selection

`AnimSelector` is a special animation dispatcher that does not produce visual effects itself. Instead, it delegates to other animations based on a strategy. Use it when you want a different animation for each page turn.

**Sequential rotation:**

```java
// Cycle through the list of animations in order
carousel.setAnimation(AnimSelector.sequence(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

**Random selection:**

```java
// Randomly select an animation each time (won't pick the same one twice in a row when pool size > 2)
carousel.setAnimation(AnimSelector.random(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

**Custom strategy:**

```java
// Use a TransitionResolver for fully custom animation selection logic
carousel.setAnimation(new AnimSelector((fromIndex, toIndex) -> {
    return toIndex == 0 ? new AnimCube() : new AnimFade();
}));
```
