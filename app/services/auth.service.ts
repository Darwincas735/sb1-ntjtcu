import { firebase } from '@nativescript/firebase-core';
import '@nativescript/firebase-auth';
import { User } from '../models/user.model';

export class AuthService {
    private static instance: AuthService;
    
    static getInstance(): AuthService {
        if (!AuthService.instance) {
            AuthService.instance = new AuthService();
        }
        return AuthService.instance;
    }

    async login(username: string, password: string): Promise<User | null> {
        try {
            const auth = firebase().auth();
            const userCredential = await auth.signInWithEmailAndPassword(username, password);
            
            // Get user data from Realtime Database
            const database = firebase().database();
            const userData = await database.ref(`users/${userCredential.user.uid}`).once('value');
            
            return userData.val() as User;
        } catch (error) {
            console.error('Login error:', error);
            return null;
        }
    }

    async createUser(user: Omit<User, 'id'>): Promise<boolean> {
        try {
            const auth = firebase().auth();
            const userCredential = await auth.createUserWithEmailAndPassword(user.username, user.password);
            
            const database = firebase().database();
            await database.ref(`users/${userCredential.user.uid}`).set({
                ...user,
                id: userCredential.user.uid
            });
            
            return true;
        } catch (error) {
            console.error('Create user error:', error);
            return false;
        }
    }

    async logout(): Promise<void> {
        const auth = firebase().auth();
        await auth.signOut();
    }
}