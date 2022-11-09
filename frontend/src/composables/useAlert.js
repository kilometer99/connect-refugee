import { createToaster } from "@meforma/vue-toaster";
const toaster = createToaster({position:'top-right',maxToasts:1,duration:3000 });
export function useAlert() {
  function showAlert(message,type='success') {
    toaster.show(message, {type});
  }

  function hideAlert() {
    toaster.clear()
  }
  return {hideAlert,showAlert}
}
