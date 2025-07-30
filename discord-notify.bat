@echo off
set WEBHOOK_URL=https://discord.com/api/webhooks/1400071331470377080/VLvk7INu2kxW_WltG5AcootWXaNplC4w9r97MzeQiDvKRIpPCuj2DkjyPZjMB6q9mXdB

REM Lấy commit message của commit cuối cùng
for /f "tokens=*" %%i in ('git log -1 --pretty=format:"%%s"') do set COMMIT_MSG=%%i

REM Gửi POST request lên webhook Discord
curl -H "Content-Type: application/json" -X POST -d "{\"content\": \"New commit: %COMMIT_MSG%\"}" %WEBHOOK_URL%
