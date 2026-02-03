/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AddFriendRequestDTO } from '../models/AddFriendRequestDTO';
import type { FriendResponseDTO } from '../models/FriendResponseDTO';
import type { SearchFriendRequestDTO } from '../models/SearchFriendRequestDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class FriendControllerService {
    /**
     * @param requestBody
     * @returns FriendResponseDTO OK
     * @throws ApiError
     */
    public static searchFriends(
        requestBody: SearchFriendRequestDTO,
    ): CancelablePromise<Array<FriendResponseDTO>> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/friends/search',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @param requestBody
     * @returns FriendResponseDTO OK
     * @throws ApiError
     */
    public static addFriend(
        requestBody: AddFriendRequestDTO,
    ): CancelablePromise<FriendResponseDTO> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/friends/add',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * @returns FriendResponseDTO OK
     * @throws ApiError
     */
    public static getFriends(): CancelablePromise<Array<FriendResponseDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/friends',
        });
    }
}
