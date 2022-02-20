package com.communism.hungrystalin.render;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.entity.Stalin;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StalinRenderer<Type extends Stalin> extends HumanoidMobRenderer<Type, StalinModel<Type>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Main.MOD_ID, "textures/entities/stalin.png");

    public StalinRenderer(EntityRendererProvider.Context context) {
        super(context, new StalinModel<>(context.bakeLayer(StalinModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Stalin entity) {
        return TEXTURE;
    }
}
