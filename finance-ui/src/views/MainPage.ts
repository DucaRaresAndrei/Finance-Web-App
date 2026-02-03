import {defineComponent, ref, onMounted, computed} from 'vue';
import AccountBalanceCard from '@/components/main/AccountBalanceCard.vue';
import SummaryStat from '@/components/main/SummaryStat.vue';
import TransactionList from '@/components/main/TransactionList.vue';
import {
  CardControllerService, type DashboardStatsDTO,
  HistoryControllerService,
  type HistorySummaryDTO, StatsControllerService, type TopupRequestDTO,
  UserControllerService,
  type UserResponseDTO
} from "@/services";
import SummaryCards from "@/components/statistics/summary-cards/SummaryCards.vue";
import StatsHeader from "@/components/statistics/header/StatsHeader.vue";


export default defineComponent({
  name: 'TransactionHistoryDesktop',
  components: {StatsHeader, SummaryCards, AccountBalanceCard, SummaryStat, TransactionList },
  setup() {
    const user = ref<UserResponseDTO | null>(null);
    const history = ref<HistorySummaryDTO | null>(null);

    const loading = ref(false);
    const error = ref<string | null>(null);

    const stats = ref<DashboardStatsDTO | null>(null);
    const selectedPeriod = ref<'week' | 'month'>('month');

    const refreshData = async () => {
      try {
        loading.value = true;
        const [u, h, s] = await Promise.all([
          UserControllerService.getCurrentUser(),
          HistoryControllerService.getHistory(),
          StatsControllerService.getDashboardStats(), // + ADD
        ]);
        user.value = u;
        history.value = h;
        stats.value = s;
      } catch (e: any) {
        error.value = e?.message ?? 'Failed to load data.';
      } finally {
        loading.value = false;
      }
    };

    onMounted(refreshData);

    const period = computed(() => selectedPeriod.value);
    const summary = computed(() => {
      if (!stats.value) return { incomes: 0, expense: 0, netSavings: 0 }
      return period.value === 'month' ? stats.value.monthSummary : stats.value.weekSummary
    })
    const summaryData = computed(() => ({
      income: summary.value?.incomes ?? 0,
      expenses: summary.value?.expense ?? 0,
      net: summary.value?.netSavings ?? 0,
    }))

    // Transaction list
    const transactions = computed(() => history.value?.lastTransactions ?? []);

    // Account card
    const balance = computed(() => user.value?.account?.balance ?? 0);
    const owner = computed(() => user.value?.fullName);
    const cardNumberMasked = computed(() =>
      user.value?.account?.card?.cardNumberMasked
    );
    const cardNumber = computed(() =>
      user.value?.account?.card?.cardNumber
    );
    const expirationDate = computed(() =>
      user.value?.account?.card?.expirationDate
    );
    const cvvNumber = computed(() =>
      user.value?.account?.card?.cvvNumber
    );

    // Topup button
    const showAddDialog = ref(false);
    const addAmount = ref<number | null>(null);
    const description = ref<string>('');

    const handleAddMoney = async () => {
      if (!addAmount.value || addAmount.value <= 0) {
        error.value = 'Please enter a valid amount.';
        return;
      }

      try {
        await CardControllerService.topup({
          amount: addAmount.value,
          description: description.value || 'Top up',
        } as TopupRequestDTO);

        await refreshData();

        showAddDialog.value = false;
        addAmount.value = null;
        description.value = '';
      } catch (e: any) {
        error.value = e?.message ?? 'Topup failed.';
      }
    };

    return {
      stats, selectedPeriod, summaryData, period,
      transactions,
      showAddDialog, addAmount, handleAddMoney, description,
      user, loading, error,
      balance, cardNumberMasked, cardNumber, expirationDate, cvvNumber, owner
    };

  },
});
