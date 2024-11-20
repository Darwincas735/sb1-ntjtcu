import { firebase } from '@nativescript/firebase-core';
import '@nativescript/firebase-database';
import { CensusEntry } from '../models/census.model';

export class CensusService {
    private static instance: CensusService;
    
    static getInstance(): CensusService {
        if (!CensusService.instance) {
            CensusService.instance = new CensusService();
        }
        return CensusService.instance;
    }

    async addEntry(entry: Omit<CensusEntry, 'id'>): Promise<string> {
        const database = firebase().database();
        const newEntryRef = database.ref('census').push();
        
        await newEntryRef.set({
            ...entry,
            id: newEntryRef.key
        });
        
        return newEntryRef.key;
    }

    async getEntriesByVotingCenter(votingCenter: string): Promise<CensusEntry[]> {
        const database = firebase().database();
        const snapshot = await database
            .ref('census')
            .orderByChild('votingCenter')
            .equalTo(votingCenter)
            .once('value');
            
        const entries: CensusEntry[] = [];
        snapshot.forEach((childSnapshot) => {
            entries.push(childSnapshot.val() as CensusEntry);
        });
        
        return entries;
    }
}