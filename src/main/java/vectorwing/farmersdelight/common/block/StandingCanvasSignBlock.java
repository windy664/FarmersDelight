package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class StandingCanvasSignBlock extends StandingSignBlock implements CanvasSign
{
	private final @Nullable DyeColor backgroundColor;

	public StandingCanvasSignBlock(ResourceKey<Block> key, @Nullable DyeColor backgroundColor) {
		super(WoodType.SPRUCE, Properties.of().mapColor(MapColor.WOOD).noCollision().strength(1.0F).sound(SoundType.WOOD).setId(key));
		this.backgroundColor = backgroundColor;
	}

	@Nullable
	public DyeColor getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.CANVAS_SIGN.get().create(pos, state);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level.getBlockEntity(pos) instanceof SignBlockEntity sign && state.getBlock() instanceof CanvasSign canvasSignBlock) {
			if (canvasSignBlock.isDarkBackground()) {
				sign.updateText((signText) -> signText.setColor(DyeColor.WHITE), true);
			}
		}
	}
}
