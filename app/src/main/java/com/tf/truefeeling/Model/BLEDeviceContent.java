package com.tf.truefeeling.Model;

import com.tf.truefeeling.Model.LeDeviceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BLEDeviceContent {
    /**
     * An array of sample (dummy) items.
     */
    //public static final List<BLEDeviceItem> ITEMS = new ArrayList<BLEDeviceItem>();
    public static final LeDeviceList listItems= LeDeviceList.getInstance();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, BLEDeviceItem> ITEM_MAP = new HashMap<String, BLEDeviceItem>();

    private static final int COUNT = 25;

    /*static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

    /*private static void addItem(BLEDeviceItem item) {
        //ITEMS.add(item);
        listItems.add(item);
        ITEM_MAP.put(item.addr, item);
    }*/

    /*private static BLEDeviceItem createDummyItem(int position) {
        return new BLEDeviceItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    public static class BLEDeviceItem {
        public final String addr;
        public final String name;
        //public final String details;

        public BLEDeviceItem(String name, String addr/*, String details*/) {
            this.name = name;
            this.addr = addr;
            //this.details = details;
        }

        @Override
        public String toString() {
            return addr;
        }
    }
}
