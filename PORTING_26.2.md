# Farmer's Delight —— NeoForge 26.1 → 26.2 移植计划

> 分支：`26.2`（由上游 `AyOhEe/FarmersDelight` 的 `26.1` 分支 fork + 改名而来，已设为默认分支）
> 起点：MC 26.1.2 / NeoForge 26.1.2.30-beta / Java 25 / moddev 2.0.141 / Gradle 9.4.1
> 目标：MC 26.2 / NeoForge 26.2.0.7-beta / Gradle 9.6.1

## 现状评估

26.1 分支已经相当现代化（Java 25、item component、network payload、moddev 2.0.141），
所以 26.1 → 26.2 是**版本号提升 + API 漂移修复**，不是重写。

代码规模：
- `src/main/java` 260 个文件
- 功能 mixin 13 个 + datafix mixin 3 个（`ItemStackComponentizationFixMixin` / `V1460Mixin` / `V3818_3Mixin`）
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

## 约束
- 用户自行编译构建，AI 不代跑 gradle
- 提交不署 Claude 名
- 遇到不确定的 API 去 NeoForge 26.2 MDK / 反编译源核对，不猜
