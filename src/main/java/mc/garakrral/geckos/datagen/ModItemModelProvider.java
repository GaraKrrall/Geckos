package mc.garakrral.geckos.datagen;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.item.ModItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Main.MODID, helper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.DEAD_FLY.get());
    }
}
