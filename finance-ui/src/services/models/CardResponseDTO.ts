/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CardResponseDTO = {
    id: string;
    cardNumberMasked: string;
    cardNumber: string;
    expirationDate: {
        year?: number;
        month?: CardResponseDTO.month;
        monthValue?: number;
        leapYear?: boolean;
    };
    cvvNumber: string;
};
export namespace CardResponseDTO {
    export enum month {
        JANUARY = 'JANUARY',
        FEBRUARY = 'FEBRUARY',
        MARCH = 'MARCH',
        APRIL = 'APRIL',
        MAY = 'MAY',
        JUNE = 'JUNE',
        JULY = 'JULY',
        AUGUST = 'AUGUST',
        SEPTEMBER = 'SEPTEMBER',
        OCTOBER = 'OCTOBER',
        NOVEMBER = 'NOVEMBER',
        DECEMBER = 'DECEMBER',
    }
}

