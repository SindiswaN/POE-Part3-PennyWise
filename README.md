# README file
PennyWise – Personal Budget Tracker
PennyWise is an innovative Android budgeting application designed to help users track expenses, manage budgets, and gain financial insights. Developed in Kotlin with Firebase integration, the app prioritizes real-time data, usability, and intelligent financial management.

**👩‍💻 Student Details**

•	Name:

SINDISWA NOMAKHOLWA MADLIWA ST10240725

SINAZO HAPPY MGIDI ST10220472

•	Module: OPSC6311

•	Project: Portfolio of Evidence – PennyWise Budget Tracker

**📱 App Features Overview**

✔ User Authentication

  •	Sign up / Login via Firebase Authentication.

✔ Expense Tracking

  •	Add, categorize, and review expenses.
  
  •	Filter by date, type, and amount.
  
✔ Budget Categories

  •	Create and manage custom spending categories.
  
✔ Visual Analytics

  •	Spending vs Budget graphs using custom views.
  •	Monthly summaries and breakdowns.
  
✔ Category & Entry Storage

•	Stored securely in Firebase Realtime Database.

**🚀 Key Innovations**

*1.	☁ Cloud Synchronization (Firebase Realtime Database)*
   
•	All app data (entries, categories, goals) is synced in real-time across devices.

•	User data is stored under their UID, making it easy to backup and restore upon login from any Android device.

•	Ensures a seamless multi-device experience.

*3.	🔔 Budgeting Alerts*

•	Users are encouraged to set monthly budgeting goals.

•	If their spending approaches or exceeds their budget, visual cues and toast messages are triggered.

•	Upcoming versions will include push notifications as proactive reminders.

**🧪 What to Expect When Marking**

Once you install and run the app:

1.	Register a new account or log in with an existing test Firebase account.

3.	On the dashboard:

o	Add categories (e.g., Food, Transport, Entertainment).

o	Add expenses with relevant type/category and amounts.

5.	Navigate to the “Spending Graph” screen:
   
o	You'll see a graph comparing actual spending per category.

o	If no data is available, the app shows “No spending data found”.

7.	Click “View Detailed Report”:
   
o	Currently displays a placeholder toast: “View details not implemented yet”.

9.	Log out and log in on another emulator or device:
    
o	Your previous data will be restored thanks to Firebase cloud sync.

**📁 Folder Structure**

•	/app/src/main/java/vcmsa/projects/sindiswasinazo/...

o	ui/ → Activities (DashboardActivity, SpendingGraphActivity, etc.)

o	ui/views/ → CustomGraphView

•	/res/layout/ → All XML UI layouts

•	/res/values/strings.xml → String resources

•	/AndroidManifest.xml

**🛠 Tech Stack**

•	Kotlin

•	Android SDK (minSdk 23)

•	Firebase Authentication

•	Firebase Realtime Database

**📌 Known Issues / Future Enhancements**

•	“View Detailed Report” button currently unimplemented.

•	Push alerts for over-budget notifications to be added.

•	Improved offline caching and biometric login options planned.

**📹 YouTube Demonstration Video**  

•	▶️ https://youtu.be/YOUR_DEMO_VIDEO_LINK

**🗂 GitHub Repository**

•	📂 https://github.com/SindiswaN/POE-Part3-PennyWise.git

**📬 Contact**

For questions related to this project, contact:

SINDISWA NOMAKHOLWA MADLIWA st10240725@inconnect.edu.za

SINAZO HAPPY MGIDI st10220472@inconnect.edu.za


