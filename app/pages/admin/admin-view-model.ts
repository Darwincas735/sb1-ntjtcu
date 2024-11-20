import { Observable } from '@nativescript/core';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

export class AdminViewModel extends Observable {
    private authService = AuthService.getInstance();

    newUser = {
        username: '',
        password: '',
        votingCenter: '',
        role: 'user' as 'admin' | 'user'
    };

    users: User[] = [];
    roleIndex: number = 0;

    async onCreateUser() {
        if (!this.newUser.username || !this.newUser.password || !this.newUser.votingCenter) {
            alert('Please fill all fields');
            return;
        }

        const success = await this.authService.createUser({
            ...this.newUser,
            role: this.roleIndex === 1 ? 'admin' : 'user'
        });

        if (success) {
            alert('User created successfully');
            this.clearForm();
        } else {
            alert('Error creating user');
        }
    }

    private clearForm() {
        this.newUser = {
            username: '',
            password: '',
            votingCenter: '',
            role: 'user'
        };
        this.roleIndex = 0;
        this.notifyPropertyChange('newUser', this.newUser);
        this.notifyPropertyChange('roleIndex', 0);
    }

    async onLogout() {
        await this.authService.logout();
        const frame = require('@nativescript/core').Frame;
        frame.topmost().navigate({
            moduleName: 'pages/login/login-page',
            clearHistory: true
        });
    }
}