package omaloon.content;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.unit.*;
import omaloon.ai.*;
import omaloon.ai.drone.*;
import omaloon.entities.abilities.*;
import omaloon.entities.part.*;
import omaloon.gen.*;
import omaloon.type.*;

import static arc.Core.*;

public class OlUnitTypes{
    // flying
    public static UnitType cilantro, basil, sage;

    // mech
    public static UnitType legionnaire, centurion, praetorian;

    // lumen
    public static UnitType lumen;

    // ornitopter
    public static @EntityDef({Unitc.class, Flyingc.class, Ornitopterc.class}) UnitType effort;

    // millipede
    public static @EntityDef({Unitc.class, ChainMechc.class, Chainedc.class}) UnitType collector;

    // core
    public static UnitType discovery;

    public static @EntityDef({Unitc.class, Corec.class, FloatMechc.class}) UnitType walker;

    public static @EntityDef({Unitc.class, Dronec.class}) UnitType attackDroneAlpha, actionDroneMono;

    public static void load(){
        collector = new ChainedUnitType("collector"){{
            constructor = ChainedChainMechUnit::create;
            segmentAI = u -> new ChainedAI();

            omniMovement = false;

            speed = 0.6f;
            health = 200f;
            regenTime = -1f;
            chainTime = 60f;

            maxSegments = 6;

            splittable = true;

            angleLimit = 65f;
            segmentDamageScl = 8f;
            segmentCast = 8;
            segmentOffset = 6.7f;

            segmentLayerOffset = 0.001f;

            hoverable = hovering = false;
            mechSideSway = 0.25f;

            weaponsIndex = unit -> {
                if(unit instanceof Chainedc chain){
                    if(chain.isHead() || chain.isTail()) return 0;
                    return 1;
                }
                return 0;
            };
            chainWeapons.add(
                Seq.with(),
                Seq.with(
                    new Weapon("omaloon-collector-launcher"){{
                        x = 0f;
                        y = 1f;
                        rotate = true;
                        mirror = false;
                        reload = 60f;
                        bullet = new ArtilleryBulletType(5f, 7){{
                            maxRange = 40f;
                            collidesTiles = collidesAir = collidesGround = true;
                            width = height = 11f;
                            splashDamage = 25f;
                            splashDamageRadius = 25f;
                            trailColor = hitColor = lightColor = backColor = Pal.thoriumPink;
                            frontColor = Pal.thoriumPink;
                        }};
                    }}
                )
            );
        }};

        //region core
        attackDroneAlpha = new DroneUnitType("combat-drone-alpha"){{
            itemCapacity = 0;
            speed = 2.2f;
            accel = 0.08f;
            drag = 0.04f;
            health = 70;
            engineOffset = 4f;
            engineSize = 2;
            hitSize = 9;


            weapons.add(new Weapon(){{
                y = 0f;
                x = 1.5f;
                reload = 20f;
                ejectEffect = Fx.casing1;

                shootCone = 60f;

                bullet = new BasicBulletType(2.5f, 6){{
                    width = 7f;
                    height = 9f;
                    lifetime = 45f;

                    hitColor = backColor = trailColor = Color.valueOf("feb380");

                    trailWidth = 1.3f;
                    trailLength = 7;

                    shootEffect = Fx.shootSmall;
                    smokeEffect = Fx.shootSmallSmoke;
                    ammoMultiplier = 2;
                }};
                shootSound = OlSounds.theShoot;
            }});
            shadowElevationScl = 0.4f;
        }};

        actionDroneMono = new DroneUnitType("main-drone-mono"){{
            mineTier = 3;
            itemCapacity = 1;

            speed = 2.2f;
            accel = 0.08f;
            drag = 0.04f;
            health = 70;
            engineOffset = 4f;
            engineSize = 2;

            buildRange = 60f;
            buildSpeed = 1f;
            mineSpeed = 5.5f;
            mineRange = 40;

            hitSize = 9;

            shadowElevationScl = 0.4f;
        }};

        walker = new GlassmoreUnitType("walker"){{
            constructor = FloatMechCoreUnit::create;
            aiController = BuilderAI::new;

            buildRange = range = mineRange = 200f;
            buildSpeed = 1f;

            rotateToBuilding = faceTarget = false;

            speed = 0.5f;
            hitSize = 8f;
            health = 150;
            boostMultiplier = 0.8f;

            mineTier = 3;

            abilities.addAll(
            new DroneAbility(attackDroneAlpha){{
                name = "omaloon-combat-drone";
                droneController = AttackDroneAI::new;
                spawnTime = 180f;
                spawnX = 5f;
                spawnY = 0f;
                spawnEffect = Fx.spawn;
                parentizeEffects = true;
                anchorPos = new Vec2[]{
                new Vec2(12f, 0f),
                };
            }},
            new DroneAbility(actionDroneMono){{
                name = "omaloon-utility-drone";
                droneController = UtilityDroneAI::new;
                spawnTime = 180f;
                spawnX = -5f;
                spawnY = 0f;
                spawnEffect = Fx.spawn;
                parentizeEffects = true;
                anchorPos = new Vec2[]{
                new Vec2(-12f, 0f),
                };
            }}
            );

            shadowElevationScl = 0.3f;
        }};

        discovery = new GlassmoreUnitType("discovery"){{
            controller = u -> new BuilderAI(true, 500f);
            constructor = UnitEntity::create;
            isEnemy = hittable = false;

            lowAltitude = true;
            flying = true;
            mineSpeed = 4.5f;
            mineTier = 2;
            mineItems = Seq.with(OlItems.cobalt, Items.beryllium);
            buildSpeed = 0.3f;
            drag = 0.03f;
            speed = 2f;
            rotateSpeed = 13f;
            accel = 0.1f;
            itemCapacity = 20;
            health = 110f;
            engineOffset = 5f;
            hitSize = 8f;
            alwaysUnlocked = true;
        }};
        //endregion

        effort = new OrnitopterUnitType("effort"){{
            constructor = OrnitopterFlyingUnit::create;
            aiController = () -> new CowardAI(){
                @Override
                public boolean retarget(){
                    return timer.get(timerTarget, 10);
                }
            };
            lowAltitude = true;
            speed = 2.7f;
            accel = 0.08f;
            drag = 0.04f;
            flying = true;
            health = 160;
            range = 140f;
            faceTarget = false;
            circleTarget = true;
            forceMultiTarget = true;
            rotateMoveFirst = true;
            rotateSpeed = 8f;
            fallDriftScl = 60f;

            blades.addAll(new Blade(name + "-blade"){{
                layerOffset = 0f;
                x = 3f;
                y = 1.5f;
                bladeMaxMoveAngle = 35;
                blurAlpha = 1f;
            }});

            blades.addAll(new Blade(name + "-blade"){{
                layerOffset = 0f;
                x = 3f;
                y = -1f;
                bladeMaxMoveAngle = -35;
                blurAlpha = 1f;
            }});
            hitSize = 8;

            weapons.add(
                new Weapon(){{
                    x = 0; y = 4;
                    shootY = 0;
                    mirror = false;

                    ignoreRotation = true;
                    shootCone = 180f;
                    reload = 30;

                    controllable = true;
                    targetInterval = targetSwitchInterval = 0f;

                    bullet = new BulletType(1f, 10){{
                        lifetime = 2;
                        hitSize = 2;
                    }};
                }}
            );
        }};

        lumen = new GlassmoreUnitType("lumen"){{
            constructor = UnitEntity::create;

            hitSize = 10f;

            speed = 1.7f;
            accel = 0.08f;
            drag = 0.04f;
            rotateSpeed = 7f;

            flying = true;
            health = 70;

            range = 0.1f;
            targetAir = false;

            deathSound = OlSounds.tankBang;

            outlineRegion = atlas.find("omaloon-lumen-outline");
            alwaysCreateOutline = true;

//            weapons.add(new FilterWeapon(){{
//                //TODO: shoot filter bullet on destroy / death
//                name = "omaloon-lumen-sprayer";
//                maxRange = 0.1f;
//                mirror = false;
//                x = 0f;
//                y = 0.25f;
//                rotate = false;
//                layerOffset = -0.01f;
//
//                shootSound = Sounds.none;
//                shootOnDeath = true;
//                shootX = shootY = 0f;
//                shoot = new ShootSpread(30, 1);
//                inaccuracy = 360f;
//                velocityRnd = 0.8f;
//                reload = 30f;
//                recoil = 0f;
//
//                shootCone = 15f;
//
//                bullets = new BulletType[]{
//                    new LiquidBulletType(OlLiquids.glacium){{
//                        //recoil = 0.06f;
//                        killShooter = true;
//
//                        speed = 5f;
//                        drag = 0.2f;
//
//                        shootEffect = Fx.shootSmall;
//
//                        lifetime = 17f;
//
//                        collidesAir = false;
//                        status = OlStatusEffects.glacied;
//                        statusDuration = 60f * 5f;
//
//                        puddleLiquid = OlLiquids.glacium;
//                        puddles = 5;
//                        puddleAmount = 80f;
//                        puddleSize = 8f;
//
//                        despawnHit = true;
//
//                        despawnSound = hitSound = Sounds.splash;
//                    }
//                        @Override
//                        public void init(Bullet b){
//                            if(killShooter && b.owner() instanceof Unit u && !u.dead()){
//                                u.elevation = 0;
//                                u.health = -1;
//                                u.dead = true;
//                                u.type().deathSound = OlSounds.tankBang;
//                                u.kill();
//                            }
//                        }
//                    },
//                    new LiquidBulletType(Liquids.water){{
//                        //recoil = 0.06f;
//                        killShooter = true;
//
//                        speed = 5f;
//                        drag = 0.2f;
//
//                        shootEffect = Fx.shootSmall;
//
//                        lifetime = 17f;
//
//                        collidesAir = false;
//                        status = StatusEffects.wet;
//                        statusDuration = 60f * 5f;
//
//                        puddleLiquid = Liquids.water;
//                        puddles = 5;
//                        puddleAmount = 80f;
//                        puddleSize = 8f;
//
//                        despawnHit = true;
//
//                        despawnSound = hitSound = Sounds.splash;
//                    }
//                        @Override
//                        public void init(Bullet b){
//                            if(killShooter && b.owner() instanceof Unit u && !u.dead()){
//                                u.elevation = 0;
//                                u.health = -1;
//                                u.dead = true;
//                                u.type().deathSound = OlSounds.tankBang;
//                                u.kill();
//                            }
//                        }
//                    },
//                    new LiquidBulletType(Liquids.slag){{
//                        //recoil = 0.06f;
//                        killShooter = true;
//
//                        speed = 5f;
//                        drag = 0.2f;
//
//                        shootEffect = Fx.shootSmall;
//
//                        lifetime = 17f;
//
//                        collidesAir = false;
//                        status = StatusEffects.melting;
//                        statusDuration = 60f * 5f;
//
//                        puddleLiquid = Liquids.slag;
//                        puddles = 5;
//                        puddleAmount = 80f;
//                        puddleSize = 8f;
//
//                        despawnHit = true;
//
//                        despawnSound = hitSound = Sounds.splash;
//                    }
//                        @Override
//                        public void init(Bullet b){
//                            if(killShooter && b.owner() instanceof Unit u && !u.dead()){
//                                u.elevation = 0;
//                                u.health = -1;
//                                u.dead = true;
//                                u.type().deathSound = OlSounds.tankBang;
//                                u.kill();
//                            }
//                        }
//                    },
//                    new LiquidBulletType(Liquids.oil){{
//                        //recoil = 0.06f;
//                        killShooter = true;
//
//                        speed = 5f;
//                        drag = 0.2f;
//
//                        shootEffect = Fx.shootSmall;
//
//                        lifetime = 17f;
//
//                        collidesAir = false;
//                        status = StatusEffects.tarred;
//                        statusDuration = 60f * 5f;
//
//                        puddleLiquid = Liquids.oil;
//                        puddles = 5;
//                        puddleAmount = 80f;
//                        puddleSize = 8f;
//
//                        despawnHit = true;
//
//                        despawnSound = hitSound = Sounds.splash;
//                    }
//                        @Override
//                        public void init(Bullet b){
//                            if(killShooter && b.owner() instanceof Unit u && !u.dead()){
//                                u.elevation = 0;
//                                u.health = -1;
//                                u.dead = true;
//                                u.type().deathSound = OlSounds.tankBang;
//                                u.kill();
//                            }
//                        }
//                    }
//                };
//                icons = new String[]{
//                    "omaloon-filled-with-glacium",
//                    "omaloon-filled-with-water",
//                    "omaloon-filled-with-slag",
//                    "omaloon-filled-with-oil"
//                };
//                tint = unit -> {
//                    if(!unit.dead() && unit.hasEffect(OlStatusEffects.filledWithGlacium)) return OlLiquids.glacium;
//                    if(!unit.dead() && unit.hasEffect(OlStatusEffects.filledWithWater)) return Liquids.water;
//                    if(!unit.dead() && unit.hasEffect(OlStatusEffects.filledWithSlag)) return Liquids.slag;
//                    if(!unit.dead() && unit.hasEffect(OlStatusEffects.filledWithOil)) return Liquids.oil;
//                    return null;
//                };
//                bulletFilter = unit -> {
//                    if(unit.hasEffect(OlStatusEffects.filledWithGlacium)) return bullets[0];
//                    if(unit.hasEffect(OlStatusEffects.filledWithWater)) return bullets[1];
//                    if(unit.hasEffect(OlStatusEffects.filledWithSlag)) return bullets[2];
//                    if(unit.hasEffect(OlStatusEffects.filledWithOil)) return bullets[3];
//                    return new BulletType(0, 0){{
//                        shootEffect = smokeEffect = hitEffect = despawnEffect = Fx.none;
//                    }};
//                };
//            }});
            weapons.add(new Weapon(){{
                bullet = new BulletType(){{
                    killShooter = instantDisappear = true;
                    hitEffect = shootEffect = smokeEffect = despawnEffect = Fx.none;
                }
                    @Override
                    public void init(Bullet b) {
                        super.init(b);
                        if (b.owner() instanceof Unit u) u.elevation = 0;
                    }
                };
            }});

            abilities.add(
                new TankAbility(OlStatusEffects.filledWithGlacium, new BulletType(){{
                    killShooter = instantDisappear = true;
                    hitEffect = shootEffect = smokeEffect = despawnEffect = Fx.none;

                    fragBullets = 30;
                    fragBullet = new LiquidBulletType(OlLiquids.glacium){{
                        //recoil = 0.06f;
                        killShooter = true;

                        speed = 5f;
                        drag = 0.2f;

                        shootEffect = Fx.shootSmall;

                        lifetime = 17f;

                        collidesAir = false;
                        status = OlStatusEffects.glacied;
                        statusDuration = 60f * 5f;

                        puddleLiquid = OlLiquids.glacium;
                        puddles = 5;
                        puddleAmount = 80f;
                        puddleSize = 8f;

                        despawnHit = true;

                        despawnSound = hitSound = Sounds.splash;
                    }};
                }}),
                new TankAbility(OlStatusEffects.filledWithWater, new BulletType(){{
                    killShooter = instantDisappear = true;
                    hitEffect = shootEffect = smokeEffect = despawnEffect = Fx.none;

                    fragBullets = 30;
                    fragBullet = new LiquidBulletType(Liquids.water){{
                        //recoil = 0.06f;
                        killShooter = true;

                        speed = 5f;
                        drag = 0.2f;

                        shootEffect = Fx.shootSmall;

                        lifetime = 17f;

                        collidesAir = false;
                        status = StatusEffects.wet;
                        statusDuration = 60f * 5f;

                        puddleLiquid = Liquids.water;
                        puddles = 5;
                        puddleAmount = 80f;
                        puddleSize = 8f;

                        despawnHit = true;

                        despawnSound = hitSound = Sounds.splash;
                    }};
                }}),
                new TankAbility(OlStatusEffects.filledWithSlag, new BulletType(){{
                    killShooter = instantDisappear = true;
                    hitEffect = shootEffect = smokeEffect = despawnEffect = Fx.none;

                    fragBullets = 30;
                    fragBullet = new LiquidBulletType(Liquids.slag){{
                        //recoil = 0.06f;
                        killShooter = true;

                        speed = 5f;
                        drag = 0.2f;

                        shootEffect = Fx.shootSmall;

                        lifetime = 17f;

                        collidesAir = false;
                        status = StatusEffects.melting;
                        statusDuration = 60f * 5f;

                        puddleLiquid = Liquids.slag;
                        puddles = 5;
                        puddleAmount = 80f;
                        puddleSize = 8f;

                        despawnHit = true;

                        despawnSound = hitSound = Sounds.splash;
                    }};
                }}),
                new TankAbility(OlStatusEffects.filledWithOil, new BulletType(){{
                    killShooter = instantDisappear = true;
                    hitEffect = shootEffect = smokeEffect = despawnEffect = Fx.none;

                    fragBullets = 30;
                    fragBullet = new LiquidBulletType(Liquids.oil){{
                        //recoil = 0.06f;
                        killShooter = true;

                        speed = 5f;
                        drag = 0.2f;

                        shootEffect = Fx.shootSmall;

                        lifetime = 17f;

                        collidesAir = false;
                        status = StatusEffects.tarred;
                        statusDuration = 60f * 5f;

                        puddleLiquid = Liquids.oil;
                        puddles = 5;
                        puddleAmount = 80f;
                        puddleSize = 8f;

                        despawnHit = true;

                        despawnSound = hitSound = Sounds.splash;
                    }};
                }})
            );
        }};

        //region roman
        legionnaire = new GlassmoreUnitType("legionnaire"){{
            constructor = MechUnit::create;
            speed = 0.5f;
            hitSize = 8f;
            health = 150;

            outlineRegion = atlas.find("omaloon-legionnaire-outline");
            alwaysCreateOutline = true;

            weapons.add(new Weapon("omaloon-legionnaire-weapon"){{
                shootSound = OlSounds.theShoot;
                top = false;

                layerOffset = -0.001f;
                reload = 35f;
                x = 4.7f;
                y = 0.4f;

                shootCone = 45f;

                ejectEffect = Fx.casing1;
                bullet = new BasicBulletType(2.5f, 5){{
                    width = 7f;
                    height = 7f;
                    lifetime = 35f;

                    maxRange = 100;

                    despawnEffect = Fx.hitBulletSmall;
                    hitEffect = Fx.none;
                    hitColor = backColor = trailColor = Color.valueOf("feb380");

                    trailWidth = 1.3f;
                    trailLength = 10;
                }};
            }});
        }};

        centurion = new GlassmoreUnitType("centurion"){{
            constructor = MechUnit::create;
            speed = 0.4f;
            hitSize = 9f;
            health = 250;
            range = 50;

            outlineRegion = atlas.find("omaloon-centurion-outline");
            alwaysCreateOutline = true;

            weapons.add(new Weapon("omaloon-centurion-weapon"){{
                shootSound = OlSounds.theShoot;
                mirror = true;
                top = false;

                layerOffset = -0.001f;
                reload = 35f;
                x = 5.75f;
                y = 0.27f;
                shootX = -0.5f;
                shootY = 5.5f;
                recoil = 1.3f;
                inaccuracy = 25;

                shoot.shots = 4;
                shoot.shotDelay = 0.2f;
                velocityRnd = 0.5f;

                shootCone = 45f;

                ejectEffect = Fx.casing1;
                bullet = new BasicBulletType(5.5f, 5){{
                    width = 4f;
                    height = 4f;
                    lifetime = 12f;

                    maxRange = 50;

                    despawnEffect = Fx.hitBulletSmall;
                    hitEffect = Fx.none;
                    hitColor = backColor = trailColor = Color.valueOf("feb380");

                    trailWidth = 0.8f;
                    trailLength = 10;
                }};
            }});
        }};

        praetorian = new GlassmoreUnitType("praetorian"){{
            constructor = MechUnit::create;
            speed = 0.3f;
            hitSize = 16f;
            rotateSpeed = 2f;
            health = 400;
            range = 200f;

            targetAir = false;

            outlineRegion = atlas.find("omaloon-praetorian-outline");
            alwaysCreateOutline = true;

            weapons.add(new Weapon("") {{
                x = 7.25f;
                y = 0f;

                rotate = true;
                rotateSpeed = 7f;
                rotationLimit = 30;

                reload = 140f;

                parts.add(new ConstructPart() {{
                    name = "omaloon-praetorian-missile";
                    layerOffset = -0.01f;
                    progress = PartProgress.reload.inv();
                }});

                shootY = 0;
                shootCone = 10f;

                recoil = 0f;

                shootSound = Sounds.missileLarge;
                bullet = new BulletType() {{
                    shake = 1f;
                    keepVelocity = false;
                    collidesAir = false;
                    spawnUnit = new MissileUnitType("praetorian-missile"){{
                        targetAir = false;
                        speed = 4f;
                        lifetime = 60f;
                        drawCell = false;
                        outlineColor = Color.valueOf("2f2f36");

                        missileAccelTime = 1f;
                        accel = drag = 0.1f;
                        rotateSpeed = 1f;

                        weapons.add(new Weapon(){{
                            shootCone = 360f;
                            mirror = false;
                            reload = 1f;
                            shootOnDeath = true;
                            bullet = new ExplosionBulletType(30f, 25f) {{
                                shootEffect = Fx.massiveExplosion;
                                collidesAir = false;
                            }};
                        }});
                    }};;
                }};
            }});
        }};
        //endregion

        //region vegetable
        cilantro = new GlassmoreUnitType("cilantro"){{
            flying = lowAltitude = true;
            health = 160;
            hitSize = 8f;

            accel = 0.05f;
            drag = 0.03f;
            rotateSpeed = 10f;
            trailLength = 10;

            constructor = UnitEntity::create;

            weapons.addAll(new Weapon(){{
                mirror = false;

                x = 0;
                y = 1;

                reload = 30;
                shoot.firstShotDelay = 60f;

                shootCone = 45f;

                shootSound = Sounds.lasershoot;
                bullet = new BasicBulletType(2f, 6, "omaloon-triangle-bullet"){{
                    width = height = 8f;
                    shrinkY = 0f;
                    trailWidth = 2f;
                    trailLength = 5;

                    frontColor = Color.valueOf("D1EFFF");
                    backColor = hitColor = trailColor = Color.valueOf("8CA9E8");

                    chargeEffect = OlFx.shootShockwave;
                    shootEffect = smokeEffect = Fx.none;
                }};
            }});
        }};

        basil = new GlassmoreUnitType("basil"){{
            flying = lowAltitude = true;
            health = 280;
            hitSize = 20f;

            drag = 0.09f;
            speed = 1.8f;
            rotateSpeed = 2.5f;
            accel = 0.05f;

            engineOffset = 12f;
            setEnginesMirror(new UnitEngine(5, -10f, 2, -45));

            constructor = UnitEntity::create;

            weapons.addAll(new Weapon(){{
                mirror = false;
                continuous = alwaysContinuous = true;

                x = 0f;
                y = -3f;
                shootSound = Sounds.smelter;

                bullet = new ContinuousFlameBulletType(5){{
                    colors = new Color[]{Color.valueOf("8CA9E8"), Color.valueOf("8CA9E8"), Color.valueOf("D1EFFF")};

                    lifetime = 60f;

                    shootCone = 360f;

                    width = 2.5f;
                    length = 75f;
                    lengthInterp = a -> Interp.smoother.apply(Mathf.slope(a));
                    flareLength = 20f;
                    flareInnerLenScl = flareRotSpeed = 0f;
                    pierceCap = 1;
                    flareColor = Color.valueOf("D1EFFF");

                    hitEffect = new ParticleEffect(){{
                        lifetime = 30f;
                        length = 20f;

                        interp = Interp.pow2Out;

                        colorFrom = Color.valueOf("D1EFFF");
                        colorTo = Color.valueOf("8CA9E8");
                    }};
                }};
            }});
        }};

        sage = new GlassmoreUnitType("sage"){{
            flying = lowAltitude = true;
            health = 550;
            hitSize = 35f;

            speed = 0.8f;
            accel = 0.04f;
            drag = 0.04f;
            rotateSpeed = 1.9f;

            constructor = UnitEntity::create;

            engineOffset = 16f;
            engineSize = 6f;
            setEnginesMirror(new UnitEngine(10, -14f, 3, -45));

            BulletType shootType = new BasicBulletType(2f, 5){{
                lifetime = 55f;

                splashDamage = 20f;
                splashDamageRadius = 32f;

                width = height = 8f;
                shrinkY = 0f;

                trailWidth = 2f;
                trailLength = 5;

                frontColor = Color.valueOf("D1EFFF");
                backColor = trailColor = Color.valueOf("8CA9E8");

                hitEffect = despawnEffect = OlFx.hitSage;
                hitSound = despawnSound = Sounds.plasmaboom;
            }};

            weapons.addAll(
                new Weapon("omaloon-sage-salvo"){{
                    reload = 90f;
                    rotate = true;
                    rotateSpeed = 12f;
                    x = 6.5f;
                    y = 1f;

                    shoot.firstShotDelay = 40f;

                    shootCone = 45f;

                    shootSound = Sounds.missile;
                    bullet = shootType;
                }},
                new Weapon("omaloon-sage-salvo"){{
                    reload = 90f;
                    rotate = true;
                    rotateSpeed = 14f;
                    x = -10.25f;
                    y = -8f;

                    shootSound = Sounds.missile;
                    bullet = shootType;
                }}
            );
        }};
        //endregion
    }


}
