import { Observable } from '@nativescript/core';
import { CensusService } from '../../services/census.service';
import { AuthService } from '../../services/auth.service';
import { CensusEntry } from '../../models/census.model';
import { User } from '../../models/user.model';

export class CensusViewModel extends Observable {
    private censusService = CensusService.getInstance();
    private authService = AuthService.getInstance();
    private currentUser: User;

    fullName: string = '';
    dni: string = '';
    phoneNumber: string = '';
    entries: CensusEntry[] = [];

    constructor(user: User) {
        super();
        this.currentUser = user;
        this.loadEntries();
    }

    get votingCenter(): string {
        return this.currentUser.votingCenter;
    }

    async onSaveEntry() {
        if (!this.fullName || !this.dni || !this.phoneNumber) {
            alert('Please fill all fields');
            return;
        }

        const entry: Omit<CensusEntry, 'id'> = {
            fullName: this.fullName,
            dni: this.dni,
            phoneNumbers: [this.phoneNumber],
            votingCenter: this.currentUser.votingCenter,
            createdBy: this.currentUser.id,
            createdAt: new Date()
        };

        await this.censusService.addEntry(entry);
        this.clearForm();
        this.loadEntries();
    }

    private clearForm() {
        this.fullName = '';
        this.dni = '';
        this.phoneNumber = '';
        this.notifyPropertyChange('fullName', '');
        this.notifyPropertyChange('dni', '');
        this.notifyPropertyChange('phoneNumber', '');
    }

    async loadEntries() {
        this.entries = await this.censusService.getEntriesByVotingCenter(this.currentUser.votingCenter);
        this.notifyPropertyChange('entries', this.entries);
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