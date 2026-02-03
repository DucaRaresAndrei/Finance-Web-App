/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type TransactionDTO = {
    type: TransactionDTO.type;
    amount: number;
    description?: string;
    ibanSender?: string;
    ibanReceiver?: string;
    date: string;
    fromOrTo: string;
};
export namespace TransactionDTO {
    export enum type {
        INCOME = 'INCOME',
        EXPENSE = 'EXPENSE',
    }
}

