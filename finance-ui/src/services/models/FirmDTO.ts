/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type FirmDTO = {
    fullName: string;
    email: string;
    category: FirmDTO.category;
    iban: string;
};
export namespace FirmDTO {
    export enum category {
        BILLS = 'BILLS',
        ENTERTAINMENT = 'ENTERTAINMENT',
        FOOD = 'FOOD',
        SHOPPING = 'SHOPPING',
        TRANSPORTATION = 'TRANSPORTATION',
        USER = 'USER',
    }
}

