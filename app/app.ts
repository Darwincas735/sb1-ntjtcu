import { Application } from '@nativescript/core';
import { firebase } from '@nativescript/firebase-core';

firebase().initializeApp({
    // Add your Firebase config here after creating the project in Firebase Console
});

Application.run({ moduleName: 'app-root' });