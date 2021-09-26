package wintersteve25.oniutils.common.blocks.base.interfaces;

import net.minecraft.item.Item;

import java.util.List;

/**
 * Should be implemented in a {@link wintersteve25.oniutils.common.blocks.base.ONIBaseInvTE}
 */
public interface ONIIHasValidItems {
    List<Item> validItems();
}