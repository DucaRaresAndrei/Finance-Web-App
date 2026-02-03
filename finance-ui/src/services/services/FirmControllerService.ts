/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { FirmDTO } from '../models/FirmDTO';
import type { FirmListRequestDTO } from '../models/FirmListRequestDTO';
import type { Unit } from '../models/Unit';
import type { UpgradeFirmRequestDTO } from '../models/UpgradeFirmRequestDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class FirmControllerService {
    /**
     * @param requestBody
     * @returns FirmDTO OK
     * @throws ApiError
     */
    public static listFirms(
        requestBody: FirmListRequestDTO,
    ): CancelablePromise<Array<FirmDTO>> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/firms',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns Unit OK
     * @throws ApiError
     */
    public static upgradeToFirm(
        requestBody: UpgradeFirmRequestDTO,
    ): CancelablePromise<Unit> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/firms/upgrade',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
