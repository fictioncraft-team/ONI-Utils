package wintersteve25.oniutils.common.events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wintersteve25.oniutils.ONIUtils;
import wintersteve25.oniutils.client.keybinds.ONIKeybinds;
import wintersteve25.oniutils.client.renderers.geckolibs.base.GeckolibBlockRendererBase;
import wintersteve25.oniutils.common.data.capabilities.germ.GermEventsHandler;
import wintersteve25.oniutils.common.contents.modules.blocks.power.coal.CoalGenGui;
import wintersteve25.oniutils.common.contents.modules.blocks.power.coal.CoalGenTE;
import wintersteve25.oniutils.common.registries.ONIBlocks;
import wintersteve25.oniutils.common.utils.ONIConstants;

@Mod.EventBusSubscriber(modid = ONIUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEventsHandler {

    @SubscribeEvent
    public static void clientPreInit(FMLClientSetupEvent event) {
        //Keybindings
        ONIKeybinds.init();

        //Events
        MinecraftForge.EVENT_BUS.addListener(GermEventsHandler::itemToolTipEvent);

        //GUI Attachments
        MenuScreens.register(ONIBlocks.Machines.Power.COAL_GEN_CONTAINER.get(), CoalGenGui::new);

        //Entities
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (ModList.get().isLoaded("geckolib3")) {
            event.registerBlockEntityRenderer(ONIBlocks.Machines.Power.COAL_GEN_TE.get(), t -> new GeckolibBlockRendererBase<CoalGenTE>(t, ONIConstants.Geo.COAL_GEN_TE));
        }
    }

    @SubscribeEvent
    public static void textureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS) {
            event.addSprite(ONIConstants.Resources.CURIOS_GOGGLES);
        }
    }
}
