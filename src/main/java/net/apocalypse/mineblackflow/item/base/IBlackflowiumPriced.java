package net.apocalypse.mineblackflow.item.base;

public interface IBlackflowiumPriced {
    int getCost();

    default boolean canExistInTrade(){
        return getCost() > 0;
    }
}
