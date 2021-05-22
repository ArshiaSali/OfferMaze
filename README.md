# OfferMaze 
## Android App developed for CMPE-277 Final Project

## App Logo
![image](https://user-images.githubusercontent.com/37695314/119236037-17d6d180-baea-11eb-979b-e4400e2f450d.png)

## App Demo Video Link
https://www.youtube.com/watch?v=X4Tksnk5Z9c

## Abstract
An Android  App which allows the following features.
- An android app where users can register and add their new/used products at discounted rates and find buyers for their products. 
- Every user can buy the products uploaded by other users. 
- The users can keep a track of sold products and get future product recommendations based on their purchases.


## Features and Services Used

-	User Registration
- User Authentication (Using Google Firebase Authentication Service)
- Add User Profile details and upload Profile image(Google Firestore)
- Main Dashboard (Bottom Navigation Activity) 
  - Dashboard Fragment 
  - Products Fragment 
  - Orders Fragment
- Fragments use Recycler View to display the list.
- Upload New/Used Products with product details.
- Update/Edit User profile.
- Add products uploaded by other users to cart. 
- Update Quantities of products in Cart.
- Add multiple delivery addresses(Home/Office) 
  - Swipe to delete Functionality
- Payment API Integration (Razorpay Payment)
- List of placed orders.

## Architecture

![image](https://user-images.githubusercontent.com/37695314/119236123-8f0c6580-baea-11eb-8ad3-164fa1ef3a8f.png)

## Step-by-step Guide

- Clone or download the repository to your local machine.
- Open the project in Android Studio.
- Under the Gradle section, click on ‘signingReport’ to generate your SHA1 key.
- Once you’ve your SHA1 key ready, login to your Google Firebase Console with your google sign-in credentials.
- create a new project with this SHA1 key.
- Download the .json file with the credentials and place it into your app folder inside your project structure.
- Setup an emulator for your project with AVD Manager.
- Once the setup is complete, you can run the app.
- Now if you log into firebase console, you can see the authenticated users in the Firebase Authentication section and the details of entities stored in Firestore.
- To debug the code, run the app in Debug Mode in Android Studio.

For Payment Integration,
- Create an account in Razorpay and login to see the dashboard.
- Download your secret credentials and place your reazorpay test id as a string in the strings.xml file and read it in AndroidManifest.xml where it is currently reading from.
- You’re all good to go!
- Test and have fun!


