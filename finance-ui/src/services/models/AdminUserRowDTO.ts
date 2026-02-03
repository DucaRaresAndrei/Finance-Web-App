/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type AdminUserRowDTO = {
    username: string;
    email: string;
    category: AdminUserRowDTO.category;
};
export namespace AdminUserRowDTO {
    export enum category {
        BILLS = 'BILLS',
        ENTERTAINMENT = 'ENTERTAINMENT',
        FOOD = 'FOOD',
        SHOPPING = 'SHOPPING',
        TRANSPORTATION = 'TRANSPORTATION',
        USER = 'USER',
    }
}

