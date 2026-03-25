# CarouselFX

[![Java Version](https://img.shields.io/badge/Java_Version-11+-ff69b4)](https://github.com/openjdk/jdk)
[![JavaFX Version](https://img.shields.io/badge/JavaFX_Version-11+-brightgreen)](https://github.com/openjdk/jfx)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

English | [中文](README_zh.md)

A feature-rich carousel / slideshow component for JavaFX with 75+ built-in page transition animations. Ready to use out of the box for image galleries, ad banners, app page navigation, and more.

## Preview

<p>
  <img src="screenshots/carousel-1.gif" alt="Animation preview 1" width="270"/>
  <img src="screenshots/carousel-2.gif" alt="Animation preview 2" width="270"/>
  <img src="screenshots/carousel-3.gif" alt="Animation preview 3" width="270"/>
</p>

## Table of Contents

- [Preview](#preview)
- [Features](#features)
- [Quick Start](#quick-start)
- [Built-in Animations](#built-in-animations)
- [Page Lifecycle Events](#page-lifecycle-events)
- [Navigator](#navigator)
- [Display Modes](#display-modes)
- [ImagePane](#imagepane)
- [CSS Styling](#css-styling)
- [Demos](#demos)
- [Building](#building)
- [Background](#background)

## Features

- **75+ transition animations** — slides, fades, flips, cubes, shatters, blinds, and many more
- **Auto-play** with configurable interval, hover-pause, and countdown progress
- **Circular / non-circular** navigation modes
- **Navigation arrows** — built-in left/right arrows with SHOW / HIDE / AUTO display modes
- **Bottom navigator** — dot indicators (auto-switch to compact mode for large page counts), fully replaceable via `CarouselNavigator` interface
- **Page caching** with distance-based eviction strategy
- **Lifecycle events** — CACHED, OPENING, OPENED, CLOSING, CLOSED, EVICTED
- **Minimum Java 11**, depends only on `javafx-controls`

## Quick Start

Pages are provided via `pageFactory` + `pageCount`. The factory creates
page nodes on demand:

```java
Carousel carousel = new Carousel();
carousel.setPageCount(5);
carousel.setPageFactory(index -> createPage(index));
```

For simple cases, `setPages` is a convenience method that sets up the
factory and count automatically:

```java
carousel.setPages(page1, page2, page3);
```

## Built-in Animations

75+ page transition animations covering slides, 3D transforms, fragmentation, wipes, blurs, and more. Switch animations with a single line of code:

```java
carousel.setAnimation(new AnimCube());
```

`AnimSelector` is a special animation dispatcher for using different animations on each page turn:

```java
// Cycle through animations in order
carousel.setAnimation(AnimSelector.sequence(new AnimCube(), new AnimDissolve(), new AnimFold()));

// Randomly select an animation each time (won't pick the same one twice in a row when pool size > 2)
carousel.setAnimation(AnimSelector.random(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

To create custom animation effects, implement the `CarouselAnimation` interface or extend the `CarouselAnimationBase` abstract class.

> For the full animation list and details, see [ANIMATIONS.md](ANIMATIONS.md)

## Page Lifecycle Events

Pages fire lifecycle events throughout their lifetime:

```
CACHED → [OPENING → OPENED → CLOSING → CLOSED]* → EVICTED
```

```java
carousel.setOnPageCached(e -> {
    // Page entered the cache — perform one-time setup
});
carousel.setOnPageOpening(e -> {
    // Page is about to appear — load data
});
carousel.setOnPageClosed(e -> {
    // Page has disappeared — pause background tasks
});
carousel.setOnPageEvicted(e -> {
    // Page evicted from cache — release heavy resources
});
```

| Event | Timing |
|-------|--------|
| `CACHED` | Page obtained from the page factory and entered the cache |
| `OPENING` | Page begins to appear (timing depends on animation) |
| `OPENED` | Page fully visible, transition complete |
| `CLOSING` | Page begins to disappear |
| `CLOSED` | Page fully hidden |
| `EVICTED` | Page evicted from cache (distance-based or manual) |

## Navigator

The bottom navigation area is controlled by the `CarouselNavigator` interface.
The built-in `DefaultNavigator` provides two modes:

- **Dot mode** (pageCount ≤ maxDots): clickable dot indicators
- **Compact mode** (pageCount > maxDots): `[◄] 3 / 200 [►]` with arrow buttons

**1. Default navigator** (used by default, auto-switches between dot and compact modes):

```java
carousel.setNavigator(new DefaultNavigator());
```

**2. Custom navigator** (implement the `CarouselNavigator` interface):

```java
carousel.setNavigator(new CarouselNavigator() {
    private Label label;
    @Override
    public Node createNode(Carousel carousel) {
        label = new Label();
        return label;
    }
    @Override
    public void onPageChanged(int oldIndex, int newIndex, int pageCount) {
        label.setText("Page " + (newIndex + 1) + " of " + pageCount);
    }
    @Override
    public void dispose() { label = null; }
});
```

**3. No bottom navigator**:

```java
carousel.setNavigator(null);
```

## Display Modes

Navigation arrows and the bottom navigator support three visibility modes:

| Mode | Behavior |
|------|----------|
| `SHOW` | Always visible |
| `HIDE` | Always hidden |
| `AUTO` | Visible on mouse hover, hidden otherwise (with fade transition) |

```java
carousel.setArrowDisplayMode(DisplayMode.AUTO);       // arrows on hover (default)
carousel.setNavigatorDisplayMode(DisplayMode.SHOW);    // navigator always visible (default)
```

## ImagePane

A convenience container for displaying images in the carousel with three
display modes:

| Mode | Behavior |
|------|----------|
| `COVER` | Fill the pane, clip overflow (default) |
| `FIT` | Fit inside the pane, may leave empty areas |
| `STRETCH` | Stretch to fill, ignoring aspect ratio |

```java
ImagePane page = new ImagePane(new Image("photo.jpg"));
page.setDisplayMode(ImagePane.ImageDisplayMode.COVER);
page.getChildren().add(new Label("Caption")); // overlay on top of image
```

## CSS Styling

Scene-graph structure (all selectors use strict `>` child combinators):

```css
/* Carousel */
.carousel > .content-pane
.carousel > .prev-button > .arrow
.carousel > .next-button > .arrow
.carousel > .placeholder

/* DefaultNavigator (independent component) */
.carousel-navigator > .dot-indicator > .dot
.carousel-navigator > .compact-navigator > .prev-button > .arrow
.carousel-navigator > .compact-navigator > .fraction-label
.carousel-navigator > .compact-navigator > .next-button > .arrow
```

Pseudo-classes:

| Pseudo-class | Node | Description |
|--------------|------|-------------|
| `:first-page` | `.carousel` | Currently displaying the first page |
| `:last-page` | `.carousel` | Currently displaying the last page |
| `:empty` | `.carousel` | No pages (pageCount ≤ 0 or pageFactory is null) |
| `:selected` | `.dot` | The currently selected dot indicator |

## Demos

The `carousel-samples` module contains runnable demo applications, each
focusing on a specific feature area:

| Demo | What it demonstrates |
|------|----------------------|
| `SimpleDemo` | Creating a minimal carousel in just a few lines of code |
| `CarouselShowcase` | Comprehensive control panel for exploring all 75+ animations and configuration options |
| `ImagePaneDemo` | Displaying images with `ImagePane` and random animation selection via `AnimSelector` |
| `CustomNavigatorDemo` | Implementing a custom bottom navigator via the `CarouselNavigator` interface |
| `LifecycleEventDemo` | Driving page content animations through lifecycle events |
| `AutoPlayProgressDemo` | Building a countdown indicator for auto-play page switching |
| `SimpleAppDemo` | Minimal app skeleton using Carousel for page navigation with nesting |
| `CarouselAppDemo` | Using Carousel as a full application navigation container with sidebar routing, nested carousels and page lifecycle animations |

### Running a demo

```
mvn install -DskipTests
mvn javafx:run -pl carousel-samples
```

This runs `CarouselShowcase` by default.

## Building

```
mvn clean install
```

Requires Java 11+ and Maven. The `carousel` module targets Java 11 with
JavaFX 11+. The `carousel-samples` module uses JavaFX 17 for demos.

## Background

CarouselFX is developed by the author of [RXControls](https://github.com/leewyatt/rxcontrols) and is an independent rewrite of its carousel component. This is an attempt to extract a large-scale component from a comprehensive library into a standalone project.
