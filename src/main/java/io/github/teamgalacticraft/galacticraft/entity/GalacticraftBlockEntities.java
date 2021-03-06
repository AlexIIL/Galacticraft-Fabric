package io.github.teamgalacticraft.galacticraft.entity;

import io.github.teamgalacticraft.galacticraft.Constants;
import io.github.teamgalacticraft.galacticraft.blocks.machines.basicsolarpanel.BasicSolarPanelBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.basicsolarpanel.BasicSolarPanelPartBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.circuitfabricator.CircuitFabricatorBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.coalgenerator.CoalGeneratorBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.compressor.CompressorBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.electriccompressor.ElectricCompressorBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.energystoragemodule.EnergyStorageModuleBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.oxygencollector.OxygenCollectorBlockEntity;
import io.github.teamgalacticraft.galacticraft.blocks.machines.refinery.RefineryBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author <a href="https://github.com/teamgalacticraft">TeamGalacticraft</a>
 */
public class GalacticraftBlockEntities {
    public static final BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.COAL_GENERATOR), BlockEntityType.Builder.create(CoalGeneratorBlockEntity::new).build(null));
    public static final BlockEntityType<BasicSolarPanelBlockEntity> BASIC_SOLAR_PANEL_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.BASIC_SOLAR_PANEL), BlockEntityType.Builder.create(BasicSolarPanelBlockEntity::new).build(null));
    public static final BlockEntityType<BasicSolarPanelPartBlockEntity> BASIC_SOLAR_PANEL_PART_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.BASIC_SOLAR_PANEL_PART), BlockEntityType.Builder.create(BasicSolarPanelPartBlockEntity::new).build(null));
    public static final BlockEntityType<CircuitFabricatorBlockEntity> CIRCUIT_FABRICATOR_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.CIRCUIT_FABRICATOR), BlockEntityType.Builder.create(CircuitFabricatorBlockEntity::new).build(null));
    public static final BlockEntityType<CompressorBlockEntity> COMPRESSOR_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.COMPRESSOR), BlockEntityType.Builder.create(CompressorBlockEntity::new).build(null));
    public static final BlockEntityType<ElectricCompressorBlockEntity> ELECTRIC_COMPRESSOR_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.ELECTRIC_COMPRESSOR), BlockEntityType.Builder.create(ElectricCompressorBlockEntity::new).build(null));
    public static final BlockEntityType<EnergyStorageModuleBlockEntity> ENERGY_STORAGE_MODULE_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.ENERGY_STORAGE_MODULE), BlockEntityType.Builder.create(EnergyStorageModuleBlockEntity::new).build(null));
    public static final BlockEntityType<RefineryBlockEntity> REFINERY_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.REFINERY), BlockEntityType.Builder.create(RefineryBlockEntity::new).build(null));
    public static final BlockEntityType<OxygenCollectorBlockEntity> OXYGEN_COLLECTOR_TYPE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(Constants.MOD_ID, Constants.Blocks.OXYGEN_COLLECTOR), BlockEntityType.Builder.create(OxygenCollectorBlockEntity::new).build(null));
    public static void init() {
    }
}