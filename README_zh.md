# CarouselFX

[![Java Version](https://img.shields.io/badge/Java_Version-11+-ff69b4)](https://github.com/openjdk/jdk)
[![JavaFX Version](https://img.shields.io/badge/JavaFX_Version-11+-brightgreen)](https://github.com/openjdk/jfx)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[English](README.md) | 中文

一个功能丰富的 JavaFX 轮播图/幻灯片组件，内置 75+ 种页面切换动画效果。开箱即用，可用于图片展示、广告 Banner、应用程序页面导航等场景。

## 效果预览

<p>
  <img src="screenshots/carousel-1.gif" alt="动画效果预览 1" width="270"/>
  <img src="screenshots/carousel-2.gif" alt="动画效果预览 2" width="270"/>
  <img src="screenshots/carousel-3.gif" alt="动画效果预览 3" width="270"/>
</p>

## 目录

- [效果预览](#效果预览)
- [特性](#特性)
- [快速开始](#快速开始)
- [内置动画](#内置动画)
- [页面生命周期事件](#页面生命周期事件)
- [导航器](#导航器)
- [显示模式](#显示模式)
- [ImagePane](#imagepane)
- [CSS 样式](#css-样式)
- [示例程序](#示例程序)
- [构建](#构建)
- [项目背景](#项目背景)

## 特性

- **75+ 种切换动画** — 滑动、淡入淡出、翻转、立方体、碎裂、百叶窗等
- **自动播放** — 可配置间隔时长、鼠标悬停暂停、倒计时进度
- **循环/非循环** 导航模式
- **左右箭头** — 内置导航箭头，支持 SHOW / HIDE / AUTO 显示模式
- **底部导航器** — 圆点指示器（页面过多时自动切换为紧凑模式），可通过 `CarouselNavigator` 接口完全替换
- **页面缓存** — 基于距离的淘汰策略
- **生命周期事件** — CACHED、OPENING、OPENED、CLOSING、CLOSED、EVICTED
- **最低 Java 11**，仅依赖 `javafx-controls`

## 快速开始

页面通过 `pageFactory` + `pageCount` 提供，工厂按需创建页面节点：

```java
Carousel carousel = new Carousel();
carousel.setPageCount(5);
carousel.setPageFactory(index -> createPage(index));
```

对于简单场景，`setPages` 是便利方法，内部自动设置工厂和页面数量：

```java
carousel.setPages(page1, page2, page3);
```

## 内置动画

提供 75+ 种页面切换动画，涵盖滑动、3D 变换、碎片化、擦除、模糊等多种风格。一行代码即可切换动画效果：

```java
carousel.setAnimation(new AnimCube());
```

`AnimSelector` 是一个特殊的动画调度器，适用于每次翻页使用不同动画效果的场景：

```java
// 按顺序轮换动画
carousel.setAnimation(AnimSelector.sequence(new AnimCube(), new AnimDissolve(), new AnimFold()));

// 每次翻页随机选择一个动画（当动画数量大于 2 时，不会连续两次选中同一个）
carousel.setAnimation(AnimSelector.random(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

如需自定义动画效果，可以实现 `CarouselAnimation` 接口或继承 `CarouselAnimationBase` 抽象类。

> 完整动画列表及详细说明请参见 [ANIMATIONS_zh.md](ANIMATIONS_zh.md)

## 页面生命周期事件

页面在整个生命周期中触发事件：

```
CACHED → [OPENING → OPENED → CLOSING → CLOSED]* → EVICTED
```

```java
carousel.setOnPageCached(e -> {
    // 页面进入缓存 — 执行一次性初始化
});
carousel.setOnPageOpening(e -> {
    // 页面即将显示 — 加载数据
});
carousel.setOnPageClosed(e -> {
    // 页面已隐藏 — 暂停后台任务
});
carousel.setOnPageEvicted(e -> {
    // 页面从缓存中移除 — 释放重量级资源
});
```

| 事件 | 触发时机 |
|------|---------|
| `CACHED` | 页面从 pageFactory 获取并进入缓存 |
| `OPENING` | 页面开始出现（具体时机由动画决定） |
| `OPENED` | 页面完全可见，过渡完成 |
| `CLOSING` | 页面开始消失 |
| `CLOSED` | 页面完全隐藏 |
| `EVICTED` | 页面从缓存中被淘汰（超出缓存距离或手动调用 clearPageCache） |

## 导航器

底部导航区域由 `CarouselNavigator` 接口控制。内置的 `DefaultNavigator` 提供两种模式：

- **圆点模式**（pageCount ≤ maxDots）：可点击的圆点指示器
- **紧凑模式**（pageCount > maxDots）：`[◄] 3 / 200 [►]` 带箭头按钮

**1. 默认导航器**（不设置即为默认，自动在圆点和紧凑模式间切换）：

```java
carousel.setNavigator(new DefaultNavigator());
```

**2. 自定义导航器**（实现 `CarouselNavigator` 接口）：

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
        label.setText("第 " + (newIndex + 1) + " 页，共 " + pageCount + " 页");
    }
    @Override
    public void dispose() { label = null; }
});
```

**3. 不使用底部导航器**：

```java
carousel.setNavigator(null);
```

## 显示模式

导航箭头和底部导航器支持三种显示模式：

| 模式 | 行为 |
|------|------|
| `SHOW` | 始终可见 |
| `HIDE` | 始终隐藏 |
| `AUTO` | 鼠标悬停时可见，移出后隐藏（带淡入淡出过渡） |

```java
carousel.setArrowDisplayMode(DisplayMode.AUTO);       // 箭头悬停显示（默认）
carousel.setNavigatorDisplayMode(DisplayMode.SHOW);    // 导航器始终可见（默认）
```

## ImagePane

用于在轮播图中便捷展示图片的容器，支持三种显示模式：

| 模式 | 行为 |
|------|------|
| `COVER` | 填满容器，裁剪溢出部分（默认） |
| `FIT` | 完整显示图片，可能留有空白 |
| `STRETCH` | 拉伸填满，忽略纵横比 |

```java
ImagePane page = new ImagePane(new Image("photo.jpg"));
page.setDisplayMode(ImagePane.ImageDisplayMode.COVER);
page.getChildren().add(new Label("标题")); // 在图片上叠加内容
```

## CSS 样式

场景图结构（所有选择器使用严格的 `>` 直接子选择器）：

```css
/* Carousel */
.carousel > .content-pane
.carousel > .prev-button > .arrow
.carousel > .next-button > .arrow
.carousel > .placeholder

/* DefaultNavigator（独立组件） */
.carousel-navigator > .dot-indicator > .dot
.carousel-navigator > .compact-navigator > .prev-button > .arrow
.carousel-navigator > .compact-navigator > .fraction-label
.carousel-navigator > .compact-navigator > .next-button > .arrow
```

伪类：

| 伪类 | 所在节点 | 说明 |
|------|---------|------|
| `:first-page` | `.carousel` | 当前显示的是第一页 |
| `:last-page` | `.carousel` | 当前显示的是最后一页 |
| `:empty` | `.carousel` | 没有页面（pageCount ≤ 0 或 pageFactory 为 null） |
| `:selected` | `.dot` | 当前选中的圆点指示器 |

## 示例程序

`carousel-samples` 模块包含多个可运行的示例程序，每个聚焦不同的功能点：

| 示例 | 演示内容 |
|------|----------|
| `SimpleDemo` | 用几行代码创建一个最简轮播图 |
| `CarouselShowcase` | 完整的控制面板，浏览全部 75+ 种动画效果和各项配置选项 |
| `ImagePaneDemo` | 使用 `ImagePane` 展示图片，通过 `AnimSelector` 实现随机动画选择 |
| `CustomNavigatorDemo` | 通过 `CarouselNavigator` 接口实现自定义底部导航器 |
| `LifecycleEventDemo` | 利用生命周期事件驱动页面内容动画 |
| `AutoPlayProgressDemo` | 实现自动播放时的页面切换倒计时指示器 |
| `SimpleAppDemo` | 使用轮播图作为应用导航骨架的最简示例，支持嵌套 |
| `CarouselAppDemo` | 将 Carousel 作为完整的应用程序导航容器，展示侧栏路由、嵌套轮播和页面生命周期动画 |

### 运行示例

```
mvn install -DskipTests
mvn javafx:run -pl carousel-samples
```

默认运行 `CarouselShowcase`。

## 构建

```
mvn clean install
```

需要 Java 11+ 和 Maven。`carousel` 模块目标 Java 11 + JavaFX 11+，`carousel-samples` 模块使用 JavaFX 17 运行示例。

## 项目背景

CarouselFX 由 [RXControls](https://github.com/leewyatt/rxcontrols) 的作者开发，是其轮播图组件的独立重写版本。这是一次将大型组件从综合库中剥离为独立项目的尝试。
