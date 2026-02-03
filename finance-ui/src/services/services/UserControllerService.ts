/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { UserResponseDTO } from '../models/UserResponseDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UserControllerService {
    /**
     * @returns UserResponseDTO OK
     * @throws ApiError
     */
    public static getCurrentUser(): CancelablePromise<UserResponseDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/user',
        });
    }
}
