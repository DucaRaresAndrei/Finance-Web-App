/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type Expense = {
    id?: string;
    description: string;
    amount: number;
    date: string;
    ibanReceiver: string;
    ibanSender: string;
    category: Expense.category;
};
export namespace Expense {
    export enum category {
        BILLS = 'BILLS',
        ENTERTAINMENT = 'ENTERTAINMENT',
        FOOD = 'FOOD',
        SHOPPING = 'SHOPPING',
        TRANSPORTATION = 'TRANSPORTATION',
        USER = 'USER',
    }
}

