/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ExpenseRequestDTO } from '../models/ExpenseRequestDTO';
import type { PairExpenseIncome } from '../models/PairExpenseIncome';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ExpenseControllerService {
    /**
     * @param requestBody
     * @returns PairExpenseIncome OK
     * @throws ApiError
     */
    public static createExpense(
        requestBody: ExpenseRequestDTO,
    ): CancelablePromise<PairExpenseIncome> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/expense',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
