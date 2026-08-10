package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class MistyTumbleweedItem extends AccessoryBase {
    public MistyTumbleweedItem() {
        super(0, "misty_tumbleweed", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 2;}
    public int getCost() {return 4;}
}
