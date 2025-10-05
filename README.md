LNCT Club App

LNCT Club App is a mobile application designed to digitally manage student clubs at LNCT (Laxmi Narayan College of Technology). It allows students to register, view, and participate in various club activities while providing club administrators tools to manage members, events, and notifications efficiently.

Table of Contents

Features

Architecture

Screenshots

Installation

Usage

Technologies Used

Contributing

License

Contact

Features

Student Registration & Login – Secure login for existing users and easy registration for new users.

Club Management – Clubs can create and manage their profiles and activities.

Event Notifications – Real-time updates for upcoming events and vacancies.

Offline Functionality – Some features are available offline to ensure accessibility even without internet.

Modular Architecture – Easily extendable to add new clubs, features, or notifications.

Admin Panel – Manage users, approve club requests, and send announcements.

Architecture

The LNCT Club App follows a modular architecture with two main flows:

Offline Flow (Brown) – Demonstrates how core features work without an internet connection.

Online Flow (Blue) – Illustrates how features function when connected to the internet, including real-time updates.

Data Flow Diagram:

graph TD
    A[User] -->|Login/Register| B[App]
    B --> C[Offline Flow]:::brown
    B --> D[Online Flow]:::blue
    C --> E[Local Storage]
    D --> F[Cloud Database]
    F --> G[Notifications]
    
    

Screenshots
Login Screen	Club Dashboard	Event Notifications

	
	
Installation

Clone the repository:

 git clone https://github.com/iaditya-DA/LNCT_CLUB.git



Open in Android Studio:

Open LNCTClubApp folder in Android Studio

Let Gradle sync all dependencies

Run the app:

Connect an Android device or use an emulator

Click Run

Usage

Launch the app and register or log in.

Browse through the list of clubs.

Join a club or participate in events.

Receive real-time notifications for upcoming events.

Clubs can create events, post updates, and manage members from their admin dashboard.

Technologies Used

Frontend: Android (Kotlin, XML,Dagger hilt)

Backend: Firebase Authentication, Firebase Firestore, Cloud Functions

Libraries: Glide, Retrofit, Firebase SDK

Contributing

We welcome contributions from the community!

Fork the repository.

Create a feature branch: git checkout -b feature/YourFeature

Commit your changes: git commit -m "Add new feature"

Push to the branch: git push origin feature/YourFeature

Open a pull request.

Please make sure to follow coding standards and document any new features.

👨‍💻 Developer

Developed by: Aditya Kr Jha

Project Type: Major Android App (LNCT College)
Language: Kotlin + Firebase
Status: 🚧 In Development

Contact
Aditya Kr Jha
Email: adityakumarrjhaa@gmail.com

LinkedIn: https://www.linkedin.com/in/aditya-kumar-jha-a6750129a/
