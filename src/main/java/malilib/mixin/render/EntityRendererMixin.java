package malilib.mixin.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

import malilib.event.dispatch.RenderEventDispatcherImpl;
import malilib.registry.Registry;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
    @Shadow @Final private Minecraft mc;

    @Inject(method = "renderWorldPass(IFJ)V", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z"
        ))
    private void onRenderWorldLast(int pass, float tickDelta, long finishTimeNano, CallbackInfo ci)
    {
        if (this.mc.world != null && this.mc.player != null)
        {
            ((RenderEventDispatcherImpl) Registry.RENDER_EVENT_DISPATCHER).onRenderWorldLast(tickDelta);
        }
    }

    @Inject(method = "updateCameraAndRender(FJ)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V",
            shift = Shift.AFTER))
    private void onRenderGameOverlayPost(float tickDelta, long nanoTime, CallbackInfo ci)
    {
        if (this.mc.world != null && this.mc.player != null)
        {
            ((RenderEventDispatcherImpl) Registry.RENDER_EVENT_DISPATCHER).onRenderGameOverlayPost();
        }
    }

    @Inject(method = "updateCameraAndRender(FJ)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V",
            shift = Shift.AFTER),
            require = 0, expect = 0)
    private void onRenderScreenPost(float tickDelta, long nanoTime, CallbackInfo ci)
    {
        if (this.mc.world != null && this.mc.player != null)
        {
            ((RenderEventDispatcherImpl) Registry.RENDER_EVENT_DISPATCHER).onRenderScreenPost(tickDelta);
        }
    }
}
