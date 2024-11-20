export interface CensusEntry {
    id: string;
    fullName: string;
    dni: string;
    phoneNumbers: string[];
    votingCenter: string;
    createdBy: string;
    createdAt: Date;
}