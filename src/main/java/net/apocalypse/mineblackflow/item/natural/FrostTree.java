package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class FrostTree extends AccessoryBase {
    public FrostTree() {
        super(1, "frost_tree", FuncCase.CURIOS);
    }

    public int getBaseEvaluation(){return 6;}
    public int getCost() {return 12;}
}
