# PhotoBooth

This project is a simplified photo booth application which allows the user to take photos and see previously taken photos.
Provide 2 options in the main screen:
● Take a photo
● View Photos

# Usage

The released apk is under [app/release](https://github.com/shtzsp/PhotoBooth/tree/master/app/release) folder. You can download, install and run it on a Android phone(Android 5.0+).

# Development environment

Android Studio 3.5
Build #AI-191.8026.42.35.5791312, built on August 9, 2019

Gradle version 5.4.1

# Implementation

It adopts "Single Activity + Multi-Fragments" architecture. 
Use [Glide](https://github.com/bumptech/glide) library to handle images.

# To be improved

UI doesn't look so good.

Not test on various Android version phones.

Only English string resource provided, lack Chinese string.

Not consider all of the edge cases.

Need ORM framework layer for DB access when app is complicated enough.

For the complicated application, consider to adopt [Fragmentation](https://github.com/YoKeyword/Fragmentation) library