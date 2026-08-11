package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class EchoCorn extends AccessoryBase{
    public EchoCorn() {
        super(1, "echo_corn", AccessoryBase.FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 4;}
    public int getCost() {return 8;}
}
