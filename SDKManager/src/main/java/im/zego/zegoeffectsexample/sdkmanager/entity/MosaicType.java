package im.zego.zegoeffectsexample.sdkmanager.entity;

import im.zego.effects.enums.ZegoEffectsMosaicType;

public enum MosaicType {
    SQUARE(0),
    TRIANGLE(1),
    HEXAGON(2);

    private int value;

    private MosaicType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MosaicType getMosaicType(int value) {
        try {
            if (TRIANGLE.value == value) {
                return TRIANGLE;
            } else if (SQUARE.value == value) {
                return SQUARE;
            } else {
                return HEXAGON.value == value ? HEXAGON : null;
            }
        } catch (Exception var2) {
            throw new RuntimeException("The enumeration cannot be found");
        }
    }

}
