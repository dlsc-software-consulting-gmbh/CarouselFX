package com.dlsc.carousel.samples;

import com.dlsc.carousel.Carousel;
import com.dlsc.carousel.ImagePane;
import com.dlsc.carousel.animation.AnimBox;
import com.dlsc.carousel.animation.AnimCube;
import com.dlsc.carousel.animation.AnimCube4;
import com.dlsc.carousel.animation.AnimCurtain;
import com.dlsc.carousel.animation.AnimDissolve;
import com.dlsc.carousel.animation.AnimGallery;
import com.dlsc.carousel.animation.AnimGlitch;
import com.dlsc.carousel.animation.AnimLouver;
import com.dlsc.carousel.animation.AnimPeel;
import com.dlsc.carousel.animation.AnimRipple;
import com.dlsc.carousel.animation.AnimSelector;
import com.dlsc.carousel.animation.AnimShatter;
import com.dlsc.carousel.animation.AnimRandomTiles;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Demo showcasing {@link ImagePane} with the carousel.
 */
public class ImagePaneDemo extends Application {

    private static final int IMAGE_COUNT = 6;

    @Override
    public void start(Stage stage) {
        Carousel carousel = new Carousel();
        carousel.setPageCount(IMAGE_COUNT);
        carousel.setPageFactory(index -> {
            String url = getClass().getResource("images/" + (index + 1) + ".png").toExternalForm();
            ImagePane imagePane = new ImagePane(new Image(url, true));

            // add a title to each image (optional)
            addTitleNode(index, imagePane);

            return imagePane;
        });

        // 1. Apply a fixed animation effect for all carousel transitions
        // carousel.setAnimation(new AnimGallery());

        // 2. Randomly select an animation effect from a predefined pool for each page turn
        carousel.setAnimation(AnimSelector.random(
                new AnimRandomTiles(), new AnimPeel(), new AnimGlitch(), new AnimGallery(), new AnimCurtain(), new AnimCube4(),
                new AnimDissolve(), new AnimShatter(), new AnimRipple(5), new AnimCube(), new AnimBox(), new AnimLouver()));

        carousel.setAutoPlay(true);
        carousel.setHoverPause(false);
        carousel.setAnimationDuration(Duration.seconds(2));
        carousel.setAutoPlayInterval(Duration.seconds(1));
        carousel.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(carousel, 650, 350);
        scene.setCamera(new PerspectiveCamera());
        stage.setTitle("CarouselFX - ImagePane Demo");
        stage.setScene(scene);
        stage.show();
    }

    private void addTitleNode(Integer index, ImagePane imagePane) {
        Label imageTitle = new Label("Image " + (index + 1));
        imageTitle.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;");
        imagePane.getChildren().add(imageTitle);
        StackPane.setAlignment(imageTitle, Pos.BOTTOM_CENTER);
        StackPane.setMargin(imageTitle, new Insets(0, 0, 35, 0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
