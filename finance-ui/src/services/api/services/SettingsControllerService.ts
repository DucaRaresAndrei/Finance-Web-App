/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SettingsRespDTO } from '../models/SettingsRespDTO';
import type { UpdateSettingsReqDTO } from '../models/UpdateSettingsReqDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SettingsControllerService {
    /**
     * @returns SettingsRespDTO OK
     * @throws ApiError
     */
    public static getSettings(): CancelablePromise<SettingsRespDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/settings',
        });
    }
    /**
     * @param requestBody
     * @returns SettingsRespDTO OK
     * @throws ApiError
     */
    public static updateSettings(
        requestBody: UpdateSettingsReqDTO,
    ): CancelablePromise<SettingsRespDTO> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/settings',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
