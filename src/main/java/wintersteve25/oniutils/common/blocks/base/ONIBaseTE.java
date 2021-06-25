package wintersteve25.oniutils.common.blocks.base;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public abstract class ONIBaseTE extends TileEntity {

    protected int progress = 0;
    protected boolean isWorking = false;

    public ONIBaseTE(TileEntityType<?> te) {
        super(te);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    protected abstract int progress();

    public boolean getWorking() {
        return isWorking;
    }

    public void setWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }
}