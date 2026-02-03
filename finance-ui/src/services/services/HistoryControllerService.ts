/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { HistorySummaryDTO } from '../models/HistorySummaryDTO';
import type { TransactionDTO } from '../models/TransactionDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class HistoryControllerService {
    /**
     * @returns HistorySummaryDTO OK
     * @throws ApiError
     */
    public static getHistory(): CancelablePromise<HistorySummaryDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/history',
        });
    }
    /**
     * @returns TransactionDTO OK
     * @throws ApiError
     */
    public static getRecentUserTransactions(): CancelablePromise<Array<TransactionDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/history/recent/users',
        });
    }
    /**
     * @returns TransactionDTO OK
     * @throws ApiError
     */
    public static getRecentFirmPayments(): CancelablePromise<Array<TransactionDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/history/recent/firms',
        });
    }
}
