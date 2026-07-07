package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.BlockShapes;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FarmersDelight.MODID);

	private static ResourceKey<Block> key(String path) {
		return ResourceKey.create(Registries.BLOCK, FarmersDelight.id(path));
	}

	private static final ThreadLocal<String> CURRENT_BLOCK_ID = new ThreadLocal<>();

	private static <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
		return BLOCKS.register(id, () -> {
			CURRENT_BLOCK_ID.set(id);
			try {
				return block.get();
			} finally {
				CURRENT_BLOCK_ID.remove();
			}
		});
	}

	private static BlockBehaviour.Properties props() {
		return BlockBehaviour.Properties.of().setId(key(CURRENT_BLOCK_ID.get()));
	}

	private static BlockBehaviour.Properties props(Block source) {
		return BlockBehaviour.Properties.ofFullCopy(source).setId(key(CURRENT_BLOCK_ID.get()));
	}

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	private static ToIntFunction<BlockState> glowingFeastBlockEmission() {
		return (state) -> state.getValue(FeastBlock.SERVINGS) * 3;
	}

	// Workstations
	public static final Supplier<Block> STOVE = registerBlock("stove",
		() -> new StoveBlock(props(Blocks.BRICKS).lightLevel(litBlockEmission(13))));
	public static final Supplier<Block> COOKING_POT = registerBlock("cooking_pot",
		() -> new CookingPotBlock(props().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
	public static final Supplier<Block> SKILLET = registerBlock("skillet",
		() -> new SkilletBlock(props().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
	public static final Supplier<Block> WOODEN_BASKET = registerBlock("wooden_basket",
		() -> new BasketBlock(props().strength(1.5F).sound(SoundType.WOOD)));
	public static final Supplier<Block> BAMBOO_BASKET = registerBlock("bamboo_basket",
		() -> new BasketBlock(props().strength(1.5F).sound(SoundType.BAMBOO_WOOD)));
	public static final Supplier<Block> CUTTING_BOARD = registerBlock("cutting_board",
		() -> new CuttingBoardBlock(props(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = registerBlock("carrot_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> POTATO_CRATE = registerBlock("potato_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> BEETROOT_CRATE = registerBlock("beetroot_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> CABBAGE_CRATE = registerBlock("cabbage_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> TOMATO_CRATE = registerBlock("tomato_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> ONION_CRATE = registerBlock("onion_crate",
		() -> new Block(props(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> RICE_BALE = registerBlock("rice_bale",
		() -> new RiceBaleBlock(props(Blocks.HAY_BLOCK)));
	public static final Supplier<Block> RICE_BAG = registerBlock("rice_bag",
		() -> new Block(props(Blocks.WOOL.pick(DyeColor.WHITE))));
	public static final Supplier<Block> STRAW_BALE = registerBlock("straw_bale",
		() -> new StrawBaleBlock(props(Blocks.HAY_BLOCK)));

	// Building
	public static final Supplier<Block> ROPE = registerBlock("rope",
		() -> new RopeBlock(props(Blocks.CARPET.pick(DyeColor.BROWN)).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL)));
	public static final Supplier<Block> SAFETY_NET = registerBlock("safety_net",
		() -> new SafetyNetBlock(props(Blocks.CARPET.pick(DyeColor.BROWN)).strength(0.2F).sound(SoundType.WOOL)));
	public static final Supplier<Block> ROPE_FENCE = registerBlock("rope_fence",
		() -> new RopeFenceBlock(props(Blocks.OAK_FENCE).strength(1.0F)));
	public static final Supplier<Block> ROPE_FENCE_GATE = registerBlock("rope_fence_gate",
		() -> new RopeFenceGateBlock(props(Blocks.OAK_FENCE).strength(1.0F)));
	public static final Supplier<Block> OAK_CABINET = registerBlock("oak_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> SPRUCE_CABINET = registerBlock("spruce_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> BIRCH_CABINET = registerBlock("birch_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> JUNGLE_CABINET = registerBlock("jungle_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> ACACIA_CABINET = registerBlock("acacia_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> DARK_OAK_CABINET = registerBlock("dark_oak_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> MANGROVE_CABINET = registerBlock("mangrove_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL)));
	public static final Supplier<Block> CHERRY_CABINET = registerBlock("cherry_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL).sound(SoundType.CHERRY_WOOD)));
	public static final Supplier<Block> BAMBOO_CABINET = registerBlock("bamboo_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD)));
	public static final Supplier<Block> CRIMSON_CABINET = registerBlock("crimson_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
	public static final Supplier<Block> WARPED_CABINET = registerBlock("warped_cabinet",
		() -> new CabinetBlock(props(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
	public static final Supplier<Block> CANVAS_RUG = registerBlock("canvas_rug",
		() -> new CanvasRugBlock(props(Blocks.CARPET.pick(DyeColor.WHITE)).sound(SoundType.GRASS).strength(0.2F)));
	public static final Supplier<Block> TATAMI = registerBlock("tatami",
		() -> new TatamiBlock(props(Blocks.WOOL.pick(DyeColor.WHITE))));
	public static final Supplier<Block> FULL_TATAMI_MAT = registerBlock("full_tatami_mat",
		() -> new TatamiMatBlock(props(Blocks.WOOL.pick(DyeColor.WHITE)).strength(0.3F)));
	public static final Supplier<Block> HALF_TATAMI_MAT = registerBlock("half_tatami_mat",
		() -> new TatamiHalfMatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.pick(DyeColor.WHITE)).strength(0.3F).pushReaction(PushReaction.DESTROY)));

	public static final Supplier<Block> CANVAS_SIGN = registerBlock("canvas_sign",
		() -> new StandingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = registerBlock("white_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = registerBlock("orange_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = registerBlock("magenta_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = registerBlock("light_blue_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = registerBlock("yellow_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_SIGN = registerBlock("lime_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_SIGN = registerBlock("pink_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = registerBlock("gray_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = registerBlock("light_gray_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = registerBlock("cyan_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = registerBlock("purple_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = registerBlock("blue_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = registerBlock("brown_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = registerBlock("green_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_SIGN = registerBlock("red_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = registerBlock("black_canvas_sign",
		() -> new StandingCanvasSignBlock(DyeColor.BLACK));

	public static final Supplier<Block> CANVAS_WALL_SIGN = registerBlock("canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(CANVAS_SIGN.get().getLootTable()), null));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = registerBlock("white_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(WHITE_CANVAS_SIGN.get().getLootTable()), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = registerBlock("orange_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(ORANGE_CANVAS_SIGN.get().getLootTable()), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = registerBlock("magenta_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(MAGENTA_CANVAS_SIGN.get().getLootTable()), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = registerBlock("light_blue_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_BLUE_CANVAS_SIGN.get().getLootTable()), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = registerBlock("yellow_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(YELLOW_CANVAS_SIGN.get().getLootTable()), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = registerBlock("lime_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(LIME_CANVAS_SIGN.get().getLootTable()), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = registerBlock("pink_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(PINK_CANVAS_SIGN.get().getLootTable()), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = registerBlock("gray_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(GRAY_CANVAS_SIGN.get().getLootTable()), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = registerBlock("light_gray_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_GRAY_CANVAS_SIGN.get().getLootTable()), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = registerBlock("cyan_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(CYAN_CANVAS_SIGN.get().getLootTable()), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = registerBlock("purple_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(PURPLE_CANVAS_SIGN.get().getLootTable()), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = registerBlock("blue_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(BLUE_CANVAS_SIGN.get().getLootTable()), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = registerBlock("brown_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(BROWN_CANVAS_SIGN.get().getLootTable()), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = registerBlock("green_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(GREEN_CANVAS_SIGN.get().getLootTable()), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = registerBlock("red_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(RED_CANVAS_SIGN.get().getLootTable()), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = registerBlock("black_canvas_wall_sign",
		() -> new WallCanvasSignBlock(props(Blocks.SPRUCE_SIGN).overrideLootTable(BLACK_CANVAS_SIGN.get().getLootTable()), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_SIGN = registerBlock("hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = registerBlock("white_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = registerBlock("orange_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = registerBlock("magenta_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = registerBlock("light_blue_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = registerBlock("yellow_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = registerBlock("lime_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = registerBlock("pink_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = registerBlock("gray_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = registerBlock("light_gray_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = registerBlock("cyan_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = registerBlock("purple_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = registerBlock("blue_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = registerBlock("brown_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = registerBlock("green_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = registerBlock("red_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = registerBlock("black_hanging_canvas_sign",
		() -> new CeilingHangingCanvasSignBlock(DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = registerBlock("wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(HANGING_CANVAS_SIGN.get().getLootTable()), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = registerBlock("white_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(WHITE_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = registerBlock("orange_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(ORANGE_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = registerBlock("magenta_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(MAGENTA_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = registerBlock("light_blue_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_BLUE_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = registerBlock("yellow_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(YELLOW_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = registerBlock("lime_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIME_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = registerBlock("pink_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PINK_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = registerBlock("gray_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GRAY_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = registerBlock("light_gray_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_GRAY_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = registerBlock("cyan_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(CYAN_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = registerBlock("purple_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PURPLE_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = registerBlock("blue_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLUE_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = registerBlock("brown_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BROWN_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = registerBlock("green_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GREEN_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = registerBlock("red_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(RED_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = registerBlock("black_wall_hanging_canvas_sign",
		() -> new WallHangingCanvasSignBlock(props(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLACK_HANGING_CANVAS_SIGN.get().getLootTable()), DyeColor.BLACK));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = registerBlock("brown_mushroom_colony",
		() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), props(Blocks.BROWN_MUSHROOM)));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = registerBlock("red_mushroom_colony",
		() -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), props(Blocks.RED_MUSHROOM)));
	public static final Supplier<Block> ORGANIC_COMPOST = registerBlock("organic_compost",
		() -> new OrganicCompostBlock(props(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP)));
	public static final Supplier<Block> RICH_SOIL = registerBlock("rich_soil",
		() -> new RichSoilBlock(props(Blocks.DIRT).randomTicks()));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = registerBlock("rich_soil_farmland",
		() -> new RichSoilFarmlandBlock(props(Blocks.FARMLAND)));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = registerBlock("apple_pie",
		() -> new PieBlock(props(Blocks.CAKE), ModItems.APPLE_PIE_SLICE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = registerBlock("sweet_berry_cheesecake",
		() -> new PieBlock(props(Blocks.CAKE), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final Supplier<Block> CHOCOLATE_PIE = registerBlock("chocolate_pie",
		() -> new PieBlock(props(Blocks.CAKE), ModItems.CHOCOLATE_PIE_SLICE));
	public static final Supplier<Block> PUMPKIN_PIE = registerBlock("pumpkin_pie",
		() -> new PieBlock(props(Blocks.CAKE), ModItems.PUMPKIN_PIE_SLICE)
		{
			@Override
			public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
				return new ItemStack(Items.PUMPKIN_PIE);
			}
		});

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = registerBlock("sandy_shrub",
		() -> new SandyShrubBlock(props(Blocks.TALL_GRASS)));

	public static final Supplier<Block> WILD_CABBAGES = registerBlock("wild_cabbages",
		() -> new WildCropBlock(MobEffects.STRENGTH, 6, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_ONIONS = registerBlock("wild_onions",
		() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_TOMATOES = registerBlock("wild_tomatoes",
		() -> new WildCropBlock(MobEffects.POISON, 10, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_CARROTS = registerBlock("wild_carrots",
		() -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_POTATOES = registerBlock("wild_potatoes",
		() -> new WildCropBlock(MobEffects.NAUSEA, 8, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_BEETROOTS = registerBlock("wild_beetroots",
		() -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, props(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_RICE = registerBlock("wild_rice",
		() -> new WildRiceBlock(props(Blocks.TALL_GRASS)));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = registerBlock("cabbages",
		() -> new CabbageBlock(props(Blocks.WHEAT)));
	public static final Supplier<Block> ONION_CROP = registerBlock("onions",
		() -> new OnionBlock(props(Blocks.WHEAT)));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = registerBlock("budding_tomatoes",
		() -> new BuddingTomatoBlock(props(Blocks.WHEAT)));
	public static final DeferredHolder<Block, TomatoBlock> TOMATO_CROP = registerBlock("tomatoes",
		() -> new TomatoBlock(props().noCollision().randomTicks().instabreak().sound(SoundType.CROP)));
	public static final DeferredHolder<Block, HangingTomatoBlock> TOMATO_CROP_ON_ROPE = registerBlock("tomatoes_on_rope",
		() -> new HangingTomatoBlock(props(ModBlocks.TOMATO_CROP.get()).pushReaction(PushReaction.NORMAL)));
	public static final Supplier<Block> RICE_CROP = registerBlock("rice",
		() -> new RiceBlock(props(Blocks.WHEAT).strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = registerBlock("rice_panicles",
		() -> new RicePaniclesBlock(props(Blocks.WHEAT)));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = registerBlock("roast_chicken_block",
		() -> new RotatedFeastBlock(props(Blocks.CAKE), ModItems.ROAST_CHICKEN, true, BlockShapes.ROAST_CHICKEN_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = registerBlock("stuffed_pumpkin_block",
		() -> new FeastBlock(props(Blocks.PUMPKIN), ModItems.STUFFED_PUMPKIN, false, true));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = registerBlock("honey_glazed_ham_block",
		() -> new RotatedFeastBlock(props(Blocks.CAKE), ModItems.HONEY_GLAZED_HAM, true, BlockShapes.HONEY_GLAZED_HAM_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = registerBlock("shepherds_pie_block",
		() -> new RotatedFeastBlock(props(Blocks.CAKE), ModItems.SHEPHERDS_PIE, true, BlockShapes.SHEPHERDS_PIE_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> GLEAMING_SALAD_BLOCK = registerBlock("gleaming_salad_block",
		() -> new GleamingSaladBlock(props(Blocks.OAK_PLANKS).lightLevel(glowingFeastBlockEmission()), ModItems.GLEAMING_SALAD, true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = registerBlock("rice_roll_medley_block",
		() -> new RiceRollMedleyBlock(props(Blocks.CAKE)));
}
