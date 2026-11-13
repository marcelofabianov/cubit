package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.item.PowerBankItem;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

/** The storage of the dock: what it keeps, charges, gives back and saves (answers 947 to 959). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockStorageGameTests {
  private static final String ARENA = "arena";
  private static final String WRONG_ITEM = "The storage kept the wrong items";
  private static final String WRONG_CHARGE = "The storage charged the wrong bank";
  private static final String WRONG_AUTOMATION = "The automation moved the wrong item";
  private static final String LOST = "The storage lost its items";
  private static final String NOT_IN_ITEM = "The item of the dock should keep the storage";
  private static final String NOT_DROPPED = "The refused item should drop beside the dock";
  private static final int FIRST = 0;
  private static final int SECOND = 1;
  private static final int FIRST_STORAGE_HANDLER_SLOT = 1;
  private static final int INPUT_HANDLER_SLOT = 0;
  private static final int ONE = 1;
  private static final int CHARGE_TICKS = 20;
  private static final int DROP_TICKS = 5;
  private static final String STORAGE_KEY = "Storage";
  private static final String CHARGE_SLOT_KEY = "ChargeSlot";
  private static final String SLOT_KEY = "Slot";
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy BASE_CAPACITY = new Energy(210_000);

  private DockStorageGameTests() {}

  @GameTest(template = ARENA)
  public static void theStorageKeepsOnlyItsFourItems(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, PLENTY);

    helper.assertTrue(dock.storage().canPlaceItem(FIRST, toolbox()), WRONG_ITEM);
    helper.assertTrue(dock.storage().canPlaceItem(FIRST, emptyBank()), WRONG_ITEM);
    helper.assertTrue(dock.storage().canPlaceItem(FIRST, robot()), WRONG_ITEM);
    helper.assertTrue(dock.storage().canPlaceItem(FIRST, damagedRobot()), WRONG_ITEM);
    helper.assertFalse(
        dock.storage().canPlaceItem(FIRST, new ItemStack(Items.REDSTONE)), WRONG_ITEM);
    helper.assertFalse(dock.storage().canPlaceItem(FIRST, new ItemStack(Items.DIRT)), WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void onlyTheFirstBankThatIsNotFullCharges(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, PLENTY);
    dock.storage().setItem(FIRST, emptyBank());
    dock.storage().setItem(SECOND, emptyBank());

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          helper.assertFalse(chargeAt(dock, FIRST).isZero(), WRONG_CHARGE);
          helper.assertTrue(chargeAt(dock, SECOND).isZero(), WRONG_CHARGE);
          helper.assertValueEqual(chargeAt(dock, FIRST).plus(dock.energy()), PLENTY, LOST);
          helper.succeed();
        });
  }

  /** The banks wait while the robot in the base still charges (answer 952). */
  @GameTest(template = ARENA)
  public static void theBanksWaitForTheDockedRobot(GameTestHelper helper) {
    Player owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    DockScene.placeOnDock(helper, owner, Energy.ZERO);
    dock.storage().setItem(FIRST, emptyBank());

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          helper.assertTrue(chargeAt(dock, FIRST).isZero(), WRONG_CHARGE);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aClickWithAToolboxStoresIt(GameTestHelper helper) {
    Player owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);

    owner.setItemInHand(InteractionHand.MAIN_HAND, toolbox());
    DockScene.clickWithHeldItem(helper, owner, DockScene.DOCK);

    helper.assertTrue(dock.storage().getItem(FIRST).is(CubitItems.TOOLBOX.get()), WRONG_ITEM);
    helper.assertTrue(owner.getMainHandItem().isEmpty(), WRONG_ITEM);
    helper.succeed();
  }

  /** A Damaged Cubit in the hand is stored, and a Cubit still goes to the base (answer 968). */
  @GameTest(template = ARENA)
  public static void aClickWithADamagedCubitStoresIt(GameTestHelper helper) {
    Player owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);

    owner.setItemInHand(InteractionHand.MAIN_HAND, damagedRobot());
    DockScene.clickWithHeldItem(helper, owner, DockScene.DOCK);

    helper.assertTrue(
        dock.storage().getItem(FIRST).is(CubitItems.DAMAGED_GENERAL_ROBOT.get()), WRONG_ITEM);
    helper.succeed();
  }

  /** The robot items go in and out by automation at any time (answer 967). */
  @GameTest(template = ARENA)
  public static void theAutomationMovesTheRobotItems(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, Energy.ZERO);
    IItemHandler handler = dock.itemHandler();

    handler.insertItem(FIRST_STORAGE_HANDLER_SLOT, robot(), false);
    handler.insertItem(FIRST_STORAGE_HANDLER_SLOT + ONE, damagedRobot(), false);

    helper.assertFalse(
        handler.extractItem(FIRST_STORAGE_HANDLER_SLOT, ONE, false).isEmpty(), WRONG_AUTOMATION);
    helper.assertFalse(
        handler.extractItem(FIRST_STORAGE_HANDLER_SLOT + ONE, ONE, false).isEmpty(),
        WRONG_AUTOMATION);
    helper.succeed();
  }

  /** By any face: both go in, the toolbox comes out, and a bank only when full (answer 953). */
  @GameTest(template = ARENA)
  public static void theAutomationTakesTheToolboxAndOnlyAFullBank(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, Energy.ZERO);
    IItemHandler handler = dock.itemHandler();

    ItemStack refused = handler.insertItem(INPUT_HANDLER_SLOT, emptyBank(), false);
    helper.assertValueEqual(refused.getCount(), ONE, WRONG_AUTOMATION);
    handler.insertItem(FIRST_STORAGE_HANDLER_SLOT, emptyBank(), false);
    handler.insertItem(FIRST_STORAGE_HANDLER_SLOT + ONE, toolbox(), false);

    helper.assertTrue(
        handler.extractItem(FIRST_STORAGE_HANDLER_SLOT, ONE, false).isEmpty(), WRONG_AUTOMATION);
    helper.assertFalse(
        handler.extractItem(FIRST_STORAGE_HANDLER_SLOT + ONE, ONE, false).isEmpty(),
        WRONG_AUTOMATION);
    dock.storage().setItem(FIRST, fullBank());
    helper.assertFalse(
        handler.extractItem(FIRST_STORAGE_HANDLER_SLOT, ONE, false).isEmpty(), WRONG_AUTOMATION);
    helper.succeed();
  }

  /** The storage stays in the item of the dock and comes back with it (answer 954). */
  @GameTest(template = ARENA)
  public static void theItemOfTheDockKeepsTheStorage(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = dock(helper, PLENTY);
    dock.storage().setItem(SECOND, toolbox());
    ItemStack item = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    item.applyComponents(dock.collectComponents());
    helper.assertTrue(item.has(CubitDataComponents.DOCK_STORAGE.get()), NOT_IN_ITEM);

    helper.destroyBlock(DockScene.DOCK);
    GameTestRobots.use(helper, owner, item, DockScene.FLOOR);
    InductionDockBlockEntity placed = helper.getBlockEntity(DockScene.DOCK);

    helper.assertTrue(placed.storage().getItem(SECOND).is(CubitItems.TOOLBOX.get()), LOST);
    helper.succeed();
  }

  /** A dock saved with the old charge slot moves its bank to the first slot (answer 955). */
  @GameTest(template = ARENA)
  public static void theBankOfTheOldChargeSlotMovesToTheStorage(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, PLENTY);
    HolderLookup.Provider registries = helper.getLevel().registryAccess();
    CompoundTag saved = dock.saveWithoutMetadata(registries);
    saved.remove(STORAGE_KEY);
    saved.put(CHARGE_SLOT_KEY, fullBank().save(registries));

    dock.loadWithComponents(saved, registries);

    helper.assertTrue(PowerBankItem.is(dock.storage().getItem(FIRST)), LOST);
    helper.assertFalse(dock.saveWithoutMetadata(registries).contains(CHARGE_SLOT_KEY), LOST);
    helper.succeed();
  }

  /** An item the storage does not keep, from an edited save, drops beside the dock (rule 17). */
  @GameTest(template = ARENA)
  public static void aRefusedItemOfAnEditedSaveDrops(GameTestHelper helper) {
    InductionDockBlockEntity dock = dock(helper, PLENTY);
    HolderLookup.Provider registries = helper.getLevel().registryAccess();
    CompoundTag saved = dock.saveWithoutMetadata(registries);
    CompoundTag dirt = new CompoundTag();
    dirt.putByte(SLOT_KEY, (byte) FIRST);
    ListTag list = new ListTag();
    list.add(new ItemStack(Items.DIRT).save(registries, dirt));
    saved.put(STORAGE_KEY, list);

    dock.loadWithComponents(saved, registries);

    helper.assertTrue(dock.storage().isEmpty(), WRONG_ITEM);
    helper.runAfterDelay(
        DROP_TICKS,
        () -> {
          List<ItemEntity> dropped =
              helper
                  .getLevel()
                  .getEntitiesOfClass(ItemEntity.class, new AABB(dock.getBlockPos()).inflate(ONE));
          helper.assertTrue(
              dropped.stream().anyMatch(item -> item.getItem().is(Items.DIRT)), NOT_DROPPED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aDockWithoutTheComponentHasAnEmptyStorage(GameTestHelper helper) {
    helper.assertTrue(dock(helper, PLENTY).storage().isEmpty(), WRONG_ITEM);
    helper.succeed();
  }

  private static InductionDockBlockEntity dock(GameTestHelper helper, Energy energy) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    return DockScene.placeDock(helper, owner, DockScene.FLOOR, energy);
  }

  private static ItemStack toolbox() {
    return new ItemStack(CubitItems.TOOLBOX.get());
  }

  private static ItemStack robot() {
    return DockScene.robotItem(PLENTY);
  }

  private static ItemStack damagedRobot() {
    return new ItemStack(CubitItems.DAMAGED_GENERAL_ROBOT.get());
  }

  private static ItemStack emptyBank() {
    return CubitItems.BASE_POWER_BANK.get().getDefaultInstance();
  }

  private static ItemStack fullBank() {
    ItemStack bank = emptyBank();
    PowerBankItem.store(bank, new Battery(BASE_CAPACITY, BASE_CAPACITY));
    return bank;
  }

  private static Energy chargeAt(InductionDockBlockEntity dock, int index) {
    return PowerBankItem.batteryOf(dock.storage().getItem(index)).charge();
  }
}
