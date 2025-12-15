(ns net.eraserhead.arbor.webui.bluetooth
  (:require
   [re-frame.core :as rf]
   ["@awesome-cordova-plugins/bluetooth-classic-serial-port" :as bt-serial]))

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
   (-> (.list (.-BluetoothClassicSerialPort bt-serial))
       (.then (fn [devices]
                (let [device-list (into []
                                        (map (fn [device]
                                               {:id      (.-id device)
                                                :name    (.-name device)
                                                :address (.-address device)}))
                                        devices)]
                  (rf/dispatch [::device-list-arrived device-list]))))
       (.catch (fn [error]
                 (if (= "cordova_not_available" error)
                   (rf/dispatch [::device-list-arrived
                                 [{:id      "00:00:00:00:00:01"
                                   :name    "TestDRO1"
                                   :address "00:00:00:00:00:01"}
                                  {:id      "00:00:00:00:00:02"
                                   :name    "TestDRO2"
                                   :address "00:00:00:00:00:02"}]])
                   ;;FIXME: We need an error flash
                   (prn error)))))))

(rf/reg-event-fx
 ::fetch-device-list
 (fn [_ _]
   {::fetch-device-list nil}))
