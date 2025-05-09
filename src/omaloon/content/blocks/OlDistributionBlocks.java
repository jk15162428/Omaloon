package omaloon.content.blocks;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import omaloon.content.*;
import omaloon.world.blocks.distribution.*;
import omaloon.world.blocks.liquid.*;
import omaloon.world.meta.*;

import static mindustry.type.ItemStack.*;

public class OlDistributionBlocks{
    public static Block
        //item
        tubeConveyor, tubeDistributor, tubeJunction, tubeSorter, tubeGate, tubeUnderflowGate, tubeBridge,

    //liquid
    liquidTube, liquidJunction, liquidBridge, liquidPump, filterPump, liquidValve, liquidGauge,

    end;

    public static void load(){
        // region items
        tubeConveyor = new TubeConveyor("tube-conveyor"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 1
            ));
            researchCost = empty;
            health = 65;
            speed = 0.03f;
            displayedSpeed = 4.2f;
        }};

        tubeDistributor = new TubeDistributor("tube-distributor"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3
            ));
            researchCost = empty;
            speed = 10f;
            buildCostMultiplier = 4f;
            health = 65;
            drawer = new DrawMulti(
                new DrawRegion("-bottom"){{
                    layer = Layer.blockUnder;
                }}
            );
        }};

        tubeJunction = new TubeJunction("tube-junction"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3
            ));
            researchCostMultiplier = 0.3f;
            speed = 25;
            capacity = 4;
            health = 65;
            drawer = new DrawMulti(
                new DrawRegion("-bottom"){{
                    layer = Layer.blockUnder;
                }},
                new DrawDefault()
            );
        }};

        tubeSorter = new TubeSorter("tube-sorter"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3,
                Items.beryllium, 2, OlItems.carborundum, 1
            ));
            researchCostMultiplier = 0.3f;
            health = 65;
        }};

        tubeGate = new TubeGate("tube-overflow-gate"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3,
                Items.beryllium, 2, OlItems.carborundum, 1
            ));
            researchCostMultiplier = 0.3f;
            health = 65;
        }};

        tubeUnderflowGate = new TubeGate("tube-underflow-gate"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3,
                Items.beryllium, 2, OlItems.carborundum, 1
            ));
            invert = true;
            researchCostMultiplier = 0.3f;
            health = 65;
        }};

        tubeBridge = new TubeItemBridge("tube-bridge-conveyor"){{
            requirements(Category.distribution, with(
                OlItems.cobalt, 3,
                Items.beryllium, 2
            ));
            researchCostMultiplier = 0.3f;
            fadeIn = moveArrows = false;
            range = 4;
            speed = 74f;
            arrowSpacing = 6f;
            bufferCapacity = 14;
        }};
        //endregion

        //region liquids
        liquidTube = new PressureLiquidConduit("liquid-tube"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 2
            ));
            researchCost = with(
                OlItems.cobalt, 10
            );
        }};

        liquidJunction = new PressureLiquidJunction("liquid-junction"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 5
            ));
            researchCostMultiplier = 0.3f;
        }};

        liquidBridge = new PressureLiquidBridge("liquid-bridge"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 2,
                Items.beryllium, 3
            ));
            range = 4;
        }};

        liquidPump = new PressureLiquidPump("liquid-pump"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 4
            ));
            researchCost = with(
                OlItems.cobalt, 25
            );
            pumpStrength = 5f / 60f;

            ambientSound = Sounds.wind2;
            ambientSoundVolume = 0.1f;

            pumpEffectIn = OlFx.pumpIn;
            pumpEffectOut = OlFx.pumpOut;

            pressureConfig = new PressureConfig(){{
//                minPressure = -25f;
//                maxPressure = 25f;
            }};
        }};

        filterPump = new PressureLiquidPump("filter-pump"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 4
            ));
            researchCost = with(
                OlItems.cobalt, 20
            );
            pumpStrength = 1f / 6f;
            pressureDifference = 0;

            configurable = true;

            ambientSound = Sounds.wind2;
            ambientSoundVolume = 0.1f;

            pumpEffectIn = OlFx.pumpIn;
            pumpEffectOut = OlFx.pumpOut;
        }};

        liquidValve = new PressureLiquidValve("liquid-valve"){{
            requirements(Category.liquid, BuildVisibility.sandboxOnly, with(
                OlItems.cobalt, 2,
                Items.beryllium, 2
            ));
            researchCost = with(
                OlItems.cobalt, 20,
                Items.beryllium, 20
            );
            pressureLoss = 0.3f;

            pumpingEffectIn = OlFx.flowIn;
            pumpingEffectOut = OlFx.flowOut;
        }};

        liquidGauge = new PressureLiquidGauge("liquid-gauge"){{
            requirements(Category.liquid, with(
                OlItems.cobalt, 2,
                Items.beryllium, 1
            ));
            researchCost = with(
                OlItems.cobalt, 20,
                Items.beryllium, 10
            );
        }};
        //endregion
    }
}
