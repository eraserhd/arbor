(ns net.eraserhead.arbor.webui.bluetooth
  (:require
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
 ::devices
 (fn [{:keys [::devices]} _]
   devices))

(rf/reg-event-db
 ::device-list-arrived
 (fn [db [_ device-list]]
   (prn "got devices:" device-list)
   (assoc db ::devices device-list)))

(rf/reg-fx
 ::fetch-device-list
 (fn fetch-device-list* []
   (.list @bt-impl
          (fn [devices]
            (let [device-list (into []
                                    (map (fn [device]
                                           {:id      (.-id device)
                                            :name    (.-name device)
                                            :address (.-address device)}))
                                    devices)]
              (rf/dispatch [::device-list-arrived device-list])))
          (fn [error]
            (js/alert (str "Unable to retrieve Bluetooth device list: "
                           error))))))

(rf/reg-event-fx
 ::fetch-device-list
 (fn [_ _]
   {::fetch-device-list nil}))
