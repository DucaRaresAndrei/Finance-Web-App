/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CategorySpendingDTO } from './CategorySpendingDTO';
import type { SummaryDTO } from './SummaryDTO';
import type { WeeklyBarDTO } from './WeeklyBarDTO';
export type DashboardStatsDTO = {
    monthSummary: SummaryDTO;
    weekSummary: SummaryDTO;
    weeklySpending: Array<WeeklyBarDTO>;
    spendingByCategory: Array<CategorySpendingDTO>;
};

