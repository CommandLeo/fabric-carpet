package carpet.mixins;

import carpet.CarpetSettings;
import net.minecraft.block.Block;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Structure.class)
public class Structure_fillUpdatesMixin
{
    @Redirect( method = "place(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;I)Z", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/IWorld;updateNeighbors(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"
    ))
    private void skipUpdateNeighbours(IWorld iWorld, BlockPos var1, Block var2)
    {
        if (!CarpetSettings.impendingFillSkipUpdates)
            iWorld.updateNeighbors(var1, var2);
    }

    @Redirect(method = "place(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;I)Z", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/structure/StructurePlacementData;shouldUpdateNeighbors()Z"
    ))
    private boolean skipPostprocess(StructurePlacementData structurePlacementData)
    {
        return structurePlacementData.shouldUpdateNeighbors() || CarpetSettings.impendingFillSkipUpdates;
    }
}
