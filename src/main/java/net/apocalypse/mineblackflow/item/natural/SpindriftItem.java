package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class SpindriftItem extends AccessoryBase {
    public SpindriftItem() {
        super(0, "spindrift", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 3;}

    public int getCost() {return 6;}
}
