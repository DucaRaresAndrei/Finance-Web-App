/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AdminUserRowDTO } from '../models/AdminUserRowDTO';
import type { EndpointRateDTO } from '../models/EndpointRateDTO';
import type { LabelValueDTO } from '../models/LabelValueDTO';
import type { MonitoringSummaryDTO } from '../models/MonitoringSummaryDTO';
import type { ServiceUpDTO } from '../models/ServiceUpDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AdminMonitoringControllerService {
    /**
     * @returns AdminUserRowDTO OK
     * @throws ApiError
     */
    public static users(): CancelablePromise<Array<AdminUserRowDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/admin/monitoring/users',
        });
    }
    /**
     * @param limit
     * @returns EndpointRateDTO OK
     * @throws ApiError
     */
    public static topEndpoints(
        limit: number = 10,
    ): CancelablePromise<Array<EndpointRateDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/admin/monitoring/top-endpoints',
            query: {
                'limit': limit,
            },
        });
    }
    /**
     * @returns MonitoringSummaryDTO OK
     * @throws ApiError
     */
    public static summary(): CancelablePromise<MonitoringSummaryDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/admin/monitoring/summary',
        });
    }
    /**
     * @returns ServiceUpDTO OK
     * @throws ApiError
     */
    public static servicesUp(): CancelablePromise<Array<ServiceUpDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/admin/monitoring/services-up',
        });
    }
    /**
     * @returns LabelValueDTO OK
     * @throws ApiError
     */
    public static requestsByStatus(): CancelablePromise<Array<LabelValueDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/admin/monitoring/requests-by-status',
        });
    }
}
