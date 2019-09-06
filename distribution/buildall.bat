cd ..
call gradlew :desktop:build
call gradlew :desktop:distZip
cd distribution
echo "Starting build, zip, and upload process"
START "Windows Build" cmd /c "java -jar packr.jar config-win.json && 7z a windows64.zip -r out-win64\ && gdrive-windows-x64.exe update 1vytawSRs-p10JJqmPw07JjIlpRJ0NGfG windows64.zip && call SystemTrayNotification.bat -tooltip warning -time 3000 -title "Finished" -text "Windows Build/Upload" -icon question"
START "Mac Build" cmd /c "java -jar packr.jar config-mac.json && 7z a mac.zip -r out-mac\ && gdrive-windows-x64.exe update 16yW9xtR4GlOe3Ja7f_8aycsy7MCVo_QC mac.zip && call SystemTrayNotification.bat -tooltip warning -time 3000 -title "Finished" -text "Mac Build/Upload" -icon question"
pause