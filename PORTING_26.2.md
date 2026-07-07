# Farmer's Delight —— NeoForge 26.1 → 26.2 移植计划

> 分支：`26.2`（由上游 `AyOhEe/FarmersDelight` 的 `26.1` 分支 fork + 改名而来，已设为默认分支）
> 起点：MC 26.1.2 / NeoForge 26.1.2.30-beta / Java 25 / moddev 2.0.141 / Gradle 9.4.1
> 目标：MC 26.2 / NeoForge 26.2.0.7-beta / Gradle 9.6.1

## 现状评估

26.1 分支已经相当现代化（Java 25、item component、network payload、moddev 2.0.141），
所以 26.1 → 26.2 是**版本号提升 + API 漂移修复**，不是重写。

代码规模：
- `src/main/java` 260 个文件
- 功能 mixin 暂时全部禁用 + datafix mixin 2 个（`ItemStackComponentizationFixMixin` / `V1460Mixin`）
- 18 个文件使用 `@EventBusSubscriber`
- datagen 完整：recipe builder / BlockStates / BlockTags / Advancements / loot

## 阶段划分

### M1 —— 构建配置提升 ✅（已完成，版本均经 maven metadata 查证）
- [x] `gradle.properties`：
  - `minecraft_version` 26.1.2 → **26.2**（range `[26.2,26.3)`）
  - `neo_version` → **26.2.0.8-beta**（当前 26.2 线最新），`neo_version_range` 同步
  - `jei_minecraft` → **26.2**，`jei_version` → **30.7.0.39**
  - `appleskin_version` → **mc26.2-3.0.10**
- [x] `gradle/wrapper/gradle-wrapper.properties`：`gradle-9.4.1` → **9.6.1**
- [x] `build.gradle`：moddev **2.0.141 已是最新**，不改；Parchment 保持禁用
- [ ] CI 跑一次确认依赖可解析（neoForge/JEI/AppleSkin）→ 编译错误即 M2 清单

### M2 —— 编译期 API 漂移修复（主体工作量）
按已知 26.1→26.2 破坏点排查（编译错误驱动）：
- [ ] **`@EventBusSubscriber` 去 bus 参数**：1.26 总线合并，删掉 `bus = EventBusSubscriber.Bus.MOD`，只留 `value = Dist.CLIENT` 之类（18 处）
- [ ] **`ItemStack.save()` 相关**：26.2 已删部分序列化 API，datafix mixin 首当其冲
- [ ] **datafix mixin 目标签名核对**：`ItemStackComponentizationFixMixin` / `V1460Mixin` / `V3818_3Mixin` 注入点可能漂移，逐个核对目标方法
- [ ] 客户端渲染 / GlStateManager 缓存类 API（若有引用）
- [ ] 映射改名残留核对（`ResourceLocation`→`Identifier` 等在 26.1 应已完成，26.2 复查）

### M3 —— datagen / recipe 数据驱动化（26.1→26.2 已知大破坏）
- [ ] recipe datagen 在 26.2 有数据驱动化改动（染料/混凝土/铜等参照 MI+GuideME 移植经验）
- [ ] 重新跑 `runData`，diff `src/generated/resources` 与旧产物，处理 schema 变化
- [ ] CuttingBoard / CookingPot 自定义 recipe serializer 复查（RecipeSerializer 可能 final 化 / 重命名）

### M4 —— 运行期冒烟
- [ ] client 启动进主菜单 → 进世界
- [ ] 核心方块：烹饪锅 / 切菜板 / 篝火烧烤 交互
- [ ] JEI 集成显示自定义配方
- [ ] datafix：旧存档载入不崩

## 已知 26.2 踩坑速查（来自其它 mod 移植经验）
| 症状 | 根因 | 处理 |
|---|---|---|
| `EventBusSubscriber.Bus` 找不到 | 1.26 总线合并 | 删 `bus=` 参数 |
| `ItemStack.save()` 报错 | 26.2 删该 API | 换 component / codec 路径 |
| RecipeSerializer 报 final | 26.2 密封 | 改用官方注册入口 |
| Gradle 版本不符 | 需 9.6.1 | 改 wrapper |
| Java 版本 | MC 26.2 要 Java 25 | 已满足 |

## 进度快照（2026-07-07 凌晨，CI 驱动）

**这个 26.1 分支本身是上游一个未完成的半成品移植**（提交历史全是移植 commit，代码里有真实 bug：`compoundRecipes` 未定义、`super.saveAdditional(compound)` 的 `compound` 未定义等）。所以本质是**接力完成一个半成品移植**，不是干净版本 bump。

编译错误数：初始 **1035** → **0，CI 编译通过 ✅**（`build.yml` 产出 `FarmersDelight-jars` 工件，~2.5MB 可下载）。尚未做运行期冒烟（M4）。

### 已完成 ✅
- **M1 构建配置**：版本全部提升并 CI 验证可解析。
- **Gradle wrapper 修复**：仓库里的 `gradlew`/`gradlew.bat` 被改坏（`-jar` + 空 CLASSPATH），已恢复标准 `GradleWrapperMain` 启动方式。
- **CI**：`build.yml`（JDK25/上传 jar）+ `diagnose.yml`（`createMinecraftArtifacts` 后 grep/javap 反编译 jar 拿真实 API，**不猜**）。
- **注解**：删除全部已不存在的 `MethodsReturnNonnullByDefault`（`@NullMarked` 已覆盖）。
- **advancements 包拆分**：`critereon`→`triggers`/`predicates`/`predicates.entity`，全部 import 已改。
- **ModData**：`client.model.data`→`model.data`。
- **datagen（M3）整体排除编译**：26.2 重写了 model/tag/recipe/loot 全套 datagen（NeoForge 删 model generators+ExistingFileHelper，vanilla tag/recipe provider 大改）。`data/**` 自包含、生成的 JSON 已在 `src/generated/resources` 打进 jar，故**运行时不受影响**。见 `build.gradle` 的 `sourceSets.main.java.exclude`。约消除 800 错。
- **CookingPot 生态**：BlockEntity 退回稳定的旧 `ItemStackHandler`/`IItemHandler` API（内部逻辑本就是旧 API 写的）；序列化改成 26.2 的 `ValueInput/ValueOutput`（`serialize(output.child())`/`getIntOr`/`ItemStack.OPTIONAL_CODEC`/`ComponentSerialization.CODEC`/`Codec.unboundedMap` 存 RecipesUsed）；Menu 字段改 `IItemHandler`；`getCraftingRemainingItem`→`getCraftingRemainder`、`assemble` 去多余 registries 参、`recipe.id().location()`、`getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, id))`。

### 关键决策
1. **BlockEntity 用稳定旧 `ItemStackHandler` API**：26.2 的新 transfer API（`ResourceHandler<ItemResource>`+事务）是移动的 beta 目标，旧 `ItemStackHandler`/`IItemHandler` 仍完整可用且带 `serialize(ValueOutput)`。CookingPot 逻辑本就旧 API，退回最省最稳。（注：CuttingBoard/Skillet/Stove 上游已完整迁到新 API 且能编译，保留原样。）
2. **Capability 暴露暂时中和**：`Capabilities.Item.BLOCK` 在 26.2 要 `ResourceHandler<ItemResource>`，IItemHandler→ResourceHandler 无现成公开桥。CookingPot 的 `registerCapabilities` 已注释（世界内功能完好，仅漏斗自动 I/O 延后）。**待办**：把 sided `CookingPotItemHandler` 写成 `ResourceHandler` 适配器再恢复。

### ⚠️ 为编译通过而"延后/禁用"的功能（M4 需恢复+验证，核心玩法不受影响）
1. **所有方块的 item-handler capability 暴露**（漏斗/管道自动进出）——26.2 `Capabilities.Item.BLOCK` 改要 `ResourceHandler<ItemResource>`，`IItemHandler` 无现成公开桥。CookingPot、Basket 的 `registerCapabilities` 已注释。**恢复法**：写 `IItemHandler → ResourceHandler<ItemResource>` 适配器（含事务）。`BasketInvWrapper.java` 已排除编译。
2. **canvas sign 自定义渲染**——26.2 sign 渲染重构（`SignRenderer.createSignModel`/`Sheets.SIGN_SHEET`/`SpriteMapper` 全删）。`CanvasSignRenderer`/`HangingCanvasSignRenderer`/`CanvasSignEditScreen`/`HangingCanvasSignEditScreen`/`ModAtlases` 排除编译，BER 注册+2 client mixin 已移出。方块仍在只是不渲染画。
3. **datagen 全套**（`data/**` 排除）——生成资源已打包，运行时无影响；改配方/模型需迁到 vanilla `client.data.models`。
4. **零碎降级**（代码里都有 `M-client`/`M2` 注释）：CookingPot 配方书 ghost 预览/高亮（`selectMatchingRecipes`/`fillGhostRecipe` 空实现）、HUD 血饿条动态偏移（暂固定 0）、SafetyNet 落地弹跳、Stove 用自身格光照。

### 运行期修复（M4 冒烟发现）
- **V3818_3Mixin 移除**：`@ModifyArg` 目标 `lambda$registerTypes$0` 在 26.2 里 lambda 编号变了（前置 lambda 增减导致合成方法重排），注入 0/1 失败。这是 DataFixer 自定义组件类型注册，仅旧存档迁移需要；新装 mod 不需要，直接从 `farmersdelight.mixins.json` 移除。若将来需要旧存档兼容，用 `diagnose.yml` 的 `javap -p net.minecraft.util.datafix.schemas.V3818_3` 拿到真实 lambda 编号再恢复。
- **KeepRichSoilTreeMixin + KeepRichSoilGiantTreeMixin 移除**：`TrunkPlacer.setDirtAt` 在 26.2 被重命名/移除（`TreeGrower` 初始化时触发 mixin 注入失败）；`Feature.isGrassOrDirt` 同理可能漂移。这两个 mixin 保护富土壤不被树生成覆盖，非核心玩法，先禁用。恢复法：`diagnose.yml` 的 `javap -p` 找到 26.2 的 `TrunkPlacer` 真实方法签名。
- **全部非 datafix mixin 暂时禁用**：`VillagersTargetRichSoilMixin`（`SecondaryPoiSensor.doTick` 签名变）、`CampfireBaleMixin`、`CuttingBoardDispenserMixin`、`KeepRichSoilUntrampledMixin`、`NourishmentAlwaysEatMixin`、`PlacePumpkinPieMixin`、`RopeFenceConnectionMixin` 一并禁用，先让游戏能启动。逐个用 `diagnose.yml` 的 `javap -p` 核实 26.2 真实签名后恢复。
- **ModItems eager `.get()` 修复**：26.2 的 `DeferredHolder.get()` 在注册完成前抛 NPE（以前返回 null）。`ModItems` 静态初始化时 `ModBlocks.STOVE.get()` 触发。修复：`registerBlockWithTab`/`registerFuelBlockWithTab` 参数改 `Supplier<Block>`，`.get()` 延迟到注册 lambda 内。

### 已修的运行时 API 漂移（核心，已编译通过）
CookingPot 全家 + Menu/Slots/Block；`Blocks.WOOL.pick(DyeColor)`；`Vec3.atCenterOf(pos)`；`getCraftingRemainder().create()`（返回 ItemStackTemplate）；`recipe.id().identifier()`；`onCraftedBy(Player,int)`；advancements 包拆分；ValueIO 序列化；`ItemHandlerHelper.calcRedstoneFromInventory`；`AddTableLootModifier` 3 参构造 + `resolver.get(ResourceKey)`。

### 诊断方法（可复用）
`diagnose.yml`（workflow_dispatch）：跑 `createMinecraftArtifacts` → 把所有 gradle 缓存 jar 的类路径 grep 成包树、对关键类 `javap -p` 出方法签名。**这是拿 26.2 真实 API 的正道，别猜**。

## M4 —— 运行期冒烟（编译绿后）
- [ ] client 启动进主菜单 → 进世界
- [ ] 核心方块：烹饪锅 / 切菜板 / 篝火烧烤 交互
- [ ] JEI 集成显示自定义配方
- [x] datafix：V3818_3Mixin 已移除（lambda 编号漂移），其余两个待验证
- [ ] 恢复 capability 暴露后测漏斗 I/O
- [ ] datagen 重写到 vanilla `net.minecraft.client.data.models` 后 `runData` diff

## 约束
- 提交不署 Claude 名
- 遇到不确定的 API 去 NeoForge 26.2 反编译源 / diagnose.yml 核对，不猜
