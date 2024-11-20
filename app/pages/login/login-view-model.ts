import { Observable } from '@nativescript/core';
import { AuthService } from '../../services/auth.service';

export class LoginViewModel extends Observable {
    private authService = AuthService.getInstance();
    
    username: string = '';
    password: string = '';
    isAdminLoginVisible: boolean = false;

    async onLogin() {
        if (!this.username || !this.password) {
            alert('Please enter username and password');
            return;
        }

        const user = await this.authService.login(this.username, this.password);
        if (user) {
            // Navigate to appropriate page based on user role
            const frame = require('@nativescript/core').Frame;
            frame.topmost().navigate({
                moduleName: user.role === 'admin' 
                    ? 'pages/admin/admin-page' 
                    : 'pages/census/census-page',
                context: { user }
            });
        } else {
            alert('Invalid credentials');
        }
    }

    onAdminLogin() {
        const frame = require('@nativescript/core').Frame;
        frame.topmost().navigate('pages/admin/admin-login-page');
    }
}