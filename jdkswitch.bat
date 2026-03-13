@echo off
setlocal enabledelayedexpansion

REM JDK 安装目录
set JDK_DIR=C:\Program Files\Java

echo 检测到以下 JDK 版本：
set /a index=0
for /d %%i in ("%JDK_DIR%\jdk*") do (
    set /a index+=1
    set "jdk[!index!]=%%i"
    echo !index!. %%i
)

if %index%==0 (
    echo 没有找到任何 JDK 安装，请检查 %JDK_DIR% 路径。
    exit /b 1
)

set /p choice=请输入要切换的 JDK 序号 (1-%index%): 

if "%choice%"=="" (
    echo 未输入序号，退出。
    exit /b 1
)

if %choice% GTR %index% (
    echo 输入的序号超出范围。
    exit /b 1
)

set "JAVA_HOME=!jdk[%choice%]!"
echo 正在切换到: %JAVA_HOME%

REM 设置环境变量
setx JAVA_HOME "%JAVA_HOME%"
setx PATH "%JAVA_HOME%\bin;%PATH%"

echo 已切换到 %JAVA_HOME%
echo 请重新打开命令行窗口后运行 java -version 验证。
