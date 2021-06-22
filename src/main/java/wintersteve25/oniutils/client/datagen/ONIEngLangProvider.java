package wintersteve25.oniutils.client.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import wintersteve25.oniutils.ONIUtils;
import wintersteve25.oniutils.common.blocks.libs.ONIBaseDirectional;
import wintersteve25.oniutils.common.blocks.libs.ONIBaseRock;
import wintersteve25.oniutils.common.effects.ONIBaseEffect;
import wintersteve25.oniutils.common.init.ONIBlocks;
import wintersteve25.oniutils.common.init.ONIEffects;
import wintersteve25.oniutils.common.utils.helper.MiscHelper;

public class ONIEngLangProvider extends LanguageProvider {
    public ONIEngLangProvider(DataGenerator gen) {
        super(gen, ONIUtils.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //simple blocks
        add(ONIBlocks.IgneousRock.get(), "Igneous Rock");
        add(ONIBlocks.Slime, "Slime");

        for (ONIBaseRock b : ONIBlocks.rocksList) {
            add("block.oniutils." + MiscHelper.langToReg(b.getRegName()), b.getRegName());
        }
        for (ONIBaseRock b : ONIBlocks.rocksListNoDataGen) {
            add("block.oniutils." + MiscHelper.langToReg(b.getRegName()), b.getRegName());
        }
        for (ONIBaseDirectional b : ONIBlocks.direList) {
            add("block.oniutils." + MiscHelper.langToReg(b.getRegName()), b.getRegName());
        }
        for (ONIBaseDirectional b : ONIBlocks.direListNoDataGen) {
            add("block.oniutils." + MiscHelper.langToReg(b.getRegName()), b.getRegName());
        }
        for (ONIBaseEffect e : ONIEffects.effectList) {
            add("effect.oniutils." + MiscHelper.langToReg(e.getRegName()), e.getRegName());
        }

        //item groups
        add("itemGroup.oniutils", "Veiled Ascent");

        //messages
        add("oniutils.message.germs.infectEntity", "Infected interacted entity with %s %s");
        add("oniutils.message.germs.infectItem", "Infected item(s) with %s %s");
        add("oniutils.message.germs.infectPlayer", "Infected with %s %s");

        add("oniutils.message.trait.gotTrait", "Traits: %s");
        add("oniutils.message.trait.traitInfo", "Check Trait infos here: https://github.com/FictionCraft-Team/ONI-Utils/wiki/Trait-System");

        add("oniutils.message.manualGen", "Use Get off manual generator key(Right-Shift By Default) to dismount..");

        add("death.attack.oniutils.oxygen", "%1$s forgot to breathe");
        add("death.attack.oniutils.germ", "%1$s didn't take the vaccine");

        //tooltips
        add("oniutils.tooltips.germs.itemGerms", "Item contains: %s %s");

        //keybinds
        add("oniutils.keybinds.category", "Veiled Ascent Keybinds");
        add("oniutils.offManualGen", "Get off manual generator");
    }
}
