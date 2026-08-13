package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class CageControllerItem extends AccessoryBase {
    public CageControllerItem() {
        super(2, "cage_controller", FuncCase.EMPTY);
    }

    public int getBaseEvaluation(){return 16;}

    public int getCost() {return 0;}
}
