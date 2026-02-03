/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { LowBalanceAlertDTO } from '../models/LowBalanceAlertDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AlertControllerService {
    /**
     * @returns LowBalanceAlertDTO OK
     * @throws ApiError
     */
    public static checkLowBalance(): CancelablePromise<LowBalanceAlertDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/alerts/low-balance',
        });
    }
}
