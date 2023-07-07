package com.crschnick.pdxu.app;

import com.crschnick.pdxu.app.core.ComponentManager;
import com.crschnick.pdxu.app.core.ErrorHandler;
import com.crschnick.pdxu.app.core.PdxuInstallation;
import com.crschnick.pdxu.app.core.settings.SavedState;
import com.crschnick.pdxu.app.gui.GuiLayout;
import com.crschnick.pdxu.app.gui.GuiStyle;
import com.crschnick.pdxu.app.util.ImageHelper;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PdxuApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(PdxuApp.class);

    private static PdxuApp APP;

    private GuiLayout layout;
    private Stage stage;
    private Image icon;
    private boolean windowActive;

    public static PdxuApp getApp() {
        return APP;
    }

    public static void main() {
        try {
            launch();
        } catch (Throwable t) {
            ErrorHandler.handleTerminalException(t);
        }
    }

    public void setupWindowState() {
        var w = stage;

        // Set size to default
        w.setWidth(1550);
        w.setHeight(700);

        var s = SavedState.getInstance();

        boolean inBounds = false;
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D visualBounds = screen.getVisualBounds();

            // Check whether the bounds intersect where the intersection is larger than 20 pixels!
            if (s.getWindowWidth() > 40 && s.getWindowHeight() > 40 && visualBounds.intersects(new Rectangle2D(
                    s.getWindowX() + 20,
                    s.getWindowY() + 20,
                    s.getWindowWidth() - 40,
                    s.getWindowHeight() - 40
            ))) {
                inBounds = true;
                break;
            }
        }
        if (inBounds) {
            if (s.getWindowX() != SavedState.INVALID) {
                w.setX(s.getWindowX());
            }
            if (s.getWindowY() != SavedState.INVALID) {
                w.setY(s.getWindowY());
            }
            if (s.getWindowWidth() != SavedState.INVALID) {
                w.setWidth(s.getWindowWidth());
            }
            if (s.getWindowHeight() != SavedState.INVALID) {
                w.setHeight(s.getWindowHeight());
            }
        } else {
            logger.warn("Saved window was out of bounds");
        }

        stage.xProperty().addListener((c, o, n) -> {
            if (windowActive) {
                logger.debug("Changing window x to " + n.intValue());
                s.setWindowX(n.intValue());
            }
        });
        stage.yProperty().addListener((c, o, n) -> {
            if (windowActive) {
                logger.debug("Changing window y to " + n.intValue());
                s.setWindowY(n.intValue());
            }
        });
        stage.widthProperty().addListener((c, o, n) -> {
            if (windowActive) {
                logger.debug("Changing window width to " + n.intValue());
                s.setWindowWidth(n.intValue());
            }
        });
        stage.heightProperty().addListener((c, o, n) -> {
            if (windowActive) {
                logger.debug("Changing window height to " + n.intValue());
                s.setWindowHeight(n.intValue());
            }
        });
        w.maximizedProperty().addListener((c, o, n) -> {
            if (windowActive) {
                logger.debug("Changing window maximized to " + n);
                s.setMaximized(n);
            }
        });

        w.show();
        windowActive = true;

        // Fix bug with DPI scaling.
        // Window only calculates its right content size when resized AFTER being shown
        w.setWidth(w.getWidth() + 1);
        w.setWidth(w.getWidth() - 2);


        // Set maximize only after fixing the DPI scaling
        if (s.isMaximized()) {
            // This is a bug in JavaFX, maximizing the window breaks the DPI scaling
            // w.setMaximized(true);
        }
    }

    public void setupBasicWindowContent() {
        layout = new GuiLayout();
        layout.setup();
        var title = "Pdx-Unlimiter (" + PdxuInstallation.getInstance().getVersion() + ")";
        var l = PdxuInstallation.getInstance().getLatestVersion();
        if (PdxuInstallation.getInstance().isProduction() &&
                !PdxuInstallation.getInstance().isStandalone() &&
                l != null &&
                !l.equals(PdxuInstallation.getInstance().getVersion())) {
            title = title + "     OUTDATED: " + l + " available";
        }
        stage.setTitle(title);

        var aa = Platform.isSupported(ConditionalFeature.SCENE3D) ?
                SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED;
        var scene = new Scene(layout.getContent(), -1, -1, false, aa);
        stage.setScene(scene);

        GuiStyle.addStylesheets(scene);
        layout.getContent().requestLayout();
    }

    public void setupCompleteWindowContent() {
        layout.finishSetup();
    }

    @Override
    public void start(Stage primaryStage) {
        ErrorHandler.setPlatformInitialized();

        APP = this;
        stage = primaryStage;

        addIcons(stage);

        primaryStage.setOnCloseRequest(event -> {
            windowActive = false;

            ComponentManager.finalTeardown();
            // Close other windows
            Stage.getWindows().stream()
                    .filter(w -> !w.equals(stage))
                    .collect(Collectors.toList())
                    .forEach(w -> w.fireEvent(event));
        });

        ComponentManager.initialPlatformSetup();
    }

    private void addIcons(Stage stage) {
        stage.getIcons().clear();
        for (String s : List.of(
                "logo_16x16.png",
                "logo_24x24.png",
                "logo_32x32.png",
                "logo_48x48.png",
                "logo_128x128.png",
                "logo_256x256.png")) {
            stage.getIcons().add(ImageHelper.loadImage(PdxuInstallation.getInstance().getResourceDir().resolve("logo/" + s)));
        }
    }

    public Image getIcon() {
        return icon;
    }

    public Stage getStage() {
        return stage;
    }
}
