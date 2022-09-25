package com.crschnick.pdxu.app.gui.game;

import com.crschnick.pdxu.app.core.CacheManager;
import com.crschnick.pdxu.app.core.ErrorHandler;
import com.crschnick.pdxu.app.info.SavegameInfo;
import com.crschnick.pdxu.app.installation.GameFileContext;
import com.crschnick.pdxu.app.util.CascadeDirectoryHelper;
import com.crschnick.pdxu.io.node.Node;
import com.crschnick.pdxu.io.parser.TextFormatParser;
import com.crschnick.pdxu.model.GameColor;
import com.crschnick.pdxu.model.ck3.Ck3CoatOfArms;
import com.crschnick.pdxu.model.ck3.Ck3House;
import com.crschnick.pdxu.model.ck3.Ck3Tag;
import com.crschnick.pdxu.model.ck3.Ck3Title;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.crschnick.pdxu.app.util.ColorHelper.fromGameColor;

public class Ck3TagCache {

    private static final int REALM_DEFAULT_IMG_SIZE = 64;
    private static final int HOUSE_DEFAULT_IMG_SIZE = 128;
    private static final int TITLE_DEFAULT_IMG_SIZE = 64;

    static Map<String, javafx.scene.paint.Color> getPredefinedColors(GameFileContext ctx) {
        var cache = CacheManager.getInstance().get(CoatOfArmsCache.class);
        var loaded = cache.colorsLoaded;
        if (loaded) {
            return cache.colors;
        }

        var file = CascadeDirectoryHelper.openFile(
                Path.of("common").resolve("named_colors").resolve("default_colors.txt"),
                ctx);
        if (file.isPresent()) {
            try {
                Node node = TextFormatParser.text().parse(file.get());
                node.getNodeForKeyIfExistent("colors").ifPresent(n -> {
                    n.forEach((k, v) -> {
                        try {
                            cache.colors.put(k, fromGameColor(GameColor.fromColorNode(v)));
                        } catch (Exception ignored) {
                        }
                    });
                });
                cache.colorsLoaded = true;
                return cache.colors;
            } catch (Exception ex) {
                ErrorHandler.handleException(ex);
            }
        }
        cache.colorsLoaded = true;
        return Map.of();
    }

    public static Image realmImage(SavegameInfo<Ck3Tag> info, Ck3Tag tag) {
        var cache = CacheManager.getInstance().get(CoatOfArmsCache.class);
        var cachedImg = cache.realms.get(tag);
        if (cachedImg != null) {
            return cachedImg;
        }
        Ck3CoatOfArms coa = tag.getCoatOfArms();
        var img = Ck3TagRenderer.renderRealmImage(coa, tag.getGovernmentName(), GameFileContext.fromData(info.getData()), REALM_DEFAULT_IMG_SIZE, true);
        cache.realms.put(tag, img);
        return img;
    }

    public static Image houseImage(Ck3House house, GameFileContext ctx) {
        var cache = CacheManager.getInstance().get(CoatOfArmsCache.class);
        var cachedImg = cache.houses.get(house);
        if (cachedImg != null) {
            return cachedImg;
        }
        var img = Ck3TagRenderer.renderHouseImage(house.getCoatOfArms(), ctx, HOUSE_DEFAULT_IMG_SIZE, true);
        cache.houses.put(house, img);
        return img;
    }

    public static Image titleImage(Ck3Title title, GameFileContext ctx) {
        var cache = CacheManager.getInstance().get(CoatOfArmsCache.class);
        var cachedImg = cache.titles.get(title);
        if (cachedImg != null) {
            return cachedImg;
        }

        Ck3CoatOfArms coa = title.getCoatOfArms();
        var img = Ck3TagRenderer.renderTitleImage(coa, ctx, TITLE_DEFAULT_IMG_SIZE, true);
        cache.titles.put(title, img);
        return img;
    }

    public static class CoatOfArmsCache extends CacheManager.Cache {

        private boolean colorsLoaded;
        private final Map<String, javafx.scene.paint.Color> colors = new ConcurrentHashMap<>();
        private final Map<Ck3Tag, Image> realms = new ConcurrentHashMap<>();
        private final Map<Ck3Title, Image> titles = new ConcurrentHashMap<>();
        private final Map<Ck3House, Image> houses = new ConcurrentHashMap<>();

        public CoatOfArmsCache() {
            super(CacheManager.Scope.SAVEGAME_CAMPAIGN_SPECIFIC);
        }
    }
}
