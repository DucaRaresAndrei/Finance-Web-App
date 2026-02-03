/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { TopupRequestDTO } from '../models/TopupRequestDTO';
import type { Unit } from '../models/Unit';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CardControllerService {
    /**
     * @param requestBody
     * @returns Unit OK
     * @throws ApiError
     */
    public static topup(
        requestBody: TopupRequestDTO,
    ): CancelablePromise<Unit> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/card/topup',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @returns Unit OK
     * @throws ApiError
     */
    public static deleteCard(): CancelablePromise<Unit> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/card',
        });
    }
}
