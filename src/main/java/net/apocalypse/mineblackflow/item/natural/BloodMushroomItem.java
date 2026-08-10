package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class BloodMushroomItem extends AccessoryBase {
    public BloodMushroomItem() {
        super(0, "blood_mushroom", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 2;}
    public int getCost() {return 4;}
}
