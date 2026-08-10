package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class SeedItem extends AccessoryBase {
    public SeedItem() {
        super(0, "seeds", FuncCase.EMPTY);
    }

    public int getBaseEvaluation(){return 2;}

    public int getCost() {return 4;}
}
