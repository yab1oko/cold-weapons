package yblk.cold_weapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import yblk.cold_weapons.entity.TaserDartEntity;
import yblk.cold_weapons.item.TaserItem;

/** Electrode plus the two wires back to the taser. */
public class TaserDartRenderer extends EntityRenderer<TaserDartEntity> {
    public TaserDartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TaserDartEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        VertexConsumer lines = buffer.getBuffer(RenderType.lines());
        Vec3 dartPos = entity.getPosition(partialTick);

        Vec3 motion = entity.getDeltaMovement();
        Vec3 back = new Vec3(0.0D, 0.0D, 0.12D);
        if (!entity.isStuck() && motion.lengthSqr() > 1.0E-4D) {
            back = motion.normalize().scale(-0.62D);
        }
        line(lines, pose, matrix, Vec3.ZERO, back, 245, 228, 230, 236);
        cross(lines, pose, matrix, 0.075F, 250, 236, 238, 242);

        Entity owner = entity.getOwner();
        if (owner instanceof LivingEntity living && keepWire(entity, living)) {
            Vec3 from = muzzle(living, entity.isMainHand(), partialTick).subtract(dartPos);
            float age = entity.tickCount + partialTick;
            Minecraft minecraft = Minecraft.getInstance();
            drawSpiral(minecraft, lines, pose, matrix, from, Vec3.ZERO, dartPos, age, entity.isStuck(), 1.0D, 198, 200, 206);
            drawSpiral(minecraft, lines, pose, matrix, from, Vec3.ZERO, dartPos, age, entity.isStuck(), -1.0D, 186, 188, 194);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static boolean keepWire(TaserDartEntity dart, LivingEntity shooter) {
        if (!dart.isStuckToEntity()) {
            return dart.tickCount <= 30;
        }
        Entity stuck = dart.level().getEntity(dart.getStuckId());
        if (!(stuck instanceof LivingEntity living) || !living.isAlive()) {
            return false;
        }
        return shooter.distanceTo(living) <= TaserItem.RANGE + 1.5D;
    }

    private static Vec3 muzzle(LivingEntity entity, boolean mainHand, float partial) {
        Vec3 look = entity.getViewVector(partial);
        Vec3 side = sideOf(Vec3.ZERO, look);
        if (side.lengthSqr() < 1.0E-4D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        }
        int sign = entity.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!mainHand) {
            sign = -sign;
        }
        return entity.getEyePosition(partial)
                .add(side.scale(0.32D * sign))
                .add(0.0D, -0.28D, 0.0D)
                .add(look.scale(0.55D));
    }

    private static void drawSpiral(Minecraft minecraft, VertexConsumer buffer, PoseStack.Pose pose, Matrix4f matrix,
                                   Vec3 from, Vec3 to, Vec3 worldOrigin, float age, boolean stuck, double sideSign,
                                   int r, int g, int b) {
        double length = Math.max(0.2D, from.distanceTo(to));
        float settle = stuck ? 1.0F : Mth.clamp((age - 2.0F) / 5.0F, 0.0F, 0.35F);
        int segments = Mth.clamp((int) (length * 14.0D), 10, 48);
        double twistTurns = stuck ? 0.0D : 1.15D;
        double radius = stuck ? 0.0D : 0.07D;
        double sag = Mth.lerp(settle, 0.02D, Math.min(1.15D, 0.26D + length * 0.18D));
        Vec3 axis = to.subtract(from);
        Vec3 side = sideOf(from, to);
        Vec3 up = axis.cross(side);
        if (up.lengthSqr() < 1.0E-6D) {
            up = new Vec3(0.0D, 1.0D, 0.0D);
        } else {
            up = up.normalize();
        }
        double sway = Math.sin(age * 0.18D) * 0.04D * settle * sideSign;
        Vec3 prev = null;
        for (int i = 0; i <= segments; i++) {
            double t = i / (double) segments;
            Vec3 point = from.lerp(to, t);
            double droop = Math.sin(Math.PI * t) * sag;
            double hang = Math.sin(Math.PI * t) * (0.1D * settle * sideSign + sway);
            point = new Vec3(point.x, point.y - droop, point.z).add(side.scale(hang));
            point = aboveFloor(minecraft, point.add(worldOrigin)).subtract(worldOrigin);
            if (radius > 0.001D) {
                double angle = t * twistTurns * (Math.PI * 2.0D) + (sideSign > 0.0D ? 0.0D : Math.PI);
                point = point.add(side.scale(Math.cos(angle) * radius)).add(up.scale(Math.sin(angle) * radius));
            }
            if (prev != null) {
                line(buffer, pose, matrix, prev, point, 230, r, g, b);
            }
            prev = point;
        }
    }

    private static Vec3 aboveFloor(Minecraft minecraft, Vec3 point) {
        if (minecraft.level == null || minecraft.player == null) {
            return point;
        }
        BlockHitResult ground = minecraft.level.clip(new ClipContext(
                point.add(0.0D, 0.25D, 0.0D),
                point.add(0.0D, -3.0D, 0.0D),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                minecraft.player));
        if (ground.getType() == HitResult.Type.MISS) {
            return point;
        }
        double floor = ground.getLocation().y + 0.04D;
        return point.y < floor ? new Vec3(point.x, floor, point.z) : point;
    }

    private static void cross(VertexConsumer buffer, PoseStack.Pose pose, Matrix4f matrix,
                              float size, int alpha, int r, int g, int b) {
        line(buffer, pose, matrix, new Vec3(-size, 0.0D, 0.0D), new Vec3(size, 0.0D, 0.0D), alpha, r, g, b);
        line(buffer, pose, matrix, new Vec3(0.0D, -size, 0.0D), new Vec3(0.0D, size, 0.0D), alpha, r, g, b);
        line(buffer, pose, matrix, new Vec3(0.0D, 0.0D, -size), new Vec3(0.0D, 0.0D, size), alpha, r, g, b);
    }

    private static Vec3 sideOf(Vec3 from, Vec3 to) {
        Vec3 dir = to.subtract(from);
        Vec3 side = dir.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (side.lengthSqr() < 1.0E-4D) {
            side = dir.cross(new Vec3(1.0D, 0.0D, 0.0D));
        }
        return side.normalize();
    }

    private static void line(VertexConsumer buffer, PoseStack.Pose pose, Matrix4f matrix,
                             Vec3 a, Vec3 b, int alpha, int r, int g, int bl) {
        float dx = (float) (b.x - a.x);
        float dy = (float) (b.y - a.y);
        float dz = (float) (b.z - a.z);
        float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1.0E-4F) {
            return;
        }
        float nx = dx / len;
        float ny = dy / len;
        float nz = dz / len;
        buffer.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(r, g, bl, alpha).normal(pose.normal(), nx, ny, nz).endVertex();
        buffer.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(r, g, bl, alpha).normal(pose.normal(), nx, ny, nz).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(TaserDartEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
