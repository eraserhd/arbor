(ns net.eraserhead.arbor.webui.bluetooth
  (:require
   [net.eraserhead.arbor.bluetooth :as bt]
   [re-frame.core :as rf]
   ["cordova-plugin-bluetooth-classic-serial-port/src/browser/bluetoothClassicSerial" :as bt-browser]))


(def bt-impl (atom bt-browser))

(.addEventListener js/document "deviceready"
  (fn []
    (if-let [impl js/bluetoothClassicSerial]
      (do
        (js/console.log "found cordova bluetooth serial implementation")
        (reset! bt-impl js/bluetoothClassicSerial))
      (js/console.log "using fallback (fake) bluetooth serial implementation"))))

(rf/reg-sub
 ::bt/devices
 (fn [{:keys [::bt/devices]} _]
   devices))

(rf/reg-event-fx ::bt/device-list-arrived bt/device-list-arrived)

(rf/reg-fx
 ::bt/fetch-device-list
 (fn fetch-device-list* []
   (.list @bt-impl
          (fn [devices]
            (let [device-list (into []
                                    (map (fn [device]
                                           {::bt/id      (.-id device)
                                            ::bt/name    (.-name device)
                                            ::bt/address (.-address device)}))
                                    devices)]
              (rf/dispatch [::bt/device-list-arrived device-list])))
          (fn [error]
            (js/alert (str "Unable to retrieve Bluetooth device list: " error))))))

(rf/reg-event-fx
 ::bt/fetch-device-list
 (fn [_ _]
   {::bt/fetch-device-list nil}))
