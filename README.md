# NotesApp

1. Only 1 activity for the entire app. Fagments are used for different screens.
2. When a user starts the app, a login screen appears (if the user is not logged in).
3. Login can only happen through Google Sign-In. Using shared preferences / SQLite to track logged-in users.
3. Once the user logs in, display all notes of the logged-in user. Recyclerview is used to display notes.
4. Floating button with "+" icon used to add a new note.
5. Users can update notes by clicking on specific note.
6. Allows the user to delete a note on swiping the note to right.
7. User can undo the delete by clicking undo on snackbar.

#### Note: This app uses google signin api. You need to do the following things to add google siognin to your app
> You can skip the dependency and code (already added), you just need to configure your app.
 ```
 1. visit "https://developers.google.com/identity/sign-in/android/start-integrating"
 2. go to "Configure a Google API Console projectConfigure a Google API Console project"
 3. click "Configure a project" button
 4. select a project (if already create) or create new project
 5. click NEXT
 6. select "Android" from the dropdown menu
 7. add package nane and SHA-1 (run keytool -keystore path-to-debug-or-production-keystore -list -v to get SHA-1 certificate)
 8. click CREATE
 9. you can download the file
 10. now you are good to go.

...
## Features
* Google Signin
* Add, Update and Delete notes

## Screenshots

- #####


- #####


- ##### >

## Contact
Created by [@Team fukset](https://github.com/neerajp67/) - feel free to contact me!