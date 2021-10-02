package wintersteve25.oniutils.common.network;

import net.minecraftforge.fml.network.NetworkEvent;
import wintersteve25.oniutils.client.gui.ONIBaseGuiTabModification;

import java.util.function.Supplier;

public class RenderErrorPacket {
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ONIBaseGuiTabModification::addError);
        ctx.get().setPacketHandled(true);
    }
}