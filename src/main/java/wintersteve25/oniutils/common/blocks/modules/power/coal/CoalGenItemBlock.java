package wintersteve25.oniutils.common.blocks.modules.power.coal;

import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wintersteve25.oniutils.client.renderers.geckolibs.machines.power.CoalGenItemBlockRenderer;
import wintersteve25.oniutils.common.items.base.interfaces.ONIIColoredName;
import wintersteve25.oniutils.common.items.base.interfaces.ONIIHasToolTip;
import wintersteve25.oniutils.common.items.base.ONIBaseBlockItem;
import wintersteve25.oniutils.common.init.ONIBlocks;
import wintersteve25.oniutils.common.utils.MiscHelper;

import java.util.ArrayList;
import java.util.List;

public class CoalGenItemBlock extends ONIBaseBlockItem implements ONIIHasToolTip, ONIIColoredName {
    public CoalGenItemBlock() {
        super(ONIBlocks.COAL_GEN_BLOCK, MiscHelper.DEFAULT_ITEM_PROPERTY.setISTER(() -> CoalGenItemBlockRenderer::new));
    }

    @Override
    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        boolean canPlace = false;

        World world = context.getWorld();
        BlockPos ogPos = context.getPos();

        switch (state.get(BlockStateProperties.FACING)) {
            case NORTH:
                if (WorldUtils.isValidReplaceableBlock(world, ogPos.east())) {
                    if (WorldUtils.isValidReplaceableBlock(world, ogPos.up())) {
                        if (WorldUtils.isValidReplaceableBlock(world, ogPos.east().up())) {
                            canPlace = true;
                        }
                    }
                }
                break;
            case SOUTH:
                if (WorldUtils.isValidReplaceableBlock(world, ogPos.west())) {
                    if (WorldUtils.isValidReplaceableBlock(world, ogPos.up())) {
                        if (WorldUtils.isValidReplaceableBlock(world, ogPos.west().up())) {
                            canPlace = true;
                        }
                    }
                }
                break;
            case WEST:
                if (WorldUtils.isValidReplaceableBlock(world, ogPos.north())) {
                    if (WorldUtils.isValidReplaceableBlock(world, ogPos.up())) {
                        if (WorldUtils.isValidReplaceableBlock(world, ogPos.north().up())) {
                            canPlace = true;
                        }
                    }
                }
                break;
            case EAST:
                if (WorldUtils.isValidReplaceableBlock(world, ogPos.south())) {
                    if (WorldUtils.isValidReplaceableBlock(world, ogPos.up())) {
                        if (WorldUtils.isValidReplaceableBlock(world, ogPos.south().up())) {
                            canPlace = true;
                        }
                    }
                }
                break;
        }

        return super.canPlace(context, state) && canPlace;
    }

    @Override
    public List<ITextComponent> tooltip() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TranslationTextComponent("oniutils.tooltips.items.coal_gen"));
        return list;
    }

    @Override
    public TextFormatting color() {
        return MiscHelper.POWER_CAT_COLOR;
    }
}
