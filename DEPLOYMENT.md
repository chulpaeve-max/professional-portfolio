# מדריך העלאה ל-Render.com

## דרישות מוקדמות
1. חשבון GitHub (חינמי)
2. חשבון Render.com (חינמי)
3. Git מותקן במחשב

---

## שלב 1: העלאה ל-GitHub

### 1.1 צור repository חדש ב-GitHub
1. היכנסי ל-https://github.com
2. לחצי על "New repository" (הכפתור הירוק)
3. תני שם ל-repository (למשל: `portfolio-website`)
4. בחרי **Public** (חובה לתוכנית החינמית של Render)
5. **אל תסמני** "Initialize with README"
6. לחצי "Create repository"

### 1.2 העלי את הקוד
פתחי PowerShell בתיקיית הפרויקט והריצי:

```powershell
# אתחול Git (אם עוד לא עשית)
git init

# הוספת כל הקבצים
git add .

# יצירת commit ראשון
git commit -m "Initial commit - Portfolio website"

# חיבור ל-GitHub (החליפי USERNAME ו-REPO-NAME)
git remote add origin https://github.com/USERNAME/REPO-NAME.git

# העלאה ל-GitHub
git branch -M main
git push -u origin main
```

**חשוב:** אל תשכחי להחליף `USERNAME` ו-`REPO-NAME` בכתובת שלך!

---

## שלב 2: פריסה ב-Render.com

### 2.1 יצירת חשבון
1. היכנסי ל-https://render.com
2. לחצי "Get Started for Free"
3. התחברי עם חשבון GitHub שלך

### 2.2 יצירת Web Service
1. לחצי על "New +" בפינה הימנית העליונה
2. בחרי "Web Service"
3. חברי את ה-GitHub repository שלך:
   - אם זו הפעם הראשונה: לחצי "Connect GitHub"
   - אחרת: בחרי את ה-repository מהרשימה

### 2.3 הגדרות Service
מלאי את הפרטים הבאים:

- **Name**: `portfolio-website` (או כל שם שתרצי)
- **Region**: בחרי את האזור הקרוב ביותר (Frankfurt לישראל)
- **Branch**: `main`
- **Root Directory**: השאירי ריק
- **Runtime**: Render יזהה אוטומטית "Java"
- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -jar target/professional-website-1.0.0.jar`

### 2.4 הגדרות סביבה (Environment Variables)
לחצי על "Advanced" והוסיפי:

1. **OPENROUTER_API_KEY**
   - Value: המפתח שלך מ-.env

2. **JAVA_TOOL_OPTIONS** (אופציונלי)
   - Value: `-Xmx512m`

### 2.5 תוכנית חינמית
- גללי למטה ל-"Instance Type"
- בחרי **"Free"** (750 שעות חינם בחודש)

### 2.6 פריסה
1. לחצי "Create Web Service"
2. Render יתחיל לבנות ולפרוס את האתר
3. זה ייקח כ-5-10 דקות

---

## שלב 3: בדיקה

### 3.1 המתן לסיום הבנייה
- תראי לוגים בזמן אמת
- חפשי את ההודעה: "Started PortfolioApplication"

### 3.2 גישה לאתר
- Render ייתן לך URL כמו: `https://portfolio-website-xxxx.onrender.com`
- לחצי על ה-URL או העתיקי אותו לדפדפן

---

## שלב 4: עדכונים עתידיים

כל פעם שתרצי לעדכן את האתר:

```powershell
# ערכי את הקבצים שלך
# אז:

git add .
git commit -m "תיאור השינויים"
git push
```

Render יזהה את השינוי ויפרוס אוטומטית! 🚀

---

## פתרון בעיות נפוצות

### הבנייה נכשלת
- בדקי את הלוגים ב-Render
- ודאי ש-`mvnw` ו-`mvnw.cmd` קיימים בפרויקט
- ודאי ש-Profile.pdf קיים בשורש הפרויקט

### האתר לא עולה
- בדקי שה-Start Command נכון
- ודאי שהפורט הוא 8080
- בדקי את הלוגים לשגיאות

### AI Chat לא עובד
- ודאי שהוספת את OPENROUTER_API_KEY ב-Environment Variables
- בדקי שהמפתח תקף

### האתר "ישן" אחרי 15 דקות
- זה נורמלי בתוכנית החינמית
- הגישה הראשונה תקח 30-60 שניות להעיר את השרת
- אפשר לשדרג ל-Paid plan אם צריך

---

## טיפים

1. **Custom Domain**: אפשר להוסיף דומיין משלך (חינמי) בהגדרות ה-Service
2. **HTTPS**: Render מספק HTTPS אוטומטית
3. **Logs**: תמיד אפשר לראות לוגים ב-Render Dashboard
4. **Restart**: אפשר לעשות restart ידני אם צריך

---

## תמיכה

אם יש בעיות:
- Render Docs: https://render.com/docs
- Render Community: https://community.render.com
