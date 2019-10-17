package com.mulight.demo.photobooth.ui.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing content
 * <p>
 */
public class PhotoContent {

    public static final List<PhotoItem> ITEMS = new ArrayList<PhotoItem>();

    public static void initData(ArrayList<PhotoItem> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        ITEMS.clear();

        int count = items.size();
        for (int i = 0; i < count; i++) {
            addItem(items.get(i));
        }
    }

    private static void addItem(PhotoItem item) {
        ITEMS.add(item);
    }

    public static class PhotoItem {
        public final int id;
        public final String name;
        public final String path;
        public final String createTime;

        public PhotoItem(int id, String name, String path, String createTime) {
            this.id = id;
            this.name = name;
            this.path = path;
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return path;
        }
    }
}
