package com.crschnick.pdxu.app.gui;

import com.crschnick.pdxu.app.core.ErrorHandler;
import com.crschnick.pdxu.app.core.PdxuInstallation;
import javafx.scene.Scene;

import java.io.IOException;
import java.nio.file.Files;

public class GuiStyle {

    public static String CLASS_CAMPAIGN_LIST = "campaign-list";
    public static String CLASS_DIPLOMACY_ROW = "diplomacy-row";
    public static String CLASS_CAMPAIGN_ENTRY_NODE = "node";
    public static String CLASS_CAMPAIGN_ENTRY_NODE_CONTENT = "node-content";
    public static String CLASS_CAMPAIGN_LIST_ENTRY = "campaign-list-entry";
    public static String CLASS_DATE = "date";
    public static String CLASS_CAMPAIGN_ENTRY_NODE_CONTAINER = "node-container";
    public static String CLASS_COMPATIBLE = "compatible";
    public static String CLASS_INCOMPATIBLE = "incompatible";
    public static String CLASS_RULER = "ruler";
    public static String CLASS_WAR = "war";
    public static String CLASS_ALLIANCE = "alliance";
    public static String CLASS_MARRIAGE = "marriage";
    public static String CLASS_GUARANTEE = "guarantee";
    public static String CLASS_VASSAL = "vassal";
    public static String CLASS_TRUCE = "vassal";
    public static String CLASS_IMAGE_ICON = "image-icon";
    public static String CLASS_POWER_ICON = "power-icon";
    public static String CLASS_RULER_ICON = "ruler-icon";
    public static String CLASS_TAG_ICON = "tag-icon";
    public static String CLASS_TEXT = "text";
    public static String CLASS_TEXT_FIELD = "text-field";
    public static String CLASS_ENTRY_BAR = "entry-bar";
    public static String CLASS_ENTRY_LIST = "entry-list";
    public static String CLASS_ENTRY_LOADING = "entry-loading";
    public static String CLASS_STATUS_BAR = "status-bar";
    public static String CLASS_STATUS_RUNNING = "status-running";
    public static String CLASS_STATUS_INCOMPATIBLE = "status-incompatible";
    public static String CLASS_EXPORT = "export-button";
    public static String CLASS_BROWSE = "browse-button";
    public static String CLASS_LAUNCH = "launch-button";
    public static String CLASS_KILL = "kill-button";
    public static String CLASS_IMPORT = "import-button";
    public static String CLASS_DELETE = "delete-button";
    public static String CLASS_SAVEGAME = "savegame";
    public static String CLASS_ALERT = "alert-icon";
    public static String CLASS_ENTRY = "entry";
    public static String CLASS_TAG_BAR = "tag-bar";
    public static String CLASS_BUTTON_BAR = "button-bar";
    public static String CLASS_GAME_ICON = "game-icon";
    public static String CLASS_GAME_ICON_BAR = "game-icon-bar";
    public static String CLASS_SWTICH_GAME = "switch-game-button";
    public static String CLASS_CONTENT = "needed-content";
    public static String CLASS_NO_CAMPAIGN = "no-campaign";
    public static String CLASS_LOADING = "loading";
    public static String CLASS_CONVERT = "convert-button";
    public static String CLASS_MAP = "map-button";
    public static String CLASS_ANALYZE = "analyze-button";
    public static String CLASS_MELT = "melt-button";
    public static String CLASS_SEARCH = "search-icon";
    public static String CLASS_FOLDER = "folder";
    public static String CLASS_CAMPAIGN_TOP_BAR = "campaign-top-bar";
    public static String CLASS_CONTENT_DIALOG = "content-dialog";
    public static String CLASS_EDIT = "edit-button";
    public static String CLASS_COPY = "copy-button";
    public static String CLASS_CASE_SENSITIVE = "case-sensitive-button";
    public static String CLASS_FILTER = "filter-button";
    public static String CLASS_EDITOR_GRID = "editor-grid";
    public static String CLASS_EDITOR_FILTER = "editor-filter-bar";
    public static String CLASS_EDITOR_NAVIGATION = "editor-nav-bar";
    public static String CLASS_KEY = "key-button";
    public static String CLASS_VALUE = "value-button";
    public static String CLASS_CLEAR = "clear-button";

    public static void addStylesheets(Scene scene) {
        if (PdxuInstallation.getInstance() == null) {
            return;
        }
        try (var s = Files.list(PdxuInstallation.getInstance().getResourceDir().resolve("style"))) {
            s.map(p -> p.toUri().toString()).forEach(style -> scene.getStylesheets().add(style));
        } catch (IOException e) {
            ErrorHandler.handleException(e, "Pdx-Unlimiter installation files could not be found at " + PdxuInstallation.getInstance().getResourceDir() + ". Were they deleted somehow?");
        }
    }

    public static void makeEmptyAlert(Scene scene) {
        if (PdxuInstallation.getInstance() == null) {
            return;
        }

        scene.getStylesheets().add(PdxuInstallation.getInstance().getResourceDir().resolve("style")
                .resolve("empty-alert.css").toUri().toString());
    }
}
