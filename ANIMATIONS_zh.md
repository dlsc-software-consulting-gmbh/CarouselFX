# CarouselFX 内置动画列表

[English](ANIMATIONS.md) | [中文](ANIMATIONS_zh.md)

> 返回 [README](README_zh.md)

## 列说明

- **方向感知**：「是」表示 FORWARD 和 BACKWARD 方向产生不同的视觉效果；「否」表示两个方向的动画表现一致
- **截图**：「是」表示动画需要对当前页面进行截图来实现效果，这类动画通常不是 Resize 安全的；「否」表示直接操作节点，无需截图，天然支持 Resize

## Resize 安全的动画

动画播放期间容器尺寸变化时自动适应，无需中断。

| 动画 | 效果描述 | 方向感知 | 截图 |
|------|---------|:-------:|:---:|
| AnimSlide | 水平/垂直滑动 | 是 | 否 |
| AnimFade | 交叉淡入淡出 | 否 | 否 |
| AnimFlip | 卡片翻转 (scaleX/Y) | 是 | 否 |
| AnimBox | 3D 盒子 (PerspectiveTransform) | 是 | 否 |
| AnimCube | 3D 立方体旋转（透视投影） | 是 | 否 |
| AnimCube4 | 四面立方体 (scaleX + translateX) | 是 | 否 |
| AnimFold | 折叠 (PerspectiveTransform) | 是 | 否 |
| AnimSwing | 摆门 (PerspectiveTransform) | 是 | 否 |
| AnimConveyor | 传送带（缩放 + 滑动） | 是 | 否 |
| AnimGallery | 画廊浏览（纵深缩放 + 淡出） | 是 | 否 |
| AnimParallax | 视差滑动（分层纵深感） | 是 | 否 |
| AnimBounce | 弹性滑动（过冲回弹） | 是 | 否 |
| AnimCards | Tinder 风格卡片滑动 | 是 | 否 |
| AnimNewspaper | 报纸旋转（旋转 + 缩放） | 是 | 否 |
| AnimStack | 两阶段堆叠 | 是 | 否 |
| AnimSlideIn | QQ 音乐风格滑入 | 是 | 否 |
| AnimRotate | Z 轴旋转 | 是 | 否 |
| AnimZoom | 缩放 + 旋转 + 淡出 | 否 | 否 |
| AnimDoorway | 电影化推近穿越 | 否 | 否 |
| AnimDipToColor | 经由纯色淡入淡出 | 否 | 否 |
| AnimBlend | 混合模式淡入淡出 | 否 | 否 |
| AnimGaussianBlur | 高斯模糊 + 淡出 | 否 | 否 |
| AnimMotionBlur | 运动模糊 + 淡出 | 否 | 否 |
| AnimWipe | 单线擦除 | 是 | 否 |
| AnimSplitWipe | 从中心分裂擦除 | 是 | 否 |
| AnimBlinds | 百叶窗 | 是 | 否 |
| AnimRectangle | 矩形裁剪展开 | 是 | 否 |
| AnimCircle | 圆形裁剪展开 | 是 | 否 |
| AnimCircles | 多个随机圆形 | 是 | 否 |
| AnimCircleWave | 圆形波浪聚拢/扩散 | 否 | 否 |
| AnimCross | 十字形展开 | 是 | 否 |
| AnimCorner | 四角矩形 | 是 | 否 |
| AnimSector | 扇形扫描 | 是 | 否 |
| AnimShapeReveal | 形状裁剪 + 缩放/旋转 | 是 | 否 |
| AnimIris | 光圈叶片收缩 | 是 | 否 |
| AnimShutter | 快门叶片 | 是 | 否 |
| AnimPeel | 翻页剥离效果 | 是 | 否 |
| AnimCheckerboard | 棋盘格交替揭示 | 是 | 否 |
| AnimComb | 梳齿交错条带 | 是 | 否 |
| AnimRipple | 同心圆波纹扩散 | 否 | 否 |
| AnimWedge | V 形剪刀口 / 幕布合拢 | 是 | 否 |
| AnimDiamond | 菱形从中心扩张揭示 | 是 | 否 |
| AnimSerpentine | 网格蛇形揭示 | 是 | 否 |
| AnimSpiralTiles | 螺旋路径网格揭示 | 是 | 否 |
| AnimSwap | 弧线路径交换位置 | 是 | 否 |
| AnimDrain | 漩涡缩旋吸入 | 是 | 否 |
| AnimPinwheel | 风车叶片旋转擦除 | 是 | 否 |
| AnimReveal | 一页滑走，另一页静止不动 | 是 | 否 |
| AnimWhipPan | 极速滑动 + 运动模糊 | 是 | 否 |
| AnimCurtain | 幕布落下（过冲回弹） | 是 | 否 |
| AnimTiltSlide | 透视倾斜滑动（曲面感） | 是 | 否 |
| AnimSqueeze | CRT 关机效果（压缩→展开） | 否 | 否 |
| AnimShrink | 缩向中心 + 淡出 | 否 | 否 |
| AnimFlash | 明亮闪光，峰值时切换页面 | 否 | 否 |
| AnimZigzagWipe | 锯齿边缘擦除 | 是 | 否 |
| AnimNone | 无动画（瞬间切换） | 否 | 否 |

## 非 Resize 安全的动画

如果动画播放过程中容器尺寸发生变化，动画会立即跳到终态，直接显示下一页。
实际使用中几乎无感知——过渡动画通常很短（不到 1 秒），用户很少在翻页瞬间调整窗口大小。

| 动画 | 效果描述 | 方向感知 | 截图 |
|------|---------|:-------:|:---:|
| AnimAround | 三页环绕过渡 | 是 | 否 |
| AnimAccordion | 手风琴折叠 (PerspectiveTransform) | 是 | 是 |
| AnimDomino | 多米诺骨牌倒下 | 是 | 是 |
| AnimSpinStrips | 旋转条带 | 是 | 是 |
| AnimWind | 风吹条带 | 是 | 是 |
| AnimMosaic | 马赛克瓷砖散开 | 是 | 是 |
| AnimRandomTiles | 瓷砖散开 + 旋转 | 否 | 是 |
| AnimShatter | 三角碎片爆炸 | 是 | 是 |
| AnimShatterRadial | 从指定原点辐射炸裂 | 否 | 是 |
| AnimHoneycomb | 蜂窝单元散开 | 否 | 是 |
| AnimShred | 纸张撕碎条带 | 是 | 是 |
| AnimGlitch | 数字故障 RGB 通道偏移 | 是 | 是 |
| AnimPixelate | 渐进式马赛克像素化 | 否 | 是 |
| AnimDissolve | 随机方块淡出溶解 | 是 | 是 |
| AnimBarn | 3D 谷仓门透视开合 | 否 | 是 |
| AnimLouver | 翻页式百叶条带（透视翻转） | 是 | 是 |
| AnimMelt | 竖条从中心向外依次滴落 | 是 | 是 |
| AnimSplit | 条带交替左右滑出（拉链效果） | 是 | 是 |
| AnimCascade | 条带依次瀑布式掉落 | 是 | 是 |

## AnimSelector — 动态动画选择

`AnimSelector` 是一个特殊的动画调度器，本身不产生动画效果，而是根据策略委托给其他动画执行。适用于每次翻页使用不同动画效果的场景。

**按顺序轮换：**

```java
// 按顺序依次使用列表中的动画
carousel.setAnimation(AnimSelector.sequence(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

**随机选择：**

```java
// 每次翻页随机选择一个动画（当动画数量大于 2 时，不会连续两次选中同一个）
carousel.setAnimation(AnimSelector.random(new AnimCube(), new AnimDissolve(), new AnimFold()));
```

**自定义策略：**

```java
// 通过 TransitionResolver 实现完全自定义的动画选择逻辑
carousel.setAnimation(new AnimSelector((fromIndex, toIndex) -> {
    return toIndex == 0 ? new AnimCube() : new AnimFade();
}));
```
