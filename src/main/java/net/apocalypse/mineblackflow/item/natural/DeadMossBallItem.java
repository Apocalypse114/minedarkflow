package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;

public class DeadMossBallItem extends AccessoryBase {
    public DeadMossBallItem() {
        super(0, "dead_moss_ball", FuncCase.EMPTY, true);
    }

    public int getBaseEvaluation(){return 2;}

    public int getCost() {return 0;}
}
