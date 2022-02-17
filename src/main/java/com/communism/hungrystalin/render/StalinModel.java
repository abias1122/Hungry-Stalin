package com.communism.hungrystalin.render;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.entity.Stalin;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class StalinModel<Type extends Stalin> extends PlayerModel<Type> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "stalin"), "main");

    public StalinModel(ModelPart modelPart) {
        super(modelPart, false);
    }
}
