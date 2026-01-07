🛍️ Zenvy – Modern Android E-Commerce App

Zenvy is a modern, portfolio-grade Android e-commerce application built using the latest Android development best practices.
The project focuses on clean architecture, real-time data handling, smooth UI/UX, and production-level problem solving.

This app was built to demonstrate professional Android engineering skills, not just feature implementation.

✨ Features
🔐 Authentication

Email & Password authentication using Firebase Auth

Secure login & logout flow

State-driven navigation (no UI hacks)

🛒 Product Browsing

Real-time product listing from Firebase Firestore

Live updates without app restart

Category-based product display

Clean, modern product cards

❤️ Wishlist & 🛍️ Cart

Add / remove products from wishlist

Add products to cart

Offline persistence using Room Database

Quantity management & total price calculation

🔍 Search

End-to-end product search

Firestore-backed querying

Debounced input handling

Proper empty & loading states

🖼️ Image Loading

Product images loaded via Coil

Placeholder & error handling

Optimized image rendering

Real-device tested (not emulator-only)

🎨 UI & UX

Built entirely with Jetpack Compose

Material 3 design system

Edge-to-edge UI with proper system inset handling

Fully responsive on real devices

Smooth scrolling & navigation

🧠 Architecture

The app follows Clean Architecture + MVVM principles.

com.zenvy.app
│
├── data
│   ├── remote        // Firestore data sources
│   ├── local         // Room (Cart & Wishlist)
│   └── repository    // Single source of truth
│
├── domain
│   └── model         // Core business models
│
├── ui
│   ├── onboarding
│   ├── auth
│   ├── home
│   ├── product
│   ├── cart
│   ├── wishlist
│   └── profile
│
├── navigation        // Navigation Compose routes
├── di                // Hilt dependency injection
└── theme             // Material 3 theming

🔄 Data Flow
Firestore / Room
      ↓
Repository
      ↓
ViewModel (StateFlow)
      ↓
Jetpack Compose UI


Single source of truth

No direct database calls from UI

UI is fully state-driven

🛠️ Tech Stack
Android

Kotlin

Jetpack Compose

Material 3

Navigation Compose

MVVM Architecture

StateFlow & Coroutines

Hilt (Dependency Injection)

Backend & Storage

Firebase Authentication

Firebase Firestore (real-time updates)

Room Database (offline cart & wishlist)

Media & Utilities

Coil – Image loading

Jetpack DataStore – Preferences (onboarding state)

🚀 Key Engineering Highlights

✅ Real-time Firestore updates using Flow

✅ Proper handling of system insets (status bar, navigation bar, keyboard)

✅ No hardcoded UI paddings or hacks

✅ Fully tested on real devices

✅ Clean navigation without duplicate back stacks

✅ No dummy data in production flow

✅ Professional error & empty state handling

📱 Screens (Highlights)

Splash & Onboarding

Login / Register

Home (Products & Categories)

Product Detail

Cart

Wishlist

Profile

The UI is designed to feel modern, compact, and premium, similar to real-world shopping apps.

🧪 Testing & Quality

Tested on real Android devices

Handles:

Network failures

Empty states

Configuration changes

No crashes during navigation or data updates

🔮 Future Improvements (Not Implemented)

Payments integration

Admin panel

Order tracking

Push notifications

Multi-vendor support

(Intentionally excluded to keep the project portfolio-focused.)

👨‍💻 Author

Jay
Android Developer | ECE Background | Portfolio Project

GitHub: jay-kumar-android

Tech Focus: Android • Kotlin • Jetpack Compose • Firebase

📄 License

This project is for educational and portfolio purposes.
You are free to explore and learn from the code.

⭐ Final Note

Zenvy is not just an app — it is a demonstration of real Android engineering discipline, from UI polish to state management and architecture decisions.

If you are reviewing this project as a recruiter or engineer:

This code reflects how I would build and maintain a real production Android app.
