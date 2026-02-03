import { ref } from 'vue'
import { AlertControllerService, type LowBalanceAlertDTO } from '@/services'

export function lowBalanceAlert() {
  const open = ref(false)
  const message = ref<string>('')

  async function checkAfterTransaction() {
    try {
      const res: LowBalanceAlertDTO = await AlertControllerService.checkLowBalance()
      if (res?.shouldAlert) {
        message.value = res.message ?? 'Your balance dropped below the configured threshold.'
        open.value = true
      }
    } catch {
    }
  }

  function close() { open.value = false }

  return { open, message, checkAfterTransaction, close }
}
