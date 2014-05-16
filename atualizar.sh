now=$(date '+%d/%m/%Y %H:%M')
git add .
git commit -a -m "Version $now"
git push origin master --force
