(ns net.eraserhead.arbor.bluetooth
  (:require
   [clojure.set :as set]
   [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::name string?)
(s/def ::address string?)

(s/def ::device (s/keys :req [::id ::name ::address]))
(s/def ::devices (s/map-of ::id ::device))
(s/def ::device-list (s/coll-of ::device))

(s/def ::db (s/keys :opt [::devices]))
(s/def ::effects (s/keys :req-un [::db]))

(defn device-list-arrived
  "When a new device list arrives, add new devices and remove missing devices
  from our internal device map, while keeping state of existing devices."
  [{:keys [db]} [_ device-list]]
  {:pre [(s/assert ::db db)
         (s/assert ::device-list device-list)]
   :post [(s/assert ::effects %)]}
  (let [db' (update db ::devices (fn [devices]
                                   (let [arrived   (into #{} (map ::id) device-list)
                                         have      (into #{} (keys devices))
                                         to-remove (set/difference have arrived)
                                         devices   (apply dissoc devices to-remove)

                                         updates   (reduce (fn [devices {:keys [::id], :as device}]
                                                             (assoc devices id device))
                                                           {}
                                                           device-list)
                                         devices   (merge-with merge devices updates)]
                                     (update-vals devices (fn [{:keys [::status],
                                                                :or {status :disconnected}
                                                                :as device}]
                                                            (assoc device ::status status))))))]
    {:db db'}))

(defn log-event
  [db device-id event-type event-data]
  (let [event {::id device-id
               ::event-type event-type
               ::event-data event-data}]
    (update db ::log (fnil conj []) event)))
