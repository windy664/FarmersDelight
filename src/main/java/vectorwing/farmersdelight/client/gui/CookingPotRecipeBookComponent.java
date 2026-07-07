package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModRecipeCategories;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{
	// Cooking pot input slots are indices 0..5.
	private static final int INPUT_SLOT_COUNT = 6;

	protected static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted"));

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, ModRecipeCategories.createCookingPotTabInfo());
	}

	public void hide() {
		this.setVisible(false);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return RECIPE_BOOK_BUTTONS;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index >= 0 && slot.index < INPUT_SLOT_COUNT;
	}

	@Override
	@Nonnull
	protected Component getRecipeFilterName() {
		return TextUtils.container("recipe_book.cookable");
	}

	// M2 (26.2 port): the recipe-book ghost-preview / craftable-highlight system was rewritten around
	// RecipeDisplay + GhostSlots + StackedItemContents. Structural integration (tabs, filter, visibility)
	// is ported; ghost recipe + match highlighting are stubbed pending the new recipe-display port.
	@Override
	protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
	}
}
