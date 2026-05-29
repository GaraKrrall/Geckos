package mc.garakrral.geckos.entity.variant.type;

import java.util.Arrays;
import java.util.Comparator;

public enum GeckoType {
    NORMAL(0),
    BIG(1),
    NETHER(2);

    private static final GeckoType[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(GeckoType::getId)).toArray(GeckoType[]::new);

    private final int id;

    GeckoType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GeckoType byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
