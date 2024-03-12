package malilib.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiScreen;

import malilib.event.dispatch.RenderEventDispatcherImpl;
import malilib.mixin.access.GuiScreenMixin;
import malilib.registry.Registry;

@Pseudo
@Mixin(targets = "net.minecraftforge.client.ForgeHooksClient")
public abstract class ForgeHooksClientMixin
{

    @Inject(method = "drawScreen(Lnet/minecraft/client/gui/GuiScreen;IIF)V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V",
        shift = Shift.AFTER))
    private static void onRenderScreenPost(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo)
    {
        if (((GuiScreenMixin) screen).getMC().world != null && ((GuiScreenMixin) screen).getMC().player != null)
        {
            ((RenderEventDispatcherImpl) Registry.RENDER_EVENT_DISPATCHER).onRenderScreenPost(partialTicks);
        }
    }

}