package mekanism.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.Nonnull;
import mekanism.client.render.MekanismRenderType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelQuantumEntangloporter extends Model {

    private static final ResourceLocation ENTANGLOPORTER_TEXTURE = MekanismUtils.getResource(ResourceType.RENDER, "quantum_entangloporter.png");
    private static final ResourceLocation OVERLAY = MekanismUtils.getResource(ResourceType.RENDER, "quantum_entangloporter_overlay.png");
    private static final RenderType RENDER_TYPE_OVERLAY = MekanismRenderType.mekStandard(OVERLAY);
    private final RenderType RENDER_TYPE = getRenderType(ENTANGLOPORTER_TEXTURE);

    private final ModelRenderer portTop;
    private final ModelRenderer portBottom;
    private final ModelRenderer portLeft;
    private final ModelRenderer portRight;
    private final ModelRenderer portBack;
    private final ModelRenderer portFront;
    private final ModelRenderer energyCubeCore;
    private final ModelRenderer frameEdge1;
    private final ModelRenderer frameEdge2;
    private final ModelRenderer frameEdge3;
    private final ModelRenderer frameEdge4;
    private final ModelRenderer frameEdge5;
    private final ModelRenderer frameEdge6;
    private final ModelRenderer frameEdge7;
    private final ModelRenderer frameEdge8;
    private final ModelRenderer frameEdge9;
    private final ModelRenderer frameEdge10;
    private final ModelRenderer frameEdge11;
    private final ModelRenderer frameEdge12;
    private final ModelRenderer frame1;
    private final ModelRenderer frame2;
    private final ModelRenderer frame3;
    private final ModelRenderer frame4;
    private final ModelRenderer frame5;
    private final ModelRenderer frame6;
    private final ModelRenderer frame7;
    private final ModelRenderer frame8;
    private final ModelRenderer frame9;
    private final ModelRenderer frame10;
    private final ModelRenderer frame11;
    private final ModelRenderer frame12;
    private final ModelRenderer corner1;
    private final ModelRenderer corner2;
    private final ModelRenderer corner3;
    private final ModelRenderer corner4;
    private final ModelRenderer corner5;
    private final ModelRenderer corner6;
    private final ModelRenderer corner7;
    private final ModelRenderer corner8;

    public ModelQuantumEntangloporter() {
        super(RenderType::entitySolid);
        textureWidth = 128;
        textureHeight = 64;

        portTop = new ModelRenderer(this, 36, 0);
        portTop.addBox(0F, 0F, 0F, 8, 1, 8, false);
        portTop.setRotationPoint(-4F, 8F, -4F);
        portTop.setTextureSize(128, 64);
        portTop.mirror = true;
        setRotation(portTop, 0F, 0F, 0F);
        portBottom = new ModelRenderer(this, 36, 9);
        portBottom.addBox(0F, 0F, 0F, 8, 1, 8, false);
        portBottom.setRotationPoint(-4F, 23F, -4F);
        portBottom.setTextureSize(128, 64);
        portBottom.mirror = true;
        setRotation(portBottom, 0F, 0F, 0F);
        portLeft = new ModelRenderer(this, 0, 0);
        portLeft.addBox(0F, 0F, 0F, 1, 8, 8, false);
        portLeft.setRotationPoint(-8F, 12F, -4F);
        portLeft.setTextureSize(128, 64);
        portLeft.mirror = true;
        setRotation(portLeft, 0F, 0F, 0F);
        portRight = new ModelRenderer(this, 0, 0);
        portRight.mirror = true;
        portRight.addBox(0F, 0F, 0F, 1, 8, 8, true);
        portRight.setRotationPoint(7F, 12F, -4F);
        portRight.setTextureSize(128, 64);
        setRotation(portRight, 0F, 0F, 0F);
        portBack = new ModelRenderer(this, 18, 9);
        portBack.addBox(0F, 0F, 0F, 8, 8, 1, false);
        portBack.setRotationPoint(-4F, 12F, 7F);
        portBack.setTextureSize(128, 64);
        portBack.mirror = true;
        setRotation(portBack, 0F, 0F, 0F);
        portFront = new ModelRenderer(this, 18, 0);
        portFront.addBox(0F, 0F, 0F, 8, 8, 1, false);
        portFront.setRotationPoint(-4F, 12F, -8F);
        portFront.setTextureSize(128, 64);
        portFront.mirror = true;
        setRotation(portFront, 0F, 0F, 0F);
        energyCubeCore = new ModelRenderer(this, 0, 41);
        energyCubeCore.addBox(-2F, -2F, -2F, 4, 4, 4, false);
        energyCubeCore.setRotationPoint(0F, 16F, 0F);
        energyCubeCore.setTextureSize(128, 64);
        energyCubeCore.mirror = true;
        setRotation(energyCubeCore, 0.7132579F, 0.403365F, 0.645384F);
        frameEdge1 = new ModelRenderer(this, 0, 16);
        frameEdge1.addBox(0F, 0F, 0F, 1, 10, 1, false);
        frameEdge1.setRotationPoint(-7.5F, 11F, -7.5F);
        frameEdge1.setTextureSize(128, 64);
        frameEdge1.mirror = true;
        setRotation(frameEdge1, 0F, 0F, 0F);
        frameEdge2 = new ModelRenderer(this, 0, 16);
        frameEdge2.addBox(0F, 0F, 0F, 1, 10, 1, false);
        frameEdge2.setRotationPoint(6.5F, 11F, -7.5F);
        frameEdge2.setTextureSize(128, 64);
        frameEdge2.mirror = true;
        setRotation(frameEdge2, 0F, 0F, 0F);
        frameEdge3 = new ModelRenderer(this, 0, 16);
        frameEdge3.addBox(0F, 0F, 0F, 1, 10, 1, false);
        frameEdge3.setRotationPoint(-7.5F, 11F, 6.5F);
        frameEdge3.setTextureSize(128, 64);
        frameEdge3.mirror = true;
        setRotation(frameEdge3, 0F, 0F, 0F);
        frameEdge4 = new ModelRenderer(this, 0, 16);
        frameEdge4.addBox(0F, 0F, 0F, 1, 10, 1, false);
        frameEdge4.setRotationPoint(6.5F, 11F, 6.5F);
        frameEdge4.setTextureSize(128, 64);
        frameEdge4.mirror = true;
        setRotation(frameEdge4, 0F, 0F, 0F);
        frameEdge5 = new ModelRenderer(this, 4, 27);
        frameEdge5.addBox(0F, 0F, 0F, 10, 1, 1, false);
        frameEdge5.setRotationPoint(-5F, 22.5F, -7.5F);
        frameEdge5.setTextureSize(128, 64);
        frameEdge5.mirror = true;
        setRotation(frameEdge5, 0F, 0F, 0F);
        frameEdge6 = new ModelRenderer(this, 4, 16);
        frameEdge6.addBox(0F, 0F, 0F, 1, 1, 10, false);
        frameEdge6.setRotationPoint(-7.5F, 22.5F, -5F);
        frameEdge6.setTextureSize(128, 64);
        frameEdge6.mirror = true;
        setRotation(frameEdge6, 0F, 0F, 0F);
        frameEdge7 = new ModelRenderer(this, 4, 16);
        frameEdge7.addBox(0F, 0F, 0F, 1, 1, 10, false);
        frameEdge7.setRotationPoint(6.5F, 22.5F, -5F);
        frameEdge7.setTextureSize(128, 64);
        frameEdge7.mirror = true;
        setRotation(frameEdge7, 0F, 0F, 0F);
        frameEdge8 = new ModelRenderer(this, 4, 27);
        frameEdge8.addBox(0F, 0F, 0F, 10, 1, 1, false);
        frameEdge8.setRotationPoint(-5F, 22.5F, 6.5F);
        frameEdge8.setTextureSize(128, 64);
        frameEdge8.mirror = true;
        setRotation(frameEdge8, 0F, 0F, 0F);
        frameEdge9 = new ModelRenderer(this, 4, 27);
        frameEdge9.addBox(0F, 0F, 0F, 10, 1, 1, false);
        frameEdge9.setRotationPoint(-5F, 8.5F, -7.5F);
        frameEdge9.setTextureSize(128, 64);
        frameEdge9.mirror = true;
        setRotation(frameEdge9, 0F, 0F, 0F);
        frameEdge10 = new ModelRenderer(this, 4, 16);
        frameEdge10.addBox(0F, 0F, 0F, 1, 1, 10, false);
        frameEdge10.setRotationPoint(-7.5F, 8.5F, -5F);
        frameEdge10.setTextureSize(128, 64);
        frameEdge10.mirror = true;
        setRotation(frameEdge10, 0F, 0F, 0F);
        frameEdge11 = new ModelRenderer(this, 4, 16);
        frameEdge11.addBox(0F, 0F, 0F, 1, 1, 10, false);
        frameEdge11.setRotationPoint(6.5F, 8.5F, -5F);
        frameEdge11.setTextureSize(128, 64);
        frameEdge11.mirror = true;
        setRotation(frameEdge11, 0F, 0F, 0F);
        frameEdge12 = new ModelRenderer(this, 4, 27);
        frameEdge12.addBox(0F, 0F, 0F, 10, 1, 1, false);
        frameEdge12.setRotationPoint(-5F, 8.5F, 6.5F);
        frameEdge12.setTextureSize(128, 64);
        frameEdge12.mirror = true;
        setRotation(frameEdge12, 0F, 0F, 0F);
        frame1 = new ModelRenderer(this, 0, 29);
        frame1.addBox(0F, 0F, 0F, 2, 10, 2, false);
        frame1.setRotationPoint(-7F, 11F, -7F);
        frame1.setTextureSize(128, 64);
        frame1.mirror = true;
        setRotation(frame1, 0F, 0F, 0F);
        frame2 = new ModelRenderer(this, 0, 29);
        frame2.mirror = true;
        frame2.addBox(0F, 0F, 0F, 2, 10, 2, false);
        frame2.setRotationPoint(5F, 11F, -7F);
        frame2.setTextureSize(128, 64);
        setRotation(frame2, 0F, 0F, 0F);
        frame3 = new ModelRenderer(this, 8, 29);
        frame3.addBox(0F, 0F, 0F, 2, 10, 2, false);
        frame3.setRotationPoint(-7F, 11F, 5F);
        frame3.setTextureSize(128, 64);
        frame3.mirror = true;
        setRotation(frame3, 0F, 0F, 0F);
        frame4 = new ModelRenderer(this, 8, 29);
        frame4.mirror = true;
        frame4.addBox(0F, 0F, 0F, 2, 10, 2, false);
        frame4.setRotationPoint(5F, 11F, 5F);
        frame4.setTextureSize(128, 64);
        setRotation(frame4, 0F, 0F, 0F);
        frame5 = new ModelRenderer(this, 16, 45);
        frame5.addBox(0F, 0F, 0F, 10, 2, 2, false);
        frame5.setRotationPoint(-5F, 21F, -7F);
        frame5.setTextureSize(128, 64);
        frame5.mirror = true;
        setRotation(frame5, 0F, 0F, 0F);
        frame6 = new ModelRenderer(this, 40, 29);
        frame6.addBox(0F, 0F, 0F, 2, 2, 10, false);
        frame6.setRotationPoint(-7F, 21F, -5F);
        frame6.setTextureSize(128, 64);
        frame6.mirror = true;
        setRotation(frame6, 0F, 0F, 0F);
        frame7 = new ModelRenderer(this, 40, 29);
        frame7.mirror = true;
        frame7.addBox(0F, 0F, 0F, 2, 2, 10, false);
        frame7.setRotationPoint(5F, 21F, -5F);
        frame7.setTextureSize(128, 64);
        setRotation(frame7, 0F, 0F, 0F);
        frame8 = new ModelRenderer(this, 16, 49);
        frame8.addBox(0F, 0F, 0F, 10, 2, 2, false);
        frame8.setRotationPoint(-5F, 21F, 5F);
        frame8.setTextureSize(128, 64);
        frame8.mirror = true;
        setRotation(frame8, 0F, 0F, 0F);
        frame9 = new ModelRenderer(this, 16, 41);
        frame9.addBox(0F, 0F, 0F, 10, 2, 2, false);
        frame9.setRotationPoint(-5F, 9F, -7F);
        frame9.setTextureSize(128, 64);
        frame9.mirror = true;
        setRotation(frame9, 0F, 0F, 0F);
        frame10 = new ModelRenderer(this, 16, 29);
        frame10.addBox(0F, 0F, 0F, 2, 2, 10, false);
        frame10.setRotationPoint(-7F, 9F, -5F);
        frame10.setTextureSize(128, 64);
        frame10.mirror = true;
        setRotation(frame10, 0F, 0F, 0F);
        frame11 = new ModelRenderer(this, 16, 29);
        frame11.mirror = true;
        frame11.addBox(0F, 0F, 0F, 2, 2, 10, false);
        frame11.setRotationPoint(5F, 9F, -5F);
        frame11.setTextureSize(128, 64);
        setRotation(frame11, 0F, 0F, 0F);
        frame12 = new ModelRenderer(this, 16, 53);
        frame12.addBox(0F, 0F, 0F, 10, 2, 2, false);
        frame12.setRotationPoint(-5F, 9F, 5F);
        frame12.setTextureSize(128, 64);
        frame12.mirror = true;
        setRotation(frame12, 0F, 0F, 0F);
        corner1 = new ModelRenderer(this, 0, 49);
        corner1.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner1.setRotationPoint(-8F, 8F, -8F);
        corner1.setTextureSize(128, 64);
        corner1.mirror = true;
        setRotation(corner1, 0F, 0F, 0F);
        corner2 = new ModelRenderer(this, 0, 49);
        corner2.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner2.setRotationPoint(5F, 8F, -8F);
        corner2.setTextureSize(128, 64);
        corner2.mirror = true;
        setRotation(corner2, 0F, 0F, 0F);
        corner3 = new ModelRenderer(this, 0, 49);
        corner3.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner3.setRotationPoint(-8F, 8F, 5F);
        corner3.setTextureSize(128, 64);
        corner3.mirror = true;
        setRotation(corner3, 0F, 0F, 0F);
        corner4 = new ModelRenderer(this, 0, 49);
        corner4.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner4.setRotationPoint(5F, 8F, 5F);
        corner4.setTextureSize(128, 64);
        corner4.mirror = true;
        setRotation(corner4, 0F, 0F, 0F);
        corner5 = new ModelRenderer(this, 0, 49);
        corner5.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner5.setRotationPoint(-8F, 21F, -8F);
        corner5.setTextureSize(128, 64);
        corner5.mirror = true;
        setRotation(corner5, 0F, 0F, 0F);
        corner6 = new ModelRenderer(this, 0, 49);
        corner6.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner6.setRotationPoint(5F, 21F, -8F);
        corner6.setTextureSize(128, 64);
        corner6.mirror = true;
        setRotation(corner6, 0F, 0F, 0F);
        corner7 = new ModelRenderer(this, 0, 49);
        corner7.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner7.setRotationPoint(-8F, 21F, 5F);
        corner7.setTextureSize(128, 64);
        corner7.mirror = true;
        setRotation(corner7, 0F, 0F, 0F);
        corner8 = new ModelRenderer(this, 0, 49);
        corner8.addBox(0F, 0F, 0F, 3, 3, 3, false);
        corner8.setRotationPoint(5F, 21F, 5F);
        corner8.setTextureSize(128, 64);
        corner8.mirror = true;
        setRotation(corner8, 0F, 0F, 0F);
    }

    public void render(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, boolean renderMain) {
        if (renderMain) {
            render(matrix, renderer.getBuffer(RENDER_TYPE), light, overlayLight, 1, 1, 1, 1);
        }
        matrix.push();
        matrix.scale(1.001F, 1.001F, 1.001F);
        matrix.translate(0, -0.0011, 0);
        render(matrix, renderer.getBuffer(RENDER_TYPE_OVERLAY), MekanismRenderer.FULL_LIGHT, overlayLight, 1, 1, 1, 1);
        matrix.pop();
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, @Nonnull IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue,
          float alpha) {
        portTop.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portBottom.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portLeft.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portRight.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portBack.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        portFront.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        energyCubeCore.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge1.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge2.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge3.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge4.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge5.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge6.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge7.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge8.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge9.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge10.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge11.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frameEdge12.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame1.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame2.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame3.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame4.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame5.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame6.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame7.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame8.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame9.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame10.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame11.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        frame12.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner1.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner2.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner3.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner4.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner5.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner6.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner7.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
        corner8.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}