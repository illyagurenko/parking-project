export interface AddCarWithClientPayload {
    numberCar: string;
    fullName: string;
}

export interface Client{
    id: number,
    fullName: string,
}

export interface ParkingSpace{
    id: number,
    numberSpace: string,
}

export interface CarWithClient {
    id: number;
    numberCar: string;
    clientId: number;
    clientFullName: string;
}