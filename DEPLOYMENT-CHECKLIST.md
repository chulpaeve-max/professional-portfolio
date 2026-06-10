# רשימת בדיקה להעלאה 📋

## לפני ההעלאה ✅

- [ ] Profile.pdf קיים בשורש הפרויקט
- [ ] יש לך מפתח OpenRouter API
- [ ] יש לך חשבון GitHub
- [ ] Git מותקן במחשב

## שלב 1: GitHub 🐙

- [ ] יצרת repository חדש ב-GitHub (Public)
- [ ] העלית את הקוד:
  ```powershell
  git init
  git add .
  git commit -m "Initial commit"
  git remote add origin https://github.com/USERNAME/REPO.git
  git push -u origin main
  ```

## שלב 2: Render.com 🚀

- [ ] יצרת חשבון ב-render.com
- [ ] חיברת את GitHub
- [ ] יצרת Web Service חדש
- [ ] בחרת את ה-repository
- [ ] הגדרות:
  - [ ] Name: portfolio-website
  - [ ] Region: Frankfurt
  - [ ] Branch: main
  - [ ] Build Command: `./mvnw clean package -DskipTests`
  - [ ] Start Command: `java -jar target/professional-website-1.0.0.jar`
- [ ] הוספת Environment Variables:
  - [ ] OPENROUTER_API_KEY = המפתח שלך
- [ ] בחרת תוכנית Free
- [ ] לחצת "Create Web Service"

## שלב 3: בדיקה 🧪

- [ ] הבנייה הסתיימה בהצלחה
- [ ] האתר עולה (ראית "Started PortfolioApplication" בלוגים)
- [ ] דף הבית נטען
- [ ] השם שלך מוצג נכון
- [ ] דף Career מציג את החברות
- [ ] דף About מציג את ההשכלה
- [ ] AI Chat עובד

## עדכונים עתידיים 🔄

כדי לעדכן את האתר:
```powershell
git add .
git commit -m "תיאור השינוי"
git push
```

Render יפרוס אוטומטית! ✨

## קישורים שימושיים 🔗

- GitHub: https://github.com
- Render: https://render.com
- Render Docs: https://render.com/docs
- OpenRouter: https://openrouter.ai

## פתרון בעיות 🔧

### הבנייה נכשלת
1. בדקי את הלוגים ב-Render
2. ודאי ש-Profile.pdf קיים
3. ודאי ש-mvnw.cmd קיים

### AI Chat לא עובד
1. בדקי ש-OPENROUTER_API_KEY מוגדר
2. בדקי שהמפתח תקף
3. בדקי את הלוגים לשגיאות

### האתר איטי
- זה נורמלי בתוכנית החינמית
- הגישה הראשונה אחרי 15 דקות של חוסר פעילות תקח זמן
- השרת "מתעורר" ואז עובד מהר

---

**זקוקה לעזרה?** ראי את DEPLOYMENT.md למדריך מפורט!
