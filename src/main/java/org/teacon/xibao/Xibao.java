package org.teacon.xibao;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Mod("xibao")
public class Xibao {

    public Xibao() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = "xibao", value = Dist.CLIENT)
    public static final class XibaoImpl {
        private static final ResourceLocation LOCATION = new ResourceLocation("xibao", "textures/xibao.png");
        @SubscribeEvent
        public static void on(InitGuiEvent.Post event) {
            boolean showXibao = !Files.exists(FMLPaths.GAMEDIR.get().resolve(".xibao_stop"));
            if (showXibao) {
                var screen = event.getGui();
                if (event.getGui() instanceof DisconnectedScreen)
                {
                    var disableXibao = new Button(screen.width / 2 - 75, screen.height - 30, 150, 20, new TranslationTextComponent("xibao.do_not_show_again"), btn -> {
                        var gameDir = FMLPaths.GAMEDIR.get();
                        try {
                            Files.writeString(gameDir.resolve(".xibao_stop"), "Remove this file to show Xibao again", StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            return;
                        }
                        btn.active = false;
                    });
                    event.addWidget(disableXibao);
                }
            }
        }

        @SubscribeEvent
        public static void on(GuiScreenEvent.BackgroundDrawnEvent event) {
            boolean showXibao = !Files.exists(FMLPaths.GAMEDIR.get().resolve(".xibao_stop"));
            if(showXibao)
            {
                var screen = event.getGui();
                if(screen instanceof DisconnectedScreen)
                {
                    Minecraft.getInstance().getTextureManager().bind(LOCATION);
                    Tessellator tesselator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tesselator.getBuilder();
                    //RenderSystem.setShaderTexture(0, LOCATION);
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    bufferbuilder.vertex(0.0D, screen.height, 0.0D).uv(0F, 1F).color(255, 255, 255, 255).endVertex();
                    bufferbuilder.vertex(screen.width, screen.height, 0.0D).uv(1F, 1F).color(255, 255, 255, 255).endVertex();
                    bufferbuilder.vertex(screen.width, 0.0D, 0.0D).uv(1F, 0F).color(255, 255, 255, 255).endVertex();
                    bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0F, 0F).color(255, 255, 255, 255).endVertex();
                    tesselator.end();
                }
            }
        }
    }
}
