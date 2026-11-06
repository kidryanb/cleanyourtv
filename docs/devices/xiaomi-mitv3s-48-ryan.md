# 小米电视 3S 48 英寸实测记录

本记录来自一台真实电视的操作，适用范围仅限下列设备与固件。测试仍在进行，未验证项目不代表已通过。

| 项目 | 实际读取结果 |
|---|---|
| 型号 | MiTV3S-48 |
| 设备代号 | hancock |
| 用户界面版本 | MIUI TV 1.3.37，由用户提供 |
| Android | 4.4.4，API 19 |
| 固件 | 7.7.21.1653 |
| ABI | armeabi-v7a |

## 桌面

CleanYourTV 参考桌面要求 API 26，不能直接安装在这台 API 19 电视上。本次使用独立项目 [Simple TV Launcher 2.0.2](https://github.com/alescdb/LauncherTV/releases/tag/v2.0.2)。它不是 CleanYourTV 的官方桌面。

安装前用 Android SDK 的 aapt 检查官方 APK，最低 API 为 14；SHA256 与发布资产一致：

```text
663bd8990ee1b700df4e21c56173d9c43e492295f0425ac341a8b1a9bc2f49c4
```

APK 成功安装，启动后已检查实际截图，中文标签正常。首页配置为云视听小电视、云视听极光、小米电视设置、无线投屏；左上角保留全部应用入口。

通过以下系统页面选择默认桌面：

```sh
adb -s <tv-ip>:5555 shell am start -n com.android.settings/.Settings --es :android:show_fragment com.android.settings.HomeSettings
```

系统页面显示新桌面已选中，但 Home 键仍返回原厂桌面。直接发送标准 HOME intent 也被引向原厂组件，并报 HOME_STATE 权限错误。先明确启动并确认新桌面处于前台，再可逆隐藏原厂桌面后，ADB 注入的 Home 键成功返回新桌面。实体遥控器 Home 键仍待用户验证。

## 本次实际改动与恢复

操作前已在本地保存包状态、禁用列表、设备属性及系统设置，原始备份不上传。没有删除 APK 或清除应用数据。

实际执行的隐藏操作，每个命令单独运行：

```sh
adb -s <tv-ip>:5555 shell pm block --user 0 com.mitv.tvhome
adb -s <tv-ip>:5555 shell pm block --user 0 com.miui.systemAdSolution
```

两项均返回 blocked state 为 true。com.xiaomi.mitv.advertise 在操作前已经禁用，保持原状。其他视频应用、系统设置、输入法、网络、蓝牙、MStar 服务、电视播放器与投屏组件未改动。

只恢复本次两项改动：

```sh
adb -s <tv-ip>:5555 shell pm unblock --user 0 com.mitv.tvhome
adb -s <tv-ip>:5555 shell pm unblock --user 0 com.miui.systemAdSolution
adb -s <tv-ip>:5555 shell am start -n com.android.settings/.Settings --es :android:show_fragment com.android.settings.HomeSettings
```

选择原厂桌面并验证实体 Home 键。恢复原厂桌面前应保留替代桌面。以上恢复流程已准备，但本次尚未实测回退；不要批量启用原本已经禁用的包。

## 验证状态

- 已验证：APK 安装、桌面启动、中文显示、四个应用入口配置、隐藏原厂桌面后的 ADB Home 跳转。
- 用户已确认：执行软件重启后，电视自动进入黑色简洁桌面。
- 重启后已验证：标准 HOME intent 返回 Simple TV Launcher；原生信号源弹窗可打开，实际截图显示 HDMI1、HDMI2 等入口。仅证明菜单可用，HDMI 音视频仍待验证。
- 用户已确认：本次软件重启没有播放开机广告。这是一次观察，不代表所有后续冷启动均无广告。
- 已恢复：重启后的 ADB 曾短暂 offline、拒绝连接；用户操作设置页后重新连接成功，sys.boot_completed 为 1。不能据此判断重启是否自动关闭了调试。
- 待验证：实体遥控器、待机唤醒、正常关机后开机、HDMI 画面和声音、手机投屏、视频实际播放。

隐藏广告服务不等于已经证明所有开机广告消失。本次结果也不证明其他型号或固件兼容。
