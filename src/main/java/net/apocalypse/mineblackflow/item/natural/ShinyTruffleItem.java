package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class ShinyTruffleItem extends AccessoryBase{
    public ShinyTruffleItem() {
        super(2, "shiny_truffle", AccessoryBase.FuncCase.EMPTY);
    }

    public int getBaseEvaluation(){return 32;}

    public int getCost() {return 0;}

}
