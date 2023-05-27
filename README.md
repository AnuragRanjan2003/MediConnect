# MediConnect : Your Personal Health Assistant
## Description
  MediConnect is an innovative Android application designed to assist users in evaluating their health condition based on symptoms they are experiencing. This app         provides a convenient and user-friendly platform to help users gain insights into possible diagnoses for their symptoms. By leveraging advanced medical algorithms and a     comprehensive symptom database, MediConnect empowers users to make informed decisions about their health.

![1-merged](https://github.com/AnuragRanjan2003/MediConnect/assets/100191258/039e62e4-e2ba-4643-8781-b893d93953b8)

## Key Features
   1. **Symptom Input :** Users can input their symptoms using a simple and intuitive interface. The app supports various input methods, such as text-based entry and  selecting symptoms from predefined list.
   
   2. **Intelligent Diagnosis :** The app utilizes an API which runs sophisticated algorithm to analyze the symptoms provided by the user. By comparing the symptoms against a vast medical knowledge base, it generates a list of potential diagnoses ranked by likelihood. 
   
   3. **Detailed Information :** For each potential diagnosis, the app provides detailed information about the condition, including common causes, risk factors, symptoms, and suggested treatments. Users can educate themselves about the conditions they may be facing.
   
   
   
   

 ## Working -
  1. **Authentication :** The application asks user to login or register with email and passord. The authentication is then handeled by Firebase Authentication.
  
  2. **History :** User can then see a list of all of his past diagnosis results. They can be archived/deleted by swiping. The data is fetched/stored from firebase.
  
  3. **Form :** A floating action button(FAB) redirects them to a form activity. The form deatils are fethed from the API using retrofit.The form details can then be filled out .
  
  4. **API Result:** On submission , the data is sent to the API which the generates a response(Diagnosis) which is then shown in the UI. The details of the diseases are fetched from the wikipedia API. For more details user can br redirects to the web browser with the disease as context.
  

## APIs used
 1. EndlessMedicalApi
 2. Wikipedia API

## TechStack
 1. Kotlin
 2. XML
 3. Retrofit
 4. MVVM
 5. Kotlin coroutines
 6. Firebase( Authentication, RealtimeDatabase )

  ###  Other Dependencies
 1. Glide
 2. MPCharts
 3. Lottie Animation
 4. Loading Shimmer
 

