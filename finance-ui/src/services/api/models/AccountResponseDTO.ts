/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CardResponseDTO } from './CardResponseDTO';
import type { HistorySummaryDTO } from './HistorySummaryDTO';
export type AccountResponseDTO = {
    card?: CardResponseDTO;
    history?: HistorySummaryDTO;
    category: AccountResponseDTO.category;
    iban: string;
    balance: number;
};
export namespace AccountResponseDTO {
    export enum category {
        BILLS = 'BILLS',
        ENTERTAINMENT = 'ENTERTAINMENT',
        FOOD = 'FOOD',
        SHOPPING = 'SHOPPING',
        TRANSPORTATION = 'TRANSPORTATION',
        USER = 'USER',
    }
}

