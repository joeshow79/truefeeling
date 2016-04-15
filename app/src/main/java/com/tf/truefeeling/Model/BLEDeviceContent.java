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

    /**
     * A dummy item representing a piece of content.
     */
    public static class BLEDeviceItem {
        public final String addr;
        public final String name;

        public BLEDeviceItem(String name, String addr/*, String details*/) {
            this.name = name;
            this.addr = addr;
        }

        @Override
        public String toString() {
            return addr;
        }
    }
}
