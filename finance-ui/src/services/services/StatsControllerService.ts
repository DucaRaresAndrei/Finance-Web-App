/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DashboardStatsDTO } from '../models/DashboardStatsDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class StatsControllerService {
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats3(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats2(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats5(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats6(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'OPTIONS',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats1(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'HEAD',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
    /**
     * @param offset
     * @returns DashboardStatsDTO OK
     * @throws ApiError
     */
    public static getDashboardStats4(
        offset?: number,
    ): CancelablePromise<DashboardStatsDTO> {
        return __request(OpenAPI, {
            method: 'PATCH',
            url: '/stats',
            query: {
                'offset': offset,
            },
        });
    }
}
